package org.halvors.nuclearphysics.common.system;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.HashMap;

/**
 * Stores a collection of chunks holding radiation data
 * <p>
 * Radiation is not "rad or rem" value, it is how much radioactive material is present at the location. This
 * is used to calculate the rad value that an entity will be exposed to or can be released into the air.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/24/2018.
 */
public class BaseMap {
    public static final String NBT_CHUNK_DATA = "temperatureData";

    private final HashMap<Long, BaseChunk> loadedChunks = new HashMap<>();
    private final IBlockAccess world;
    //private final boolean isMaterialMap;

    public BaseMap(final IBlockAccess world) {
        this.world = world;
        //this.isMaterialMap = isMaterialMap;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getData(int x, int y, int z) {
        BaseChunk chunk = getChunkFromPosition(x, z, false);

        if (chunk != null) {
            return chunk.getValue(x & 15, y, z & 15);
        }

        return 0;
    }

    public int getData(BlockPos pos) {
        return getData(pos.getX(), pos.getY(), pos.getZ());
    }

    public boolean setData(int x, int y, int z, int value) {
        final BaseChunk chunk = getChunkFromPosition(x, z, value > 0);

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
            boolean hasChanged = chunk.setValue(x & 15, y, z & 15, value);

            // if changed mark chunk so it saves
            if (hasChanged && world != null) {
                ((World) world).getChunkFromChunkCoords(x, z).setChunkModified();
            }

            return hasChanged;
        }
        return true;
    }

    public boolean setData(BlockPos pos, int value) {
        return setData(pos.getX(), pos.getY(), pos.getZ(), value);
    }

    public void remove(long index) {
        /*if (loadedChunks.containsKey(index)) {

            if (isMaterialMap) {
                BaseChunk chunk = loadedChunks.get(index);

                if (chunk != null) {
                    //RadiationSystem.THREAD_RAD_EXPOSURE.queueChunkForRemoval(chunk);
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

    public void readChunkFromNBT(final Chunk chunk, final NBTTagCompound data) {
        final long index = ChunkPos.asLong(chunk.xPosition, chunk.zPosition);

        // Get chunk
        BaseChunk baseChunk = null;

        if (loadedChunks.containsKey(index)) {
            baseChunk = loadedChunks.get(index);
        }

        // Init chunk if missing
        if (baseChunk == null) {
            baseChunk = new BaseChunk(chunk);
            loadedChunks.put(index, baseChunk);
        }

        // Load
        baseChunk.readFromNBT(data.getCompoundTag(NBT_CHUNK_DATA));

        /*
        // Queue to be scanned to update exposure map
        if (isMaterialMap) {
            //RadiationSystem.THREAD_RAD_EXPOSURE.queueChunkForAddition(radiationChunk);
        }
        */
    }

    public void writeChunkFromNBT(final Chunk chunk, final NBTTagCompound data) {
        final long index = ChunkPos.asLong(chunk.xPosition, chunk.zPosition);

        if (loadedChunks.containsKey(index)) {
            final BaseChunk baseChunk = loadedChunks.get(index);

            if (baseChunk != null) {
                final NBTTagCompound tag = new NBTTagCompound();
                baseChunk.writeToNBT(tag);

                if (!tag.hasNoTags()) {
                    data.setTag(NBT_CHUNK_DATA, tag);
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public BaseChunk getChunkFromPosition(int x, int z, boolean init) {
        return getChunk(x >> 4, z >> 4, init);
    }

    public BaseChunk getChunk(int x, int z, boolean init) {
        long index = ChunkPos.asLong(x, z);
        BaseChunk chunk = loadedChunks.get(index);

        if (chunk == null && init) {
            chunk = new BaseChunk(world, new ChunkPos(x, z));
            loadedChunks.put(index, chunk);
        }

        return chunk;
    }
}