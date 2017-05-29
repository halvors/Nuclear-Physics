package org.halvors.atomicscience.old.particle.fulmination;

import atomicscience.AtomicScience;
import atomicscience.Settings;
import atomicscience.base.ItemCell;
import calclavia.api.icbm.explosion.ExplosionEvent.DoExplosionEvent;
import calclavia.api.icbm.explosion.IExplosion;
import calclavia.lib.flag.FlagRegistry;
import calclavia.lib.flag.ModFlag;
import calclavia.lib.prefab.poison.Poison;
import calclavia.lib.prefab.poison.PoisonRadiation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventBus;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import universalelectricity.api.vector.Vector3;

public class ItemAntimatter
        extends ItemCell
{
    private Icon iconGram;

    public ItemAntimatter(int itemID)
    {
        super(itemID);
        func_77656_e(0);
        func_77627_a(true);
    }

    @SideOnly(Side.CLIENT)
    public void func_94581_a(IconRegister iconRegister)
    {
        this.field_77791_bV = iconRegister.func_94245_a(func_77658_a().replace("item.", "") + "_milligram");
        this.iconGram = iconRegister.func_94245_a(func_77658_a().replace("item.", "") + "_gram");
    }

    public Icon func_77617_a(int metadata)
    {
        if (metadata >= 1) {
            return this.iconGram;
        }
        return this.field_77791_bV;
    }

    public void func_77633_a(int id, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(id, 1, 0));
        par3List.add(new ItemStack(id, 1, 1));
    }

    public int getEntityLifespan(ItemStack itemStack, World world)
    {
        return 160;
    }

    @ForgeSubscribe
    public void baoZhaEvent(ItemExpireEvent evt)
    {
        if (evt.entityItem != null)
        {
            ItemStack itemStack = evt.entityItem.func_92059_d();
            if (itemStack != null) {
                if (itemStack.field_77993_c == this.field_77779_bT)
                {
                    evt.entityItem.field_70170_p.func_72908_a(evt.entityItem.field_70165_t, evt.entityItem.field_70163_u, evt.entityItem.field_70161_v, "atomicscience:antimatter", 3.0F, 1.0F - evt.entityItem.field_70170_p.field_73012_v.nextFloat() * 0.3F);
                    if (!evt.entityItem.field_70170_p.field_72995_K) {
                        if (!FlagRegistry.getModFlag("ModFlags").containsValue(evt.entityItem.field_70170_p, AtomicScience.QIZI_FAN_WU_SU_BAO_ZHA, "true", new Vector3(evt.entityItem)))
                        {
                            IExplosion explosive = new BzFanWuSu(evt.entity.field_70170_p, evt.entityItem, evt.entityItem.field_70165_t, evt.entityItem.field_70163_u, evt.entityItem.field_70161_v, 4.0F, itemStack.func_77960_j());
                            MinecraftForge.EVENT_BUS.post(new ExplosionEvent.DoExplosionEvent(evt.entityItem.field_70170_p, explosive));
                            evt.entityItem.field_70170_p.func_72876_a(evt.entityItem, evt.entityItem.field_70165_t, evt.entityItem.field_70163_u, evt.entityItem.field_70161_v, explosive.getRadius(), true);
                            AtomicScience.LOGGER.fine("Antimatter cell detonated at: " + evt.entityItem.field_70165_t + ", " + evt.entityItem.field_70163_u + ", " + evt.entityItem.field_70161_v);

                            int radius = 20;
                            AxisAlignedBB bounds = AxisAlignedBB.func_72330_a(evt.entityItem.field_70165_t - 20.0D, evt.entityItem.field_70163_u - 20.0D, evt.entityItem.field_70161_v - 20.0D, evt.entityItem.field_70165_t + 20.0D, evt.entityItem.field_70163_u + 20.0D, evt.entityItem.field_70161_v + 20.0D);
                            List<EntityLiving> entitiesNearby = evt.entityItem.field_70170_p.func_72872_a(EntityLiving.class, bounds);
                            for (EntityLiving entity : entitiesNearby) {
                                PoisonRadiation.INSTANCE.poisonEntity(new Vector3(entity), entity);
                            }
                        }
                    }
                }
            }
        }
    }

    public static class BzFanWuSu
            extends Explosion
            implements IExplosion
    {
        private int tier;

        public BzFanWuSu(World par1World, Entity par2Entity, double x, double y, double z, float size, int tier)
        {
            super(par2Entity, x, y, z, size + 2 * tier);
            this.tier = tier;
        }

        public float getRadius()
        {
            return this.field_77280_f;
        }

        public long getEnergy()
        {
            return ((2000000000000000L + 18000000000000000L * this.tier) * Settings.fulminationOutputMultiplier);
        }

        public void explode()
        {
            func_77278_a();
            func_77279_a(true);
        }
    }
}
