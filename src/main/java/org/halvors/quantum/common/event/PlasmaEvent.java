package org.halvors.quantum.common.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class PlasmaEvent extends WorldEvent {
    public PlasmaEvent(World world) {
        super(world);
    }

    public static class PlasmaSpawnEvent extends PlasmaEvent {
        private final BlockPos pos;
        private final int temperature;

        public PlasmaSpawnEvent(World world, BlockPos pos, int temperature) {
            super(world);

            this.pos = pos;
            this.temperature = temperature;
        }

        public BlockPos getPos() {
            return pos;
        }

        public int getTemperature() {
            return temperature;
        }
    }
}
