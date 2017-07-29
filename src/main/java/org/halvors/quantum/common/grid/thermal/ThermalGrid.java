package org.halvors.quantum.common.grid.thermal;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.halvors.quantum.api.tile.IReactor;
import org.halvors.quantum.common.event.ThermalEvent.ThermalUpdateEvent;
import org.halvors.quantum.common.grid.IUpdate;
import org.halvors.quantum.common.utility.location.Location;
import org.halvors.quantum.common.utility.transform.vector.VectorWorld;

import java.util.HashMap;
import java.util.Map.Entry;

public class ThermalGrid implements IUpdate {
    private static final HashMap<VectorWorld, Float> thermalSource = new HashMap<>();

    private final float spread = 1 / 7F;
    //private final float loss = 0.1F;
    //private int tick = 0;
    private final float deltaTime = 1 / 20F;

    public static float getDefaultTemperature(World world, BlockPos pos) {
        return ThermalPhysics.getTemperatureForCoordinate(world, pos);
    }

    public static void addTemperature(World world, BlockPos pos, float deltaTemperature) {
        float defaultTemperature = getDefaultTemperature(world, pos);
        float original = thermalSource.getOrDefault(new VectorWorld(world, pos.getX(), pos.getY(), pos.getZ()), defaultTemperature);
        float newTemperature = original + deltaTemperature;

        if (Math.abs(newTemperature - defaultTemperature) > 0.4) {
            thermalSource.put(new VectorWorld(world, pos.getX(), pos.getY(), pos.getZ()), original + deltaTemperature);
        } else {
            thermalSource.remove(new VectorWorld(world, pos.getX(), pos.getY(), pos.getZ()));
        }
    }

    public static float getTemperature(World world, BlockPos pos) {
        if (thermalSource.containsKey(new VectorWorld(world, pos.getX(), pos.getY(), pos.getZ()))) {
            return thermalSource.get(new VectorWorld(world, pos.getX(), pos.getY(), pos.getZ()));
        }

        return ThermalPhysics.getTemperatureForCoordinate(world, pos);
    }

    @Override
    public void update() {
        for (Entry<VectorWorld, Float> entry : new HashMap<>(thermalSource).entrySet()) {
            // Distribute temperature
            VectorWorld pos = entry.getKey();
            Location location = new Location(pos.getWorld(), new BlockPos(pos.getX(), pos.getY(), pos.getZ()));

            /** Deal with different block types */
            float currentTemperature = getTemperature(location.getWorld(), location.getPos());

            if (currentTemperature < 0) {
                thermalSource.remove(pos);
            } else {
                float deltaFromEquilibrium = getDefaultTemperature(location.getWorld(), location.getPos()) - currentTemperature;

                TileEntity possibleReactor = location.getTileEntity();
                boolean isReactor = possibleReactor != null && possibleReactor instanceof IReactor;

                ThermalUpdateEvent event = new ThermalUpdateEvent(location.getWorld(), location.getPos(), currentTemperature, deltaFromEquilibrium, deltaTime, isReactor);
                MinecraftForge.EVENT_BUS.post(event);

                float loss = event.heatLoss;
                addTemperature(location.getWorld(), location.getPos(), (deltaFromEquilibrium > 0 ? 1 : -1) * Math.min(Math.abs(deltaFromEquilibrium), Math.abs(loss)));

                // Spread heat to surrounding.
                for (EnumFacing dir : EnumFacing.VALUES) {
                    // TODO: Convert to Location.
                    VectorWorld adjacent = (VectorWorld) pos.clone().translate(dir);

                    /*
                    double x = pos.getX() + dir.getFrontOffsetX() * dir.getFrontOffsetX();
                    double y = pos.getY() + dir.getFrontOffsetY() * dir.getFrontOffsetY();
                    double z = pos.getZ() + dir.getFrontOffsetZ() * dir.getFrontOffsetZ();

                    Location adjacent = new Location(pos.getWorld(), new BlockPos(x, y, z));
                    */

                    float deltaTemperature = getTemperature(location.getWorld(), location.getPos()) - getTemperature(adjacent.getWorld(), new BlockPos(adjacent.getX(), adjacent.getY(), adjacent.getZ()));
                    Material adjacentMaterial = adjacent.getBlock(adjacent.world).getBlockState().getBaseState().getMaterial();
                    float spread = (adjacentMaterial.isSolid() ? this.spread : this.spread / 2) * deltaTime;

                    if (deltaTemperature > 0) {
                        addTemperature(adjacent.getWorld(), new BlockPos(adjacent.getX(), adjacent.getY(), adjacent.getZ()), deltaTemperature * spread);
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
