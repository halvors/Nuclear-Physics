package org.halvors.nuclearphysics.common.storage.nbt.chunk;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

public class ChunkStorage {
    protected final HashMap<IBlockAccess, ChunkDataMap> worldToChunkDataMap = new HashMap<>(); // Holding chunk data at run time, saved to world.
    protected final String nbtKey; // NBT key for this storage.

    public ChunkStorage(final String nbtKey) {
        this.nbtKey = nbtKey;
    }

    /**
     * Gets the map of radioactive material per positon. Is used
     * to calculate exposure and other effects.
     *
     * @param world  - world of the map to get
     * @param init - true to generate the map
     * @return map, or null if it was never created
     */
    public ChunkDataMap getMap(final IBlockAccess world, final boolean init) {
        ChunkDataMap map = worldToChunkDataMap.get(world);

        if (map == null && init) {
            map = new ChunkDataMap(world);
            worldToChunkDataMap.put(world, map);
        }
        return map;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void onChunkLoadData(final IBlockAccess world, final ChunkPos chunkPos, final NBTTagCompound save) { //Called before chunk load event
        if (save != null && nbtKey != null && save.hasKey(nbtKey)) {
            ChunkDataMap map = getMap(world, true);

            if (map != null) {
                NBTTagCompound tag = save.getCompoundTag(nbtKey);

                if (!tag.hasNoTags()) {
                    map.readFromNBT(chunkPos, tag);
                }
            }
        }
    }

    public void onChunkSaveData(final IBlockAccess world, final ChunkPos chunkPos, NBTTagCompound save) { //Called on world save
        ChunkDataMap map = getMap(world, false);

        if (map != null && nbtKey != null) {
            NBTTagCompound tag = new NBTTagCompound();
            map.writeToNBT(chunkPos, tag);

            if (!tag.hasNoTags()) {
                save.setTag(nbtKey, tag);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SubscribeEvent
    public void onWorldUnloadEvent(final WorldEvent.Unload event) {
        final ChunkDataMap map = getMap(event.getWorld(), false);

        if (map != null) {
            map.clear();
        }
    }

    @SubscribeEvent
    public void onChunkUnloadEvent(final ChunkEvent.Unload event) { // Only called if chunk unloads separate from world unload
        final ChunkDataMap map = getMap(event.getWorld(), false);

        if (map != null){
            map.remove(event.getChunk().getChunkCoordIntPair());
        }
    }

    @SubscribeEvent
    public static void onChunkDataLoadEvent(final ChunkDataEvent.Load event) { // Called before chunk load event
        final NBTTagCompound tag = event.getData();

        if (tag != null && tag.hasKey(ChunkDataMap.NBT_CHUNK_DATA_MAP)) {
            final ChunkDataMap temperatureMap = ChunkStorage.getMap(event.getWorld(), true);

            if (temperatureMap != null) {
                temperatureMap.readFromNBT(event.getChunk().getChunkCoordIntPair(), event.getData());
            }
        }
    }

    @SubscribeEvent
    public static void onChunkDataSaveEvent(final ChunkDataEvent.Save event) { // Called on world save
        final ChunkDataMap temperatureMap = ChunkStorage.getMap(event.getWorld(), false);

        if (temperatureMap != null) {
            temperatureMap.writeToNBT(event.getChunk().getChunkCoordIntPair(), event.getData());
        }
    }
}
