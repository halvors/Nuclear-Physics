package org.halvors.nuclearphysics.common.storage.nbt.thread;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class ChunkDataChange {
    public final IBlockAccess world;
    public final BlockPos pos;
    public final int old_value;
    public final int new_value;

    public ChunkDataChange(IBlockAccess world, BlockPos pos, int old_value, int new_value) {
        this.world = world;
        this.pos = pos;
        this.old_value = old_value;
        this.new_value = new_value;
    }
}

