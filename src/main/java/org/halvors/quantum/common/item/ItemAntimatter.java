package org.halvors.quantum.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.effect.poison.PoisonRadiation;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.common.utility.LanguageUtility;

import java.util.List;

public class ItemAntimatter extends ItemCell {
    @SideOnly(Side.CLIENT)
    private IIcon iconGram;

    public ItemAntimatter() {
        super("antimatter");

        setMaxDurability(0);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return 160;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(Reference.PREFIX + "antimatter_milligram");
        iconGram = iconRegister.registerIcon(Reference.PREFIX + "antimatter_gram");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int metadata) {
        return metadata >= 1 ? iconGram : itemIcon;
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
        list.add(LanguageUtility.localize(getUnlocalizedName(itemStack) + ".tooltip"));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }
}

    /*
    @ForgeSubscribe
    public void baoZhaEvent(ItemExpireEvent event) {
        if (event.entityItem != null) {
            ItemStack itemStack = event.entityItem.getEntityItem();

            if (itemStack != null) {
                if (itemStack.getItem() == this) {
                    event.entityItem.worldObj.playSoundEffect(event.entityItem.posX, event.entityItem.posY, event.entityItem.posZ, Reference.PREFIX + "antimatter", 3F, 1F - event.entityItem.worldObj.rand.nextFloat() * 0.3F);

                    if (!event.entityItem.worldObj.isRemote) {
                        if (!FlagRegistry.getModFlag(FlagRegistry.DEFAULT_NAME).containsValue(evt.entityItem.worldObj, Atomic.BAN_ANTIMATTER_POWER, "true", new Vector3(evt.entityItem))) {
                            IExplosion explosive = new BzFanWuSu(event.entity.worldObj, evt.entityItem, evt.entityItem.posX, evt.entityItem.posY, evt.entityItem.posZ, 4, itemStack.getItemDamage());
                            MinecraftForge.EVENT_BUS.post(new DoExplosionEvent(evt.entityItem.worldObj, explosive));
                            event.entityItem.worldObj.createExplosion(evt.entityItem, evt.entityItem.posX, evt.entityItem.posY, evt.entityItem.posZ, explosive.getRadius(), true);
                            ResonantInduction.LOGGER.fine("Antimatter cell detonated at: " + evt.entityItem.posX + ", " + evt.entityItem.posY + ", " + evt.entityItem.posZ);

                            int radius = 20;
                            AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(event.entityItem.posX - radius, event.entityItem.posY - radius, event.entityItem.posZ - radius, event.entityItem.posX + radius, event.entityItem.posY + radius, event.entityItem.posZ + radius);
                            List<EntityLiving> entitiesNearby = event.entityItem.worldObj.getEntitiesWithinAABB(EntityLiving.class, bounds);

                            for (EntityLiving entity : entitiesNearby) {
                                PoisonRadiation.INSTANCE.poisonEntity(new Vector3(entity), entity);
                            }
                        }
                    }
                }
            }
        }
    }
    */