package org.halvors.quantum.common.item.particle;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.api.explotion.ExplosionEvent;
import org.halvors.quantum.api.explotion.IExplosion;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.effect.poison.PoisonRadiation;
import org.halvors.quantum.common.item.ItemCell;
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }

    @SubscribeEvent
    public void itemExpireEvent(ItemExpireEvent event) {
        if (event.entityItem != null) {
            ItemStack itemStack = event.entityItem.getEntityItem();

            if (itemStack != null) {
                if (itemStack.getItem() == this) {
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

    public static class AntimatterExplosion extends Explosion implements IExplosion {
        private int tier;

        public AntimatterExplosion(World world, Entity entity, double x, double y, double z, float size, int tier) {
            super(world, entity, x, y, z, size + 2 * tier);
            this.tier = tier;
        }

        @Override
        public float getRadius() {
            return this.explosionSize;
        }

        @Override
        public long getEnergy() {
            return (long) ((2000000000000000L + (2000000000000000L * 9 * tier)) * ConfigurationManager.General.fulminationOutputMultiplier);
        }

        @Override
        public void explode() {
            doExplosionA();
            doExplosionB(true);
        }
    }
}
