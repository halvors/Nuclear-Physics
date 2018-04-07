package org.halvors.nuclearphysics.common.item.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.item.ItemTooltip;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.List;

public class ItemAntimatterCell extends ItemTooltip {
    @SideOnly(Side.CLIENT)
    private IIcon iconGram;

    public ItemAntimatterCell() {
        super("antimatter_cell");

        setHasSubtypes(true);
        setMaxDurability(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(Reference.PREFIX + name + "_milligram");
        iconGram = iconRegister.registerIcon(Reference.PREFIX + name + "_gram");
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
        list.add(LanguageUtility.transelate(getUnlocalizedName(itemStack) + ".tooltip"));
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (EnumAntimatterCell type : EnumAntimatterCell.values()) {
            list.add(new ItemStack(item, 1, type.ordinal()));
        }
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return 160;
    }

    public enum EnumAntimatterCell {
        MILLIGRAM("milligram"),
        GRAM("gram");

        private String name;

        EnumAntimatterCell(String name) {
            this.name = name;
        }

        public String getName() {
            return name.toLowerCase();
        }
    }
}
