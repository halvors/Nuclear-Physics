package org.halvors.nuclearphysics.common.system.thread;

import net.minecraft.util.math.BlockPos;
import org.halvors.nuclearphysics.common.system.chunk.ChunkDataMap;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ChunkThread extends Thread {
    public ConcurrentLinkedQueue<ChunkDataChange> changeQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void run() {

    }

    protected void setValue(ChunkDataMap map, BlockPos pos, int value) {

    }
}
