package org.halvors.nuclearphysics.common.storage.nbt.chunk;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.common.Reference;

import java.util.HashMap;

public class ChunkDataStorage {

    protected final HashMap<IBlockAccess, WorldData> worldToData = new HashMap<>();
    protected final String nbtKey; // NBT key for this chunk data storage.

    public ChunkDataStorage(final String nbtKey) {
        this.nbtKey = nbtKey;
    }

    public WorldData getData(final IBlockAccess world, final boolean init) {
        WorldData worldData = worldToData.get(world);

        if (worldData == null && init) {
            worldData = new WorldData(world);
            worldToData.put(world, worldData);
        }

        return worldData;
    }

    public int getValue(final IBlockAccess world, final BlockPos pos) {
        final WorldData worldData = getData(world, false);

        if (worldData != null) {
            return worldData.getValue(pos);
        }

        return 0;
    }

    public void setValue(final IBlockAccess world, final BlockPos pos, int value) {
        final WorldData worldData = getData(world, value > 0);

        if (worldData != null) {
            worldData.setValue(pos, value);
        }
    }

    public void readFromNBT(final IBlockAccess world, final ChunkPos pos, final NBTTagCompound tag) {
        if (tag != null && nbtKey != null && tag.hasKey(nbtKey)) {
            final WorldData worldData = getData(world, true);

            if (worldData != null) {
                final NBTTagCompound dataTag = tag.getCompoundTag(nbtKey);

                if (!dataTag.hasNoTags()) {
                    worldData.readFromNBT(pos, dataTag);
                }
            }
        }
    }

    public NBTTagCompound writeToNBT(final IBlockAccess world, final ChunkPos pos, final NBTTagCompound tag) {
        final WorldData worldData = getData(world, false);

        if (worldData != null && nbtKey != null) {
            final NBTTagCompound dataTag = new NBTTagCompound();
            worldData.writeToNBT(pos, dataTag);

            if (!dataTag.hasNoTags()) {
                tag.setTag(nbtKey, dataTag);
            }
        }

        return tag;
    }
}
