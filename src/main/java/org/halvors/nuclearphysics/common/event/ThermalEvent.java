package org.halvors.nuclearphysics.common.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class ThermalEvent extends WorldEventBase {
    private final double temperature;
    private final double deltaTemperature;
    private final double deltaTime;
    private final boolean reactor;
    private double heatLoss = 0.1;

    public ThermalEvent(final IWorld world, final BlockPos pos, final double temperature, final double deltaTemperature, final double deltaTime, final boolean reactor) {
        super(world, pos);

        this.temperature = temperature;
        this.deltaTemperature = deltaTemperature;
        this.deltaTime = deltaTime;
        this.reactor = reactor;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getDeltaTemperature() {
        return deltaTemperature;
    }

    public double getDeltaTime() {
        return deltaTime;
    }

    public double getHeatLoss() {
        return heatLoss;
    }

    public void setHeatLoss(final double heatLoss) {
        this.heatLoss = heatLoss;
    }

    public boolean isReactor() {
        return reactor;
    }

    public static class ThermalUpdateEvent extends ThermalEvent {
        public ThermalUpdateEvent(final IWorld world, final BlockPos pos, final double temperature, final double deltaTemperature, final double deltaTime, final boolean reactor) {
            super(world, pos, temperature, deltaTemperature, deltaTime, reactor);
        }
    }
}
