package org.halvors.nuclearphysics.common.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class PlasmaEvent extends WorldEvent {
    private final BlockPos pos;
    private final int temperature;

    public PlasmaEvent(IBlockAccess world, BlockPos pos, int temperature) {
        super((World) world);

        this.pos = pos;
        this.temperature = temperature;
    }

    public BlockPos getPos() {
        return pos;
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
