package org.halvors.nuclearphysics.common.event.handler;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.halvors.nuclearphysics.common.effect.explosion.AntimatterExplosion;
import org.halvors.nuclearphysics.common.init.ModItems;

public class ItemEventHandler {
    @SubscribeEvent
    public void onItemExpireEvent(ItemExpireEvent event) {
        //if (!FlagRegistry.getModFlag(FlagRegistry.DEFAULT_NAME).containsValue(event.entityItem.worldObj, Atomic.BAN_ANTIMATTER_POWER, "true", new Vector3(event.entityItem))) {

        EntityItem entityItem = event.getEntityItem();

        if (entityItem != null) {
            ItemStack itemStack = entityItem.getEntityItem();

            if (itemStack.getItem() == ModItems.itemAntimatterCell) {
                AntimatterExplosion explosion = new AntimatterExplosion(entityItem.getEntityWorld(), entityItem, entityItem.getPosition(), 4, itemStack.getMetadata());
                explosion.explode();

            }
        }

        //}
    }
}
