package org.halvors.nuclearphysics.common.system.chunk;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.halvors.nuclearphysics.common.Reference;

import java.util.HashMap;

public class ChunkDataMap {
    public static final String NBT_CHUNK_DATA_MAP = Reference.ID;

    private final HashMap<Long, ChunkData> loadedChunks = new HashMap<>();
    private final IBlockAccess world;
    //private final boolean isMaterialMap;

    public ChunkDataMap(final IBlockAccess world) {
        this.world = world;
        //this.isMaterialMap = isMaterialMap;
    }

    public int getData(BlockPos pos) {
        final ChunkData chunk = getChunkFromPosition(pos, false);

        if (chunk != null) {
            return chunk.getValue(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
        }

        return 0;
    }

    public boolean setData(BlockPos pos, int value) {
        final ChunkData chunk = getChunkFromPosition(pos, value > 0);

        if (chunk != null) {
            // Fire change event for modification and to trigger exposure map update
            /*
            if (isMaterialMap) {
                int prev_value = getData(x, y, z);

                RadiationMapEvent.UpdateRadiationMaterial event = new RadiationMapEvent.UpdateRadiationMaterial(this, x, y, z, prev_value, amount);

                if (MinecraftForge.EVENT_BUS.post(event)) {
                    return false;
                }

                amount = event.new_value;
            }
            */

            // set value
            boolean hasChanged = chunk.setValue(pos.getX() & 15, pos.getY(), pos.getZ() & 15, value);

            // if changed mark chunk so it saves
            if (hasChanged && world instanceof World) {
                ((World) world).getChunkFromChunkCoords(pos.getX(), pos.getZ()).setChunkModified();
            }

            return hasChanged;
        }

        return true;
    }

    public void remove(long index) {
        /*if (loadedChunks.containsKey(index)) {

            if (isMaterialMap) {
                ChunkData chunk = loadedChunks.get(index);

                if (chunk != null) {
                    //RadiationChunkHandler.THREAD_RAD_EXPOSURE.queueChunkForRemoval(chunk);
                }
            }
            */
            //TODO maybe fire events?
            loadedChunks.remove(index);
        //}
    }

    public void remove(final Chunk chunk) {
        remove(ChunkPos.asLong(chunk.xPosition, chunk.zPosition));
    }

    public void clear() {
        loadedChunks.clear();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void readFromNBT(final ChunkPos pos, final NBTTagCompound data) {
        final long index = ChunkPos.asLong(pos.chunkXPos, pos.chunkZPos);

        // Get chunk
        ChunkData chunk = null;

        if (loadedChunks.containsKey(index)) {
            chunk = loadedChunks.get(index);
        }

        // Init chunk data if missing
        if (chunk == null) {
            chunk = new ChunkData(world, pos);
            loadedChunks.put(index, chunk);
        }

        // Load
        chunk.readFromNBT(data.getCompoundTag(NBT_CHUNK_DATA_MAP));

        /*
        // Queue to be scanned to update exposure map
        if (isMaterialMap) {
            //RadiationChunkHandler.THREAD_RAD_EXPOSURE.queueChunkForAddition(radiationChunk);
        }
        */
    }

    public NBTTagCompound writeToNBT(final ChunkPos pos, final NBTTagCompound tag) {
        final long index = ChunkPos.asLong(pos.chunkXPos, pos.chunkZPos);

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ChunkData getChunk(int x, int z, boolean init) {
        final long index = ChunkPos.asLong(x, z);
        ChunkData chunk = loadedChunks.get(index);

        if (chunk == null && init) {
            chunk = new ChunkData(world, new ChunkPos(x, z));
            loadedChunks.put(index, chunk);
        }

        return chunk;
    }

    public ChunkData getChunkFromPosition(BlockPos pos, boolean init) {
        return getChunk(pos.getX() >> 4, pos.getZ() >> 4, init);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static ChunkDataMap getMap(final HashMap<IBlockAccess, ChunkDataMap> queryMap, final IBlockAccess world, final boolean init) {
        ChunkDataMap map = queryMap.get(world);

        if (map == null && init) {
            map = new ChunkDataMap(world);
            queryMap.put(world, map);
        }

        return map;
    }
}