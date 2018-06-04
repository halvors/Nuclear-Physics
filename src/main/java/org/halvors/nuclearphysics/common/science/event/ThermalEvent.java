package org.halvors.nuclearphysics.common.science.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.common.event.WorldEventBase;

public class ThermalEvent extends WorldEventBase {
    private final float temperature;
    private final float deltaTemperature;
    private final float deltaTime;
    private final boolean reactor;
    private float heatLoss = 0.1F;

    public ThermalEvent(final IBlockAccess world, final BlockPos pos, final float temperature, final float deltaTemperature, final float deltaTime, final boolean reactor) {
        super(world, pos);

        this.temperature = temperature;
        this.deltaTemperature = deltaTemperature;
        this.deltaTime = deltaTime;
        this.reactor = reactor;
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

    public boolean isReactor() {
        return reactor;
    }

    public float getHeatLoss() {
        return heatLoss;
    }

    public void setHeatLoss(final float heatLoss) {
        this.heatLoss = heatLoss;
}

    public static class ThermalUpdateEvent extends ThermalEvent {
        public ThermalUpdateEvent(final IBlockAccess world, final BlockPos pos, final float temperature, final float deltaTemperature, final float deltaTime, final boolean reactor) {
            super(world, pos, temperature, deltaTemperature, deltaTime, reactor);
        }
    }
}