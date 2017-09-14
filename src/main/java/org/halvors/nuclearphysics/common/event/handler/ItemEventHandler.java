package org.halvors.nuclearphysics.common.event.handler;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.effect.explosion.AntimatterExplosion;
import org.halvors.nuclearphysics.common.init.ModItems;
import org.halvors.nuclearphysics.common.init.ModSoundEvents;

public class ItemEventHandler {
    @SubscribeEvent
    public void onItemExpireEvent(ItemExpireEvent event) {
        EntityItem entityItem = event.getEntityItem();

        if (entityItem != null) {
            ItemStack itemStack = entityItem.getEntityItem();

            if (itemStack.getItem() == ModItems.itemAntimatterCell) {
                World world = entityItem.getEntityWorld();
                world.playSound(entityItem.posX, entityItem.posY, entityItem.posZ, ModSoundEvents.ANTIMATTER, SoundCategory.NEUTRAL, 3, 1 - world.rand.nextFloat() * 0.3F, false);

                //if (!FlagRegistry.getModFlag(FlagRegistry.DEFAULT_NAME).containsValue(event.entityItem.worldObj, Atomic.BAN_ANTIMATTER_POWER, "true", new Vector3(event.entityItem))) {
                    AntimatterExplosion explosion = new AntimatterExplosion(world, entityItem, entityItem.getPosition(), 4, itemStack.getMetadata());
                    explosion.explode();

                    NuclearPhysics.getLogger().info("Antimatter cell detonated at: " + entityItem.posX + ", " + entityItem.posY + ", " + entityItem.posZ);
                //}
            }
        }
    }
}
