package org.halvors.nuclearphysics.common.storage.nbt.event.handler;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.science.ThermalDataStorage;
import org.halvors.nuclearphysics.common.storage.nbt.chunk.ChunkDataMap;

@EventBusSubscriber
public class WorldEventHandler {
    @SubscribeEvent
    public static void onWorldUnloadEvent(final WorldEvent.Unload event) {
        final ChunkDataMap map = ThermalDataStorage.getInstance().getMap(event.getWorld(), false);

        if (map != null) {
            map.clear();
        }
    }

    @SubscribeEvent
    public static void onChunkUnloadEvent(final ChunkEvent.Unload event) {
        final IBlockAccess world = event.getWorld();
        final ChunkPos pos = event.getChunk().getChunkCoordIntPair();
        final ChunkDataMap map = ThermalDataStorage.getInstance().getMap(world, false);

        if (map != null) {
            map.remove(pos);
        }
    }

    @SubscribeEvent
    public static void onChunkDataLoadEvent(final ChunkDataEvent.Load event) {
        final IBlockAccess world = event.getWorld();
        final ChunkPos pos = event.getChunk().getChunkCoordIntPair();
        final NBTTagCompound tag = event.getData();

        ThermalDataStorage.getInstance().readFromNBT(world, pos, tag);
        NuclearPhysics.getLogger().info("Loading NBT data...");
    }

    @SubscribeEvent
    public static void onChunkDataSaveEvent(final ChunkDataEvent.Save event) {
        final IBlockAccess world = event.getWorld();
        final ChunkPos pos = event.getChunk().getChunkCoordIntPair();
        final NBTTagCompound tag = event.getData();

        ThermalDataStorage.getInstance().writeToNBT(world, pos, tag);
        NuclearPhysics.getLogger().info("Saving NBT data...");
    }
}
