package org.halvors.nuclearphysics.common.storage.nbt.chunk;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.Reference;

import java.util.HashMap;

public class ChunkDataMap {
    public static final String NBT_CHUNK_DATA_MAP = Reference.ID;

    private final HashMap<Long, ChunkData> loadedChunks = new HashMap<>();
    private final IBlockAccess world;

    public ChunkDataMap(final IBlockAccess world) {
        this.world = world;
    }

    public int getData(BlockPos pos) {
        final ChunkData chunk = getChunkData(pos, false);

        if (chunk != null) {
            return chunk.getValue(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
        }

        return 0;
    }

    public boolean setData(BlockPos pos, int value) {
        final ChunkData chunkData = getChunkData(pos, value > 0);

        if (chunkData != null) {
            /*
            int prev_value = getData(x, y, z);

            RadiationMapEvent.UpdateRadiationMaterial event = new RadiationMapEvent.UpdateRadiationMaterial(this, x, y, z, prev_value, amount);

            if (MinecraftForge.EVENT_BUS.post(event)) {
                return false;
            }

            amount = event.new_value;
            */

            // Set value
            boolean hasChanged = chunkData.setValue(pos.getX() & 15, pos.getY(), pos.getZ() & 15, value);

            // If changed mark chunk so it saves
            if (hasChanged && world instanceof World) {
                ((World) world).getChunkFromChunkCoords(pos.getX(), pos.getZ()).setChunkModified();
            }

            return hasChanged;
        }

        return true;
    }

    public long getIndex(final int x, final int z) {
        return ChunkPos.asLong(x, z);
    }

    public void clear() {
        loadedChunks.clear();
    }

    public void remove(final ChunkPos pos) {
        final long index = getIndex(pos.chunkXPos, pos.chunkZPos);

        if (loadedChunks.containsKey(index)) {
            final ChunkData chunk = loadedChunks.get(index);

            if (chunk != null) {
                //MinecraftForge.EVENT_BUS.post(new MapSystemEvent.RemoveChunk(this, chunk));
            }

            loadedChunks.remove(index);
        }
    }

    public void readFromNBT(final ChunkPos pos, final NBTTagCompound tag) {
        final long index = getIndex(pos.chunkXPos, pos.chunkZPos);

        // Get chunk
        ChunkData chunkData = null;

        if (loadedChunks.containsKey(index)) {
            chunkData = loadedChunks.get(index);
        }

        // Init chunk data if missing
        if (chunkData == null) {
            chunkData = new ChunkData(world, pos);
            loadedChunks.put(index, chunkData);
        }

        chunkData.readFromNBT(tag.getCompoundTag(NBT_CHUNK_DATA_MAP));

        /*
        // Queue to be scanned to update exposure map
        if (radiationChunk != null) {
            MinecraftForge.EVENT_BUS.post(new MapSystemEvent.AddChunk(this, radiationChunk));
        }
        */
    }

    public NBTTagCompound writeToNBT(final ChunkPos pos, final NBTTagCompound tag) {
        final long index = getIndex(pos.chunkXPos, pos.chunkZPos);

        if (loadedChunks.containsKey(index)) {
            final ChunkData chunk = loadedChunks.get(index);

            if (chunk != null) {
                final NBTTagCompound subTag = new NBTTagCompound();
                chunk.writeToNBT(subTag);

                if (!subTag.hasNoTags()) {
                    tag.setTag(NBT_CHUNK_DATA_MAP, subTag);
                }
            }
        }

        return tag;
    }

    public ChunkData getChunkData(int x, int z, boolean init) {
        final long index = getIndex(x, z);
        ChunkData chunk = loadedChunks.get(index);

        if (chunk == null && init) {
            chunk = new ChunkData(world, new ChunkPos(x, z));
            loadedChunks.put(index, chunk);
        }

        return chunk;
    }

    public ChunkData getChunkData(BlockPos pos, boolean init) {
        return getChunkData(pos.getX() >> 4, pos.getZ() >> 4, init);
    }
}