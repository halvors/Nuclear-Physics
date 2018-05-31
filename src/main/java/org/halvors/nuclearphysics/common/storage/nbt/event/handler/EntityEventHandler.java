package org.halvors.nuclearphysics.common.storage.nbt.event.handler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class EntityEventHandler {
    @SubscribeEvent
    public static void onEntityLivingUpdateEvent(final LivingUpdateEvent event) {
        final EntityLivingBase entity = event.getEntityLiving();
        final World world = entity.getEntityWorld();

        if (!world.isRemote) {
            // TODO: Implement this.
        }
    }

    @SubscribeEvent
    public static void onEntityLivingDeathEvent(final LivingDeathEvent event) {
        final EntityLivingBase entity = event.getEntityLiving();
        final World world = entity.getEntityWorld();

        if (!world.isRemote) {
            // TODO: Implement this.
        }
    }
}
