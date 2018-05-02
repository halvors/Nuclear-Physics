package org.halvors.nuclearphysics.common.event;

import net.minecraft.world.IBlockAccess;

public class ThermalEvent extends WorldEventBase {
    private final float temperature;
    private final float deltaTemperature;
    private final float deltaTime;
    private final boolean reactor;
    private float heatLoss = 0.1F;

    public ThermalEvent(final IBlockAccess world, final int x, final int y, final int z, final float temperature, final float deltaTemperature, final float deltaTime, final boolean reactor) {
        super(world, x, y, z);

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

    public float getHeatLoss() {
        return heatLoss;
    }

    public void setHeatLoss(final float heatLoss) {
        this.heatLoss = heatLoss;
    }

    public boolean isReactor() {
        return reactor;
    }

    public static class ThermalUpdateEvent extends ThermalEvent {
        public ThermalUpdateEvent(final IBlockAccess world, final int x, final int y, final int z, final float temperature, final float deltaTemperature, final float deltaTime, final boolean reactor) {
            super(world, x, y, z, temperature, deltaTemperature, deltaTime, reactor);
        }
    }
}
