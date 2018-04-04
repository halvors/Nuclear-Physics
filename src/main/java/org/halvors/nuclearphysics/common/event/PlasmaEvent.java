package org.halvors.nuclearphysics.common.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class PlasmaEvent extends WorldEventBase {
    private final int temperature;

    public PlasmaEvent(IBlockAccess world, BlockPos pos, int temperature) {
        super(world, pos);

        this.temperature = temperature;
    }

    public int getTemperature() {
        return temperature;
    }

    public static class PlasmaSpawnEvent extends PlasmaEvent {
        public PlasmaSpawnEvent(IBlockAccess world, BlockPos pos, int temperature) {
            super(world, pos, temperature);
        }
    }
}
