package org.halvors.nuclearphysics.common.storage.nbt.event.handler;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.halvors.nuclearphysics.common.storage.nbt.chunk.ChunkStorage;
import org.halvors.nuclearphysics.common.storage.nbt.chunk.ChunkDataMap;

@EventBusSubscriber
public class WorldEventHandler {
    @SubscribeEvent
    public static void onWorldUnloadEvent(final WorldEvent.Unload event) {
        final ChunkDataMap temperatureMap = ChunkStorage.getMap(event.getWorld(), false);

        if (temperatureMap != null) {
            temperatureMap.clear();
        }
    }

    @SubscribeEvent
    public static void onChunkUnloadEvent(final ChunkEvent.Unload event) { // Only called if chunk unloads separate from world unload
        final ChunkDataMap temperatureMap = ChunkStorage.getMap(event.getWorld(), false);

        if (temperatureMap != null) {
            temperatureMap.remove(event.getChunk());
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
