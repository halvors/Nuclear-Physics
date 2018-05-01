package org.halvors.nuclearphysics.common.event.handler;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.effect.explosion.AntimatterExplosion;
import org.halvors.nuclearphysics.common.init.ModItems;

public class ItemEventHandler {
    @SubscribeEvent
    public void onItemExpireEvent(final ItemExpireEvent event) {
        if (General.enableAntimatterPower) {
            final EntityItem entityItem = event.getEntityItem();

            if (entityItem != null) {
                final ItemStack itemStack = entityItem.getEntityItem();

                if (itemStack.getItem() == ModItems.itemAntimatterCell) {
                    final AntimatterExplosion explosion = new AntimatterExplosion(entityItem.getEntityWorld(), entityItem, entityItem.getPosition(), 4, itemStack.getMetadata());
                    explosion.explode();
                }
            }
        }
    }
}
