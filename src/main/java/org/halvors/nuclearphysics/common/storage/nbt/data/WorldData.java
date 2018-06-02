package org.halvors.nuclearphysics.common.storage.nbt.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;

import static org.halvors.nuclearphysics.common.storage.nbt.data.ChunkDataLayer.CHUNK_WIDTH;

public class WorldData {
    private final HashMap<Long, ChunkData> loadedChunks = new HashMap<>();
    private final World world;

    public WorldData(final World world) {
        this.world = world;
    }

    public int getValue(BlockPos pos) {
        final ChunkData chunkData = getChunkData(pos, false);

        if (chunkData != null) {
            return chunkData.getValue(pos.getX() & (CHUNK_WIDTH - 1), pos.getY(), pos.getZ() & (CHUNK_WIDTH - 1));
        }

        return 0;
    }

    public boolean setValue(BlockPos pos, int value) {
        final ChunkData chunkData = getChunkData(pos, value > 0);

        if (chunkData != null) {
            final Chunk chunk = world.getChunkFromBlockCoords(pos);
            final ChunkDataEvent.Update event = new ChunkDataEvent.Update(world, chunk.getChunkCoordIntPair(), value);
            MinecraftForge.EVENT_BUS.post(event);

            if (!event.isCanceled()) {
                value = event.getValue();
            }

            // Set value
            boolean changed = chunkData.setValue(pos.getX() & (CHUNK_WIDTH - 1), pos.getY(), pos.getZ() & (CHUNK_WIDTH - 1), value);

            // If changed mark data so it saves
            if (changed) {
                chunk.setChunkModified();
            }

            return changed;
        }

        return true;
    }

    public long getIndex(final int x, final int z) {
        return ChunkPos.asLong(x, z);
    }

    public long getIndex(final ChunkPos pos) {
        return ChunkPos.asLong(pos.chunkXPos, pos.chunkZPos);
    }

    public ChunkData getChunkData(int x, int z, boolean init) {
        final long index = getIndex(x, z);
        ChunkData chunkData = loadedChunks.get(index);

        if (chunkData == null && init) {
            chunkData = new ChunkData();
            loadedChunks.put(index, chunkData);
        }

        return chunkData;
    }

    public ChunkData getChunkData(BlockPos pos, boolean init) {
        return getChunkData(pos.getX() >> 4, pos.getZ() >> 4, init);
    }

    public void clear() {
        loadedChunks.clear();
    }

    public void remove(final ChunkPos pos) {
        final long index = getIndex(pos);

        if (loadedChunks.containsKey(index)) {
            final ChunkData chunkData = loadedChunks.get(index);

            if (chunkData != null) {
                MinecraftForge.EVENT_BUS.post(new ChunkDataEvent.Remove(world, pos, chunkData));
            }

            loadedChunks.remove(index);
        }
    }

    public void readFromNBT(final ChunkPos pos, final NBTTagCompound tag) {
        final long index = getIndex(pos);
        ChunkData chunkData = null;

        if (loadedChunks.containsKey(index)) {
            chunkData = loadedChunks.get(index);
        }

        // Init data data if missing
        if (chunkData == null) {
            chunkData = new ChunkData();
            loadedChunks.put(index, chunkData);
        }

        chunkData.readFromNBT(tag);
        MinecraftForge.EVENT_BUS.post(new ChunkDataEvent.Add(world, pos, chunkData));
    }

    public NBTTagCompound writeToNBT(final ChunkPos pos, final NBTTagCompound tag) {
        final long index = getIndex(pos);

        if (loadedChunks.containsKey(index)) {
            final ChunkData chunkData = loadedChunks.get(index);

            if (chunkData != null) {
                chunkData.writeToNBT(tag);
            }
        }

        return tag;
    }
}