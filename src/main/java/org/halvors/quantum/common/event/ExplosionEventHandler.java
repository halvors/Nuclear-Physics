package org.halvors.quantum.common.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.api.explotion.ExplosionEvent;
import org.halvors.quantum.api.explotion.IExplosion;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.effect.poison.PoisonRadiation;
import org.halvors.quantum.common.effect.explosion.AntimatterExplosion;
import org.halvors.quantum.common.transform.vector.Vector3;

import java.util.List;

public class ExplosionEventHandler {
    @SubscribeEvent
    public void onItemExpireEvent(ItemExpireEvent event) {
        if (event.entityItem != null) {
            ItemStack itemStack = event.entityItem.getEntityItem();

            if (itemStack != null) {
                if (itemStack.getItem() == Quantum.itemAntimatter) {
                    event.entityItem.worldObj.playSoundEffect(event.entityItem.posX, event.entityItem.posY, event.entityItem.posZ, Reference.PREFIX + "antimatter", 3F, 1F - event.entityItem.worldObj.rand.nextFloat() * 0.3F);

                    if (!event.entityItem.worldObj.isRemote) {
                        //if (!FlagRegistry.getModFlag(FlagRegistry.DEFAULT_NAME).containsValue(event.entityItem.worldObj, Atomic.BAN_ANTIMATTER_POWER, "true", new Vector3(event.entityItem))) {
                            IExplosion explosive = new AntimatterExplosion(event.entity.worldObj, event.entityItem, event.entityItem.posX, event.entityItem.posY, event.entityItem.posZ, 4, itemStack.getMetadata());
                            MinecraftForge.EVENT_BUS.post(new ExplosionEvent.DoExplosionEvent(event.entityItem.worldObj, explosive));
                            event.entityItem.worldObj.createExplosion(event.entityItem, event.entityItem.posX, event.entityItem.posY, event.entityItem.posZ, explosive.getRadius(), true);
                            Quantum.getLogger().info("Antimatter cell detonated at: " + event.entityItem.posX + ", " + event.entityItem.posY + ", " + event.entityItem.posZ);

                            int radius = 20;
                            AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(event.entityItem.posX - radius, event.entityItem.posY - radius, event.entityItem.posZ - radius, event.entityItem.posX + radius, event.entityItem.posY + radius, event.entityItem.posZ + radius);
                            List<EntityLiving> entitiesNearby = event.entityItem.worldObj.getEntitiesWithinAABB(EntityLiving.class, bounds);

                            for (EntityLiving entity : entitiesNearby) {
                                PoisonRadiation.INSTANCE.poisonEntity(new Vector3(entity), entity);
                            }
                        //}
                    }
                }
            }
        }
    }
}
