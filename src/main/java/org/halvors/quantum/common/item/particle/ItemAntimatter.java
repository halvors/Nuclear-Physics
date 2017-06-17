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
}
