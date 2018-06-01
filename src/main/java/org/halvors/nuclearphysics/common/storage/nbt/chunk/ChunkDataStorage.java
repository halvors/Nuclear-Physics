package org.halvors.nuclearphysics.common.storage.nbt.chunk;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.common.NuclearPhysics;

import java.util.HashMap;

public class ChunkDataStorage {
    protected final HashMap<IBlockAccess, ChunkDataMap> worldToChunkDataMap = new HashMap<>();
    protected final String nbtKey; // NBT key for this chunk data storage.

    public ChunkDataStorage(final String nbtKey) {
        this.nbtKey = nbtKey;
    }

    public ChunkDataMap getMap(final IBlockAccess world, final boolean init) {
        ChunkDataMap map = worldToChunkDataMap.get(world);

        if (map == null && init) {
            map = new ChunkDataMap(world);
            worldToChunkDataMap.put(world, map);
        }

        return map;
    }

    public int getData(final IBlockAccess world, final BlockPos pos) {
        final ChunkDataMap map = getMap(world, false);

        if (map != null) {
            map.getData(pos);
        }

        return 0;
    }

    public void setData(final IBlockAccess world, final BlockPos pos, int value) {
        final ChunkDataMap map = getMap(world, false);

        if (map != null && value > 0) {
            map.setData(pos, value);
        }
    }

    public void readFromNBT(final IBlockAccess world, final ChunkPos pos, final NBTTagCompound tag) {
        if (tag != null && nbtKey != null && tag.hasKey(nbtKey)) {
            final ChunkDataMap map = getMap(world, true);

            if (map != null) {
                final NBTTagCompound dataTag = tag.getCompoundTag(nbtKey);

                if (!dataTag.hasNoTags()) {
                    map.readFromNBT(pos, dataTag);
                }
            }
        }
    }

    public NBTTagCompound writeToNBT(final IBlockAccess world, final ChunkPos pos, final NBTTagCompound tag) {
        final ChunkDataMap map = getMap(world, false);

        if (map != null && nbtKey != null) {
            final NBTTagCompound dataTag = new NBTTagCompound();
            map.writeToNBT(pos, dataTag);

            if (!dataTag.hasNoTags()) {
                tag.setTag(nbtKey, dataTag);
            } else {
                NuclearPhysics.getLogger().info("Tag is empty....");
            }
        }

        return tag;
    }
}
