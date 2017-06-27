package org.halvors.quantum.common.event;

import cpw.mods.fml.common.eventhandler.Event;
import org.halvors.quantum.common.utility.transform.vector.VectorWorld;

public abstract class ThermalEvent extends Event {
    public final VectorWorld position;
    public final float temperature;
    public final float deltaTemperature;
    public final float deltaTime;
    public float heatLoss = 0.1F;
    public boolean isReactor = false;

    public ThermalEvent(VectorWorld position, float temperature, float deltaTemperature, float deltaTime, boolean isReactor) {
        this.position = position;
        this.temperature = temperature;
        this.deltaTemperature = deltaTemperature;
        this.deltaTime = deltaTime;
        this.isReactor = isReactor;
    }

    public static class ThermalUpdateEvent extends ThermalEvent {
        public ThermalUpdateEvent(VectorWorld position, float temperature, float deltaTemperature, float deltaTime, boolean isReactor) {
            super(position, temperature, deltaTemperature, deltaTime, isReactor);
        }
    }
}
