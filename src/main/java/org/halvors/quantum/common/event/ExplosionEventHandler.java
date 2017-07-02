package org.halvors.quantum.common.event;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.api.explosion.ExplosionEvent;
import org.halvors.quantum.api.explosion.IExplosion;
import org.halvors.quantum.common.effect.explosion.AntimatterExplosion;
import org.halvors.quantum.common.effect.poison.PoisonRadiation;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

import java.util.List;

public class ExplosionEventHandler {
    @SubscribeEvent
    public void onItemExpireEvent(ItemExpireEvent event) {
        if (event.getEntityItem() != null) {
            ItemStack itemStack = event.getEntityItem().getEntityItem();

            if (itemStack != null) {
                EntityItem entityItem = event.getEntityItem();

                if (itemStack.getItem() == Quantum.itemAntimatterCell) {
                    //event.getEntityItem().getEntityWorld().playSoundEffect(entityItem.posX, entityItem.posY, entityItem.posZ, Reference.PREFIX + "antimatter", 3F, 1F - entityItem.getEntityWorld().rand.nextFloat() * 0.3F);

                    if (!entityItem.getEntityWorld().isRemote) {
                        //if (!FlagRegistry.getModFlag(FlagRegistry.DEFAULT_NAME).containsValue(event.entityItem.worldObj, Atomic.BAN_ANTIMATTER_POWER, "true", new Vector3(event.entityItem))) {
                            IExplosion explosive = new AntimatterExplosion(event.getEntity().getEntityWorld(), entityItem, entityItem.posX, entityItem.posY, entityItem.posZ, 4, itemStack.getMetadata());
                            MinecraftForge.EVENT_BUS.post(new ExplosionEvent.DoExplosionEvent(entityItem.getEntityWorld(), explosive));
                            entityItem.getEntityWorld().createExplosion(entityItem, entityItem.posX, entityItem.posY, entityItem.posZ, explosive.getRadius(), true);
                            Quantum.getLogger().info("Antimatter cell detonated at: " + entityItem.posX + ", " + entityItem.posY + ", " + entityItem.posZ);

                            int radius = 20;
                            AxisAlignedBB bounds = new AxisAlignedBB(entityItem.posX - radius, entityItem.posY - radius, entityItem.posZ - radius, entityItem.posX + radius, entityItem.posY + radius, entityItem.posZ + radius);
                            List<EntityLiving> entitiesNearby = entityItem.getEntityWorld().getEntitiesWithinAABB(EntityLiving.class, bounds);

                            for (EntityLiving entity : entitiesNearby) {
                                PoisonRadiation.getInstance().poisonEntity(entity.getPosition(), entity);
                            }
                        //}
                    }
                }
            }
        }
    }
}
