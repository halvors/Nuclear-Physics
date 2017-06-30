package org.halvors.quantum.common.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.halvors.quantum.common.utility.transform.vector.VectorWorld;

import java.security.PublicKey;

public class ThermalEvent extends Event {
    private final IBlockAccess world;
    private final BlockPos pos;
    public final float temperature;
    public final float deltaTemperature;
    public final float deltaTime;
    public float heatLoss = 0.1F;
    public boolean isReactor = false;

    public ThermalEvent(IBlockAccess world, BlockPos pos, float temperature, float deltaTemperature, float deltaTime, boolean isReactor) {
        this.world = world;
        this.pos = pos;
        this.temperature = temperature;
        this.deltaTemperature = deltaTemperature;
        this.deltaTime = deltaTime;
        this.isReactor = isReactor;
    }

    public IBlockAccess getWorld() {
        return world;
    }

    public BlockPos getPos() {
        return pos;
    }

    public static class ThermalUpdateEvent extends ThermalEvent {
        public ThermalUpdateEvent(World world, BlockPos pos, float temperature, float deltaTemperature, float deltaTime, boolean isReactor) {
            super(world, pos, temperature, deltaTemperature, deltaTime, isReactor);
        }
    }
}
