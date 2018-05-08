package org.halvors.nuclearphysics.common.science.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.common.event.WorldEventBase;

public class TemperatureEvent extends WorldEventBase {
    protected final double temperature;

    public TemperatureEvent(final IBlockAccess world, final BlockPos pos, final double temperature) {
        super(world, pos);

        this.temperature = temperature;
    }

    public double getTemperature() {
        return temperature;
    }

    public static class UpdateEvent extends TemperatureEvent {
        private final double deltaTemperature;
        private final double deltaTime;
        private final boolean reactor;
        private double heatLoss = 0.1;

        public UpdateEvent(final IBlockAccess world, final BlockPos pos, final double temperature, final double deltaTemperature, final double deltaTime, final boolean reactor) {
            super(world, pos, temperature);

            this.deltaTemperature = deltaTemperature;
            this.deltaTime = deltaTime;
            this.reactor = reactor;
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
    }
}
