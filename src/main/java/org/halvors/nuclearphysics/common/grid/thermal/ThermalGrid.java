package org.halvors.nuclearphysics.common.grid.thermal;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.api.fluid.IBoilHandler;
import org.halvors.nuclearphysics.api.tile.IReactor;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.event.ThermalEvent.ThermalUpdateEvent;
import org.halvors.nuclearphysics.common.grid.IUpdate;
import org.halvors.nuclearphysics.common.type.Pair;
import org.halvors.nuclearphysics.common.type.Position;

import java.util.HashMap;
import java.util.Map.Entry;

public class ThermalGrid implements IUpdate {
    private static final HashMap<Pair<World, Position>, Float> thermalSource = new HashMap<>();

    private static final float spread = 1 / 7F;
    private static final float deltaTime = 1 / 20F;

    public static float getDefaultTemperature(World world, Position pos) {
        return ThermalPhysics.getTemperatureForCoordinate(world, pos);
    }

    public static void addTemperature(World world, Position pos, float deltaTemperature) {
        float defaultTemperature = getDefaultTemperature(world, pos);
        float original = thermalSource.getOrDefault(new Pair<>(world, pos), defaultTemperature);
        float newTemperature = original + deltaTemperature;

        if (Math.abs(newTemperature - defaultTemperature) > 0.4) {
            thermalSource.put(new Pair<>(world, pos), original + deltaTemperature);
        } else {
            thermalSource.remove(new Pair<>(world, pos));
        }
    }

    public static float getTemperature(World world, Position pos) {
        if (thermalSource.containsKey(new Pair<>(world, pos))) {
            return thermalSource.get(new Pair<>(world, pos));
        }

        return ThermalPhysics.getTemperatureForCoordinate(world, pos);
    }

    @Override
    public void update() {
        for (Entry<Pair<World, Position>, Float> entry : new HashMap<>(thermalSource).entrySet()) {
            // Distribute temperature
            final World world = entry.getKey().getLeft();
            final Position pos = entry.getKey().getRight();

            // Deal with different block types.
            float currentTemperature = getTemperature(world, pos);

            if (currentTemperature < 0) {
                thermalSource.remove(new Pair<>(world, pos));
            } else {
                float deltaFromEquilibrium = getDefaultTemperature(world, pos) - currentTemperature;
                boolean isReactor = pos.getTileEntity(world) instanceof IReactor || world.getTileEntity(pos.getIntX(), pos.getIntY() + 1, pos.getIntZ()) instanceof IBoilHandler;

                ThermalUpdateEvent event = new ThermalUpdateEvent(world, pos.getIntX(), pos.getIntY(), pos.getIntZ(), currentTemperature, deltaFromEquilibrium, deltaTime, isReactor);
                MinecraftForge.EVENT_BUS.post(event);

                addTemperature(world, pos, (deltaFromEquilibrium > 0 ? 1 : -1) * Math.min(Math.abs(deltaFromEquilibrium), Math.abs(event.getHeatLoss())));

                // Spread heat to surrounding.
                for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                    Position adjacentPos = pos.offset(side);

                    float deltaTemperature = getTemperature(world, pos) - getTemperature(world, adjacentPos);
                    Material adjacentMaterial = world.getBlock(adjacentPos.getIntX(), adjacentPos.getIntY(), adjacentPos.getIntZ()).getMaterial();
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
