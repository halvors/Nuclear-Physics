package org.halvors.quantum.common.grid.thermal;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.halvors.quantum.api.tile.IReactor;
import org.halvors.quantum.common.event.ThermalEvent;
import org.halvors.quantum.common.grid.IUpdate;
import org.halvors.quantum.common.utility.location.Location;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class ThermalGrid implements IUpdate {
    private final float spread = 1 / 7F;
    private final float loss = 0.1F;
    private static final HashMap<Location, Float> thermalSource = new HashMap<>();
    private int tick = 0;
    private final float deltaTime = 1 / 20F;

    public static float getDefaultTemperature(World world, BlockPos pos) {
        return ThermalPhysics.getTemperatureForCoordinate(world, pos);
    }

    public static void addTemperature(World world, BlockPos pos, float deltaTemperature) {
        final Location location = new Location(world, pos);
        float defaultTemperature = getDefaultTemperature(world, pos);
        float original = thermalSource.getOrDefault(location, defaultTemperature);
        float newTemperature = original + deltaTemperature;

        if (Math.abs(newTemperature - defaultTemperature) > 0.4) {
            thermalSource.put(location, original + deltaTemperature);
        } else {
            thermalSource.remove(location);
        }
    }

    public static float getTemperature(World world, BlockPos pos) {
        final Location location = new Location(world, pos);

        if (thermalSource.containsKey(location)) {
            return thermalSource.get(location);
        }

        return ThermalPhysics.getTemperatureForCoordinate(world, pos);
    }

    @Override
    public void update() {
        Iterator<Entry<Location, Float>> it = new HashMap<>(thermalSource).entrySet().iterator();

        while (it.hasNext()) {
            Entry<Location, Float> entry = it.next();

            // Distribute temperature
            Location location = entry.getKey();

            /** Deal with different block types */
            float currentTemperature = getTemperature(location.getWorld(),location.getPos());

            if (currentTemperature < 0) {
                thermalSource.remove(location);

                continue;
            } else {
                float deltaFromEquilibrium = getDefaultTemperature(location.getWorld(), location.getPos()) - currentTemperature;

                TileEntity possibleReactor = location.getWorld().getTileEntity(location.getPos());
                boolean isReactor = possibleReactor != null && possibleReactor instanceof IReactor;

                ThermalEvent.ThermalUpdateEvent event = new ThermalEvent.ThermalUpdateEvent(location.getWorld(), location.getPos(), currentTemperature, deltaFromEquilibrium, deltaTime, isReactor);
                MinecraftForge.EVENT_BUS.post(event);

                float loss = event.heatLoss;
                addTemperature(location.getWorld(), location.getPos(), (deltaFromEquilibrium > 0 ? 1 : -1) * Math.min(Math.abs(deltaFromEquilibrium), Math.abs(loss)));

                // Spread heat to surrounding.
                for (EnumFacing dir : EnumFacing.VALUES) {
                    World adjacentWorld = location.getWorld();
                    BlockPos adjacentPos = location.getPos().offset(dir);

                    float deltaTemperature = getTemperature(location.getWorld(), location.getPos()) - getTemperature(adjacentWorld, adjacentPos);
                    Material adjacentMaterial = adjacentWorld.getBlockState(adjacentPos).getBlock().getBlockState().getBaseState().getMaterial();
                    float spread = (adjacentMaterial.isSolid() ? this.spread : this.spread / 2) * deltaTime;

                    if (deltaTemperature > 0) {
                        addTemperature(adjacentWorld, adjacentPos, deltaTemperature * spread);
                        addTemperature(location.getWorld(), location.getPos(), -deltaTemperature * spread);
                    }
                }
            }
        }
    }

    public boolean canUpdate() {
        return true;
    }

    public boolean continueUpdate() {
        return true;
    }
}
