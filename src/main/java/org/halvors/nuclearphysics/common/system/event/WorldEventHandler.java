package org.halvors.nuclearphysics.common.system.event;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.halvors.nuclearphysics.common.system.data.ChunkDataMap;

import java.util.HashMap;

@EventBusSubscriber
public class WorldEventHandler {
    /** Temperature map, saved to world and updated over time. */
    private static final HashMap<IBlockAccess, ChunkDataMap> temperatureMap = new HashMap<>();

    /** Radiation map, saved to world and updated over time. */
    private static final HashMap<IBlockAccess, ChunkDataMap> radiationMap = new HashMap<>();

    public static ChunkDataMap getMap(final HashMap<IBlockAccess, ChunkDataMap> queryMap, final IBlockAccess world, final boolean init) {
        ChunkDataMap map = queryMap.get(world);

        if (map == null && init) {
            map = new ChunkDataMap(world);
            queryMap.put(world, map);
        }

        return map;
    }

    /**
     * Gets the map of radioactive material per positon. Is used
     * to calculate exposure and other effects.
     *
     * @param world  - world of the map to get
     * @param init - true to generate the map
     * @return map, or null if it was never created
     */
    public static ChunkDataMap getTemperatureMap(final IBlockAccess world, final boolean init) {
        return getMap(temperatureMap, world, init);
    }

    /**
     * Gets the map of radioactive material per positon. Is used
     * to calculate exposure and other effects.
     *
     * @param world  - world of the map to get
     * @param init - true to generate the map
     * @return map, or null if it was never created
     */
    public static ChunkDataMap getRadiationMap(final IBlockAccess world, final boolean init) {
        return getMap(radiationMap, world, init);
    }

    @SubscribeEvent
    public static void onWorldUnloadEvent(final WorldEvent.Unload event) {
        final ChunkDataMap temperatureMap = getTemperatureMap(event.getWorld(), false);
        //final ChunkDataMap radiationMap = getRadiationMap(event.getWorld(), false);

        if (temperatureMap != null) {
            temperatureMap.clear();
        }

        /*
        if (radiationMap != null) {
            radiationMap.clear();
        }
        */
    }

    @SubscribeEvent
    public static void onChunkUnloadEvent(final ChunkEvent.Unload event) { // Only called if chunk unloads separate from world unload
        final ChunkDataMap temperatureMap = getTemperatureMap(event.getWorld(), false);
        //final ChunkDataMap radiationMap = getRadiationMap(event.getWorld(), false);

        if (temperatureMap != null) {
            temperatureMap.remove(event.getChunk());
        }

        /*
        if (radiationMap != null) {
            radiationMap.remove(event.getChunk());
        }
        */
    }

    @SubscribeEvent
    public static void onChunkDataLoadEvent(final ChunkDataEvent.Load event) { // Called before chunk load event
        final NBTTagCompound tag = event.getData();

        if (tag != null && tag.hasKey(ChunkDataMap.NBT_CHUNK_DATA)) {
            final ChunkDataMap temperatureMap = getTemperatureMap(event.getWorld(), true);
            //final ChunkDataMap radiationMap = getRadiationMap(event.getWorld(), true);

            if (temperatureMap != null) {
                temperatureMap.readChunkFromNBT(event.getChunk(), event.getData());
            }

            /*
            if (radiationMap != null) {
                radiationMap.readChunkFromNBT(event.getChunk(), event.getData());
            }
            */
        }
    }

    @SubscribeEvent
    public static void onChunkDataSaveEvent(final ChunkDataEvent.Save event) { // Called on world save
        final ChunkDataMap temperatureMap = getTemperatureMap(event.getWorld(), false);
        //final ChunkDataMap radiationMap = getRadiationMap(event.getWorld(), false);

        if (temperatureMap != null) {
            temperatureMap.writeChunkFromNBT(event.getChunk(), event.getData());
        }

        /*
        if (radiationMap != null) {
            radiationMap.writeChunkFromNBT(event.getChunk(), event.getData());
        }
        */
    }
}
