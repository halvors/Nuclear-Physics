package org.halvors.nuclearphysics.common.event.handler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.halvors.nuclearphysics.api.tile.IElectromagnet;
import org.halvors.nuclearphysics.common.event.PlasmaEvent;

@EventBusSubscriber
public class PlasmaEventHandler {
    @SubscribeEvent
    public static void onPlasmaSpawnEvent(final PlasmaEvent.PlasmaSpawnEvent event) {
        final World world = event.getWorld();
        final BlockPos pos = event.getPos();
        final IBlockState state = world.getBlockState(pos);

        if (event.isCanceled()) {
            if (state == Blocks.BEDROCK.getDefaultState() ||
                    state == Blocks.IRON_BLOCK.getDefaultState()) {
                event.setCanceled(true);
            }

            final TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof IElectromagnet) {
                event.setCanceled(true);
            }
        }
    }
}
