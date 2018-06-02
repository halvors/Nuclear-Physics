package org.halvors.nuclearphysics.common.storage.nbt.data;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ChunkDataEvent extends Event {
    private final IBlockAccess world;
    private final ChunkPos pos;

    public ChunkDataEvent(final IBlockAccess world, final ChunkPos pos) {
        this.world = world;
        this.pos = pos;
    }

    public IBlockAccess getWorld() {
        return world;
    }

    public ChunkPos getPos() {
        return pos;
    }

    public static class Add extends ChunkDataEvent {
        public final ChunkData chunkData;

        public Add(final IBlockAccess world, final ChunkPos pos, final ChunkData chunkData) {
            super(world, pos);

            this.chunkData = chunkData;
        }
    }

    @Cancelable
    public static class Update extends ChunkDataEvent {
        private int value;

        public Update(final IBlockAccess world, final ChunkPos pos, final int value) {
            super(world, pos);

            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public static class Remove extends ChunkDataEvent {
        public final ChunkData chunkData;

        public Remove(final IBlockAccess world, final ChunkPos pos, final ChunkData chunkData) {
            super(world, pos);

            this.chunkData = chunkData;
        }
    }
}
