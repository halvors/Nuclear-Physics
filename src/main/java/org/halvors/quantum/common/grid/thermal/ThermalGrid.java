package org.halvors.quantum.common.grid.thermal;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.halvors.quantum.api.tile.IReactor;
import org.halvors.quantum.common.event.ThermalEvent.ThermalUpdateEvent;
import org.halvors.quantum.common.grid.IUpdate;
import org.halvors.quantum.common.utility.type.Pair;

import java.util.HashMap;
import java.util.Map.Entry;

public class ThermalGrid implements IUpdate {
    private static final HashMap<Pair<World, BlockPos>, Float> thermalSource = new HashMap<>();

    private final float spread = 1 / 7F;
    //private final float loss = 0.1F;
    //private int tick = 0;
    private final float deltaTime = 1 / 20F;

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
        for (Entry<Pair<World, BlockPos>, Float> entry : new HashMap<>(thermalSource).entrySet()) { // Use thermalSource HashMap directly without new?
            // Distribute temperature
            World world = entry.getKey().getLeft();
            BlockPos pos = entry.getKey().getRight();

            // Deal with different block types.
            float currentTemperature = getTemperature(world, pos);

            if (currentTemperature < 0) {
                thermalSource.remove(new Pair<>(world, pos));
            } else {
                float deltaFromEquilibrium = getDefaultTemperature(world, pos) - currentTemperature;

                TileEntity tile = world.getTileEntity(pos);
                boolean isReactor = tile != null && tile instanceof IReactor;

                ThermalUpdateEvent event = new ThermalUpdateEvent(world, pos, currentTemperature, deltaFromEquilibrium, deltaTime, isReactor);
                MinecraftForge.EVENT_BUS.post(event);

                float loss = event.heatLoss;
                addTemperature(world, pos, (deltaFromEquilibrium > 0 ? 1 : -1) * Math.min(Math.abs(deltaFromEquilibrium), Math.abs(loss)));

                // Spread heat to surrounding.
                for (EnumFacing side : EnumFacing.VALUES) {
                    BlockPos adjacentPos = pos.offset(side);

                    float deltaTemperature = getTemperature(world, pos) - getTemperature(world, adjacentPos);
                    Material adjacentMaterial = world.getBlockState(adjacentPos).getBlock().getBlockState().getBaseState().getMaterial();
                    float spread = (adjacentMaterial.isSolid() ? this.spread : this.spread / 2) * deltaTime;

                    if (deltaTemperature > 0) {
                        addTemperature(world, adjacentPos, deltaTemperature * spread);
                        addTemperature(world, pos, -deltaTemperature * spread);
                    }
                }
            }
        }
    }

    public boolean canUpdate() {
        return !Minecraft.getMinecraft().isGamePaused();
    }

    public boolean continueUpdate() {
        return true;
    }
}
