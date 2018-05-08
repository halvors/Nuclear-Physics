package org.halvors.nuclearphysics.common.science.grid;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.halvors.nuclearphysics.api.tile.IReactor;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.capabilities.CapabilityBoilHandler;
import org.halvors.nuclearphysics.common.event.ThermalEvent.ThermalUpdateEvent;
import org.halvors.nuclearphysics.common.science.physics.ThermalPhysics;
import org.halvors.nuclearphysics.common.type.Pair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThermalGrid implements IGrid {
    private static final Map<Pair<World, BlockPos>, Double> thermalSource = new ConcurrentHashMap<>();
    private static final double SPREAD = 1D / 7;
    private static final double DELTA_TIME = 1D / 20;

    public static double getDefaultTemperature(final World world, final BlockPos pos) {
        return ThermalPhysics.getTemperatureForCoordinate(world, pos);
    }

    public static double getTemperature(final World world, final BlockPos pos) {
        final Pair<World, BlockPos> key = new Pair<>(world, pos);

        if (thermalSource.containsKey(key)) {
            return thermalSource.get(key);
        }

        return ThermalPhysics.getTemperatureForCoordinate(world, pos);
    }

    public static void addTemperature(final World world, final BlockPos pos, final double deltaTemperature) {
        final Pair<World, BlockPos> key = new Pair<>(world, pos);
        final double defaultTemperature = getDefaultTemperature(world, pos);
        final double original = thermalSource.getOrDefault(key, defaultTemperature);
        final double newTemperature = original + deltaTemperature;

        if (Math.abs(newTemperature - defaultTemperature) > 0.4) {
            thermalSource.put(key, newTemperature);
        } else {
            thermalSource.remove(key);
        }
    }

    @Override
    public void update() {
        for (Pair<World, BlockPos> key : thermalSource.keySet()) {
            final World world = key.getLeft();
            final BlockPos pos = key.getRight();

            NuclearPhysics.getProxy().addScheduledTask(() -> {
                // Deal with different block types.
                final double currentTemperature = getTemperature(world, pos);

                if (currentTemperature < 0) {
                    thermalSource.remove(key);
                } else {
                    final double deltaFromEquilibrium = getDefaultTemperature(world, pos) - currentTemperature;
                    final TileEntity tile = world.getTileEntity(pos);
                    final TileEntity tileUp = world.getTileEntity(pos.up());
                    final boolean isReactor = tile instanceof IReactor || tileUp != null && tileUp.hasCapability(CapabilityBoilHandler.BOIL_HANDLER_CAPABILITY, EnumFacing.DOWN);

                    final ThermalUpdateEvent event = new ThermalUpdateEvent(world, pos, currentTemperature, deltaFromEquilibrium, DELTA_TIME, isReactor);
                    MinecraftForge.EVENT_BUS.post(event);

                    addTemperature(world, pos, (deltaFromEquilibrium > 0 ? 1 : -1) * Math.min(Math.abs(deltaFromEquilibrium), Math.abs(event.getHeatLoss())));

                    // Spread heat to surrounding.
                    for (EnumFacing side : EnumFacing.values()) {
                        final BlockPos adjacentPos = pos.offset(side);

                        final double deltaTemperature = getTemperature(world, pos) - getTemperature(world, adjacentPos);
                        final Material adjacentMaterial = world.getBlockState(adjacentPos).getBlock().getBlockState().getBaseState().getMaterial();
                        final double deltaSpread = (adjacentMaterial.isSolid() ? SPREAD : SPREAD / 2) * DELTA_TIME;

                        if (deltaTemperature > 0) {
                            addTemperature(world, adjacentPos, deltaTemperature * deltaSpread);
                            addTemperature(world, pos, -deltaTemperature * deltaSpread);
                        }
                    }
                }
            }, world);
        }
    }

    public boolean canUpdate() {
        return !NuclearPhysics.getProxy().isPaused();
    }

    public boolean continueUpdate() {
        return true;
    }
}
