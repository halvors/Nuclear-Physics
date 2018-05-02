package org.halvors.nuclearphysics.common.system.event;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.halvors.nuclearphysics.common.system.BaseMap;

import java.util.HashMap;

@EventBusSubscriber
public class WorldEventHandler {
    /** Temperature map, saved to world and updated over time */
    private static final HashMap<IBlockAccess, BaseMap> temperatureMap = new HashMap<>();

    /**
     * Gets the map of radioactive material per positon. Is used
     * to calculate exposure and other effects.
     *
     * @param world  - world of the map to get
     * @param init - true to generate the map
     * @return map, or null if it was never created
     */
    public static BaseMap getTemperatureMap(final IBlockAccess world, final boolean init) {
        BaseMap map = temperatureMap.get(world);

        if (map == null && init) {
            map = new BaseMap(world);
            temperatureMap.put(world, map);
        }

        return map;
    }

    @SubscribeEvent
    public static void onWorldUnloadEvent(final WorldEvent.Unload event) {
        final BaseMap temperatureMap = getTemperatureMap(event.getWorld(), false);

        if (temperatureMap != null) {
            temperatureMap.clear();
        }
    }

    @SubscribeEvent
    public static void onChunkUnloadEvent(final ChunkEvent.Unload event) { // Only called if chunk unloads separate from world unload
        final BaseMap temperatureMap = getTemperatureMap(event.getWorld(), false);

        if (temperatureMap != null) {
            temperatureMap.remove(event.getChunk());
        }
    }

    @SubscribeEvent
    public static void onChunkDataLoadEvent(final ChunkDataEvent.Load event) { // Called before chunk load event
        final NBTTagCompound tag = event.getData();

        if (tag != null && tag.hasKey(BaseMap.NBT_CHUNK_DATA)) {
            final BaseMap temperatureMap = getTemperatureMap(event.getWorld(), true);

            if (temperatureMap != null) {
                temperatureMap.readChunkFromNBT(event.getChunk(), event.getData());
            }
        }
    }

    @SubscribeEvent
    public static void onChunkDataSaveEvent(final ChunkDataEvent.Save event) { // Called on world save
        final BaseMap temperatureMap = getTemperatureMap(event.getWorld(), false);

        if (temperatureMap != null) {
            temperatureMap.writeChunkFromNBT(event.getChunk(), event.getData());
        }
    }
}
