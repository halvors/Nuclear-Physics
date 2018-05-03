package org.halvors.nuclearphysics.common.system.event;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.halvors.nuclearphysics.common.system.data.DataMap;

import java.util.HashMap;

@EventBusSubscriber
public class WorldEventHandler {
    /** Temperature map, saved to world and updated over time. */
    private static final HashMap<IBlockAccess, DataMap> temperatureMap = new HashMap<>();

    /** Radiation map, saved to world and updated over time. */
    private static final HashMap<IBlockAccess, DataMap> radiationMap = new HashMap<>();

    public static DataMap getMap(final HashMap<IBlockAccess, DataMap> queryMap, final IBlockAccess world, final boolean init) {
        DataMap map = queryMap.get(world);

        if (map == null && init) {
            map = new DataMap(world);
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
    public static DataMap getTemperatureMap(final IBlockAccess world, final boolean init) {
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
    public static DataMap getRadiationMap(final IBlockAccess world, final boolean init) {
        return getMap(radiationMap, world, init);
    }

    @SubscribeEvent
    public static void onWorldUnloadEvent(final WorldEvent.Unload event) {
        final DataMap temperatureMap = getTemperatureMap(event.getWorld(), false);
        //final DataMap radiationMap = getRadiationMap(event.getWorld(), false);

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
        final DataMap temperatureMap = getTemperatureMap(event.getWorld(), false);
        //final DataMap radiationMap = getRadiationMap(event.getWorld(), false);

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

        if (tag != null && tag.hasKey(DataMap.NBT_CHUNK_DATA)) {
            final DataMap temperatureMap = getTemperatureMap(event.getWorld(), true);
            //final DataMap radiationMap = getRadiationMap(event.getWorld(), true);

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
        final DataMap temperatureMap = getTemperatureMap(event.getWorld(), false);
        //final DataMap radiationMap = getRadiationMap(event.getWorld(), false);

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
