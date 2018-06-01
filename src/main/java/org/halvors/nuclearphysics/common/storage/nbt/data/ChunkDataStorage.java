package org.halvors.nuclearphysics.common.storage.nbt.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.Reference;

import java.util.HashMap;

public class ChunkDataStorage {
    private static final String NBT_MOD_ID = Reference.ID;

    private final HashMap<IBlockAccess, WorldData> worldToDataMap = new HashMap<>();
    private final String nbtKey;

    public ChunkDataStorage(final String nbtKey) {
        this.nbtKey = nbtKey;
    }

    public WorldData getData(final IBlockAccess world, final boolean init) {
        WorldData worldData = worldToDataMap.get(world);

        if (worldData == null && init) {
            worldData = new WorldData((World) world);
            worldToDataMap.put(world, worldData);
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
        final NBTTagCompound modTag = tag.getCompoundTag(NBT_MOD_ID);

        if (nbtKey != null && modTag.hasKey(nbtKey)) {
            final WorldData worldData = getData(world, true);

            if (worldData != null) {
                final NBTTagCompound dataTag = modTag.getCompoundTag(nbtKey);

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
                final NBTTagCompound modTag = new NBTTagCompound();
                modTag.setTag(nbtKey, dataTag);
                tag.setTag(NBT_MOD_ID, modTag);
            }
        }

        return tag;
    }
}
