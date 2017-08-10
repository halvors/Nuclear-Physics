package org.halvors.nuclearphysics.common.event.handler;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.halvors.nuclearphysics.api.explosion.ExplosionEvent.DoExplosionEvent;
import org.halvors.nuclearphysics.api.explosion.IExplosion;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.effect.explosion.AntimatterExplosion;
import org.halvors.nuclearphysics.common.effect.poison.PoisonRadiation;
import org.halvors.nuclearphysics.common.init.QuantumItems;
import org.halvors.nuclearphysics.common.init.QuantumSoundEvents;

import java.util.List;

public class ExplosionEventHandler {
    @SubscribeEvent
    public void onItemExpireEvent(ItemExpireEvent event) {
        EntityItem entityItem = event.getEntityItem();

        if (entityItem != null) {
            ItemStack itemStack = entityItem.getEntityItem();

            if (itemStack.getItem() == QuantumItems.itemAntimatterCell) {
                World world = entityItem.getEntityWorld();
                world.playSound(entityItem.posX, entityItem.posY, entityItem.posZ, QuantumSoundEvents.ANTIMATTER, SoundCategory.NEUTRAL, 3, 1 - world.rand.nextFloat() * 0.3F, false);

                if (!entityItem.getEntityWorld().isRemote) {
                    //if (!FlagRegistry.getModFlag(FlagRegistry.DEFAULT_NAME).containsValue(event.entityItem.worldObj, Atomic.BAN_ANTIMATTER_POWER, "true", new Vector3(event.entityItem))) {
                        IExplosion explosive = new AntimatterExplosion(event.getEntity().getEntityWorld(), entityItem, entityItem.posX, entityItem.posY, entityItem.posZ, 4, itemStack.getMetadata());
                        MinecraftForge.EVENT_BUS.post(new DoExplosionEvent(world, explosive));
                        world.createExplosion(entityItem, entityItem.posX, entityItem.posY, entityItem.posZ, explosive.getRadius(), true);
                        NuclearPhysics.getLogger().info("Antimatter cell detonated at: " + entityItem.posX + ", " + entityItem.posY + ", " + entityItem.posZ);

                        int radius = 20;
                        AxisAlignedBB bounds = new AxisAlignedBB(entityItem.posX - radius, entityItem.posY - radius, entityItem.posZ - radius, entityItem.posX + radius, entityItem.posY + radius, entityItem.posZ + radius);
                        List<EntityLiving> entitiesNearby = world.getEntitiesWithinAABB(EntityLiving.class, bounds);

                        for (EntityLiving entity : entitiesNearby) {
                            PoisonRadiation.getInstance().poisonEntity(entity.getPosition(), entity);
                        }
                    //}
                }
            }
        }
    }
}
