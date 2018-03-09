package org.halvors.nuclearphysics.common.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ThermalEvent extends WorldEvent {
    private final BlockPos pos;
    private final float temperature;
    private final float deltaTemperature;
    private final float deltaTime;
    private float heatLoss = 0.1F;
    private final boolean reactor;

    public ThermalEvent(IBlockAccess world, BlockPos pos, float temperature, float deltaTemperature, float deltaTime, boolean reactor) {
        super((World) world);

        this.pos = pos;
        this.temperature = temperature;
        this.deltaTemperature = deltaTemperature;
        this.deltaTime = deltaTime;
        this.reactor = reactor;
    }

    public BlockPos getPos() {
        return pos;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getDeltaTemperature() {
        return deltaTemperature;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public float getHeatLoss() {
        return heatLoss;
    }

    public void setHeatLoss(float heatLoss) {
        this.heatLoss = heatLoss;
    }

    public boolean isReactor() {
        return reactor;
    }

    public static class ThermalUpdateEvent extends ThermalEvent {
        public ThermalUpdateEvent(IBlockAccess world, BlockPos pos, float temperature, float deltaTemperature, float deltaTime, boolean reactor) {
            super(world, pos, temperature, deltaTemperature, deltaTime, reactor);
        }
    }
}
