package org.halvors.nuclearphysics.common.event;

import net.minecraft.world.IBlockAccess;

public class PlasmaEvent extends WorldEventBase {
    private final int temperature;

    public PlasmaEvent(IBlockAccess world, int x, int y, int z, int temperature) {
        super(world, x, y, z);

        this.temperature = temperature;
    }

    public int getTemperature() {
        return temperature;
    }

    public static class PlasmaSpawnEvent extends PlasmaEvent {
        public PlasmaSpawnEvent(IBlockAccess world, int x, int y, int z, int temperature) {
            super(world, x, y, z, temperature);
        }
    }
}
