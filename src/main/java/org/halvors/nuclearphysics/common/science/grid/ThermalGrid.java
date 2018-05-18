package org.halvors.nuclearphysics.common.science.grid;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.api.fluid.IBoilHandler;
import org.halvors.nuclearphysics.api.tile.IReactor;
import org.halvors.nuclearphysics.common.NuclearPhysics;
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

            // Deal with different block types.
            final double currentTemperature = getTemperature(world, pos);

            if (currentTemperature < 0) {
                thermalSource.remove(key);
            } else {
                final double deltaFromEquilibrium = getDefaultTemperature(world, pos) - currentTemperature;
                final TileEntity tile = pos.getTileEntity(world);
                final TileEntity tileUp = pos.offset(ForgeDirection.UP).getTileEntity(world);
                final boolean isReactor = tile instanceof IReactor || tileUp instanceof IBoilHandler;

                final ThermalUpdateEvent event = new ThermalUpdateEvent(world, pos, currentTemperature, deltaFromEquilibrium, DELTA_TIME, isReactor);
                MinecraftForge.EVENT_BUS.post(event);

                addTemperature(world, pos, (deltaFromEquilibrium > 0 ? 1 : -1) * Math.min(Math.abs(deltaFromEquilibrium), Math.abs(event.getHeatLoss())));

                // Spread heat to surrounding.
                for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                    BlockPos adjacentPos = pos.offset(side);

                    final double deltaTemperature = getTemperature(world, pos) - getTemperature(world, adjacentPos);
                    final Material adjacentMaterial = adjacentPos.getBlock(world).getMaterial();
                    final double deltaSpread = (adjacentMaterial.isSolid() ? SPREAD : SPREAD / 2) * DELTA_TIME;

                    if (deltaTemperature > 0) {
                        addTemperature(world, adjacentPos, deltaTemperature * deltaSpread);
                        addTemperature(world, pos, -deltaTemperature * deltaSpread);
                    }
                }
            }
        }
    }

    public boolean canUpdate() {
        return !NuclearPhysics.getProxy().isPaused();
    }

    public boolean continueUpdate() {
        return true;
    }
}
