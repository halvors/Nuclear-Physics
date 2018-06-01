package org.halvors.nuclearphysics.common.storage.nbt.event.handler;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.halvors.nuclearphysics.common.science.ThermalDataStorage;
import org.halvors.nuclearphysics.common.storage.nbt.chunk.WorldData;

@EventBusSubscriber
public class WorldEventHandler {
    @SubscribeEvent
    public static void onWorldUnloadEvent(final WorldEvent.Unload event) {
        final WorldData worldData = ThermalDataStorage.getInstance().getData(event.getWorld(), false);

        if (worldData != null) {
            worldData.clear();
        }
    }

    @SubscribeEvent
    public static void onChunkUnloadEvent(final ChunkEvent.Unload event) {
        final IBlockAccess world = event.getWorld();
        final ChunkPos pos = event.getChunk().getChunkCoordIntPair();
        final WorldData worldData = ThermalDataStorage.getInstance().getData(world, false);

        if (worldData != null) {
            worldData.remove(pos);
        }
}

    @SubscribeEvent
    public static void onChunkDataLoadEvent(final ChunkDataEvent.Load event) {
        final IBlockAccess world = event.getWorld();
        final ChunkPos pos = event.getChunk().getChunkCoordIntPair();
        final NBTTagCompound tag = event.getData();

        ThermalDataStorage.getInstance().readFromNBT(world, pos, tag);
    }

    @SubscribeEvent
    public static void onChunkDataSaveEvent(final ChunkDataEvent.Save event) {
        final IBlockAccess world = event.getWorld();
        final ChunkPos pos = event.getChunk().getChunkCoordIntPair();
        final NBTTagCompound tag = event.getData();

        ThermalDataStorage.getInstance().writeToNBT(world, pos, tag);
    }
}
