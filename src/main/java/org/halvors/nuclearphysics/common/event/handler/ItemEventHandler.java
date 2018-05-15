package org.halvors.nuclearphysics.common.event.handler;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.effect.explosion.AntimatterExplosion;
import org.halvors.nuclearphysics.common.init.ModItems;

import java.util.HashMap;
import java.util.Map;

public class ItemEventHandler {
    private static final Map<Block, ItemBucket> bucketMap = new HashMap<>();

    public static void registerBucket(Fluid fluid, ItemBucket item) {
        Block block = fluid.getBlock();

        if (block instanceof IFluidBlock) {
            bucketMap.put(block, item);
        }
    }

    @SubscribeEvent
    public void onItemExpireEvent(final ItemExpireEvent event) {
        if (General.enableAntimatterPower) {
            final EntityItem entityItem = event.entityItem;

            if (entityItem != null) {
                final ItemStack itemStack = entityItem.getEntityItem();

                if (itemStack.getItem() == ModItems.itemAntimatterCell) {
                    final AntimatterExplosion explosion = new AntimatterExplosion(entityItem.worldObj, entityItem, new BlockPos(entityItem), 4, itemStack.getMetadata());
                    explosion.explode();
                }
            }
        }
    }

    @SubscribeEvent
    public void onFillBucketEvent(FillBucketEvent event) {
        BlockPos pos = new BlockPos(event.target);
        World world = event.world;
        Block block = pos.getBlock(world);
        ItemBucket itemBucket = bucketMap.get(block);

        if (itemBucket != null) {
            pos.setBlock(Blocks.air, world);

            event.result = new ItemStack(itemBucket);
            event.setResult(Result.ALLOW);
        }
    }
}
