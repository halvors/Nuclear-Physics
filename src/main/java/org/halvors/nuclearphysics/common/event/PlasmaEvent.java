package org.halvors.nuclearphysics.common.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class PlasmaEvent extends WorldEventBase {
    private int temperature;

    public PlasmaEvent(final IBlockAccess world, final BlockPos pos, final int temperature) {
        super(world, pos);

        this.temperature = temperature;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    @Cancelable
    public static class PlasmaSpawnEvent extends PlasmaEvent {
        public PlasmaSpawnEvent(final IBlockAccess world, final BlockPos pos, final int temperature) {
            super(world, pos, temperature);
        }
    }
}
