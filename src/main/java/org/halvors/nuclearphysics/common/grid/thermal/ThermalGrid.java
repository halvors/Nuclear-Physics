package org.halvors.nuclearphysics.common.grid.thermal;

import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.halvors.nuclearphysics.api.fluid.IBoilHandler;
import org.halvors.nuclearphysics.api.tile.IReactor;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.event.ThermalEvent.ThermalUpdateEvent;
import org.halvors.nuclearphysics.common.grid.IUpdate;
import org.halvors.nuclearphysics.common.type.Pair;

import java.util.HashMap;
import java.util.Map.Entry;

public class ThermalGrid implements IUpdate {
    private static final HashMap<Pair<World, BlockPos>, Float> thermalSource = new HashMap<>();

    private static final float spread = 1 / 7F;
    private static final float deltaTime = 1 / 20F;

    public static float getDefaultTemperature(World world, BlockPos pos) {
        return ThermalPhysics.getTemperatureForCoordinate(world, pos);
    }

    public static void addTemperature(World world, BlockPos pos, float deltaTemperature) {
        float defaultTemperature = getDefaultTemperature(world, pos);
        float original = thermalSource.getOrDefault(new Pair<>(world, pos), defaultTemperature);
        float newTemperature = original + deltaTemperature;

        if (Math.abs(newTemperature - defaultTemperature) > 0.4) {
            thermalSource.put(new Pair<>(world, pos), original + deltaTemperature);
        } else {
            thermalSource.remove(new Pair<>(world, pos));
        }
    }

    public static float getTemperature(World world, BlockPos pos) {
        if (thermalSource.containsKey(new Pair<>(world, pos))) {
            return thermalSource.get(new Pair<>(world, pos));
        }

        return ThermalPhysics.getTemperatureForCoordinate(world, pos);
    }

    @Override
    public void update() {
        for (Entry<Pair<World, BlockPos>, Float> entry : new HashMap<>(thermalSource).entrySet()) {
            // Distribute temperature
            final World world = entry.getKey().getLeft();
            final BlockPos pos = entry.getKey().getRight();

            // Deal with different block types.
            float currentTemperature = getTemperature(world, pos);

            if (currentTemperature < 0) {
                thermalSource.remove(new Pair<>(world, pos));
            } else {
                float deltaFromEquilibrium = getDefaultTemperature(world, pos) - currentTemperature;
                boolean isReactor = world.getTileEntity(pos) instanceof IReactor || world.getTileEntity(pos.up()) instanceof IBoilHandler;

                ThermalUpdateEvent event = new ThermalUpdateEvent(world, pos, currentTemperature, deltaFromEquilibrium, deltaTime, isReactor);
                MinecraftForge.EVENT_BUS.post(event);

                addTemperature(world, pos, (deltaFromEquilibrium > 0 ? 1 : -1) * Math.min(Math.abs(deltaFromEquilibrium), Math.abs(event.getHeatLoss())));

                // Spread heat to surrounding.
                for (EnumFacing side : EnumFacing.values()) {
                    BlockPos adjacentPos = pos.offset(side);

                    float deltaTemperature = getTemperature(world, pos) - getTemperature(world, adjacentPos);
                    Material adjacentMaterial = world.getBlockState(adjacentPos).getBlock().getBlockState().getBaseState().getMaterial();
                    float deltaSpread = (adjacentMaterial.isSolid() ? spread : spread / 2) * deltaTime;

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
