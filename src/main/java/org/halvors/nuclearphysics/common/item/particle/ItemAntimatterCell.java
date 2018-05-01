package org.halvors.nuclearphysics.common.item.particle;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.item.ItemTooltip;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemAntimatterCell extends ItemTooltip {
    public ItemAntimatterCell() {
        super("antimatter_cell");

        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public void registerItemModel() {
        for (EnumAntimatterCell type : EnumAntimatterCell.values()) {
            NuclearPhysics.getProxy().registerItemRenderer(this, type.ordinal(), name + "_" + type.getName());
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemStack, final EntityPlayer player, final List<String> list, final boolean flag) {
        list.add(LanguageUtility.transelate(getUnlocalizedName(itemStack) + ".tooltip"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull final Item item, final CreativeTabs tab, final List<ItemStack> list) {
        for (EnumAntimatterCell type : EnumAntimatterCell.values()) {
            list.add(new ItemStack(item, 1, type.ordinal()));
        }
    }

    @Override
    public int getEntityLifespan(final ItemStack itemStack, final World world) {
        return 160;
    }

    public enum EnumAntimatterCell {
        MILLIGRAM,
        GRAM;

        public String getName() {
            return name().toLowerCase();
        }
    }
}
