package org.halvors.nuclearphysics.common.item.particle;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.item.ItemTooltip;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemAntimatterCell extends ItemTooltip {
    public ItemAntimatterCell() {
        super("antimatter_cell");

        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public void registerItemModel() {
        for (final EnumAntimatterCell type : EnumAntimatterCell.values()) {
            NuclearPhysics.getProxy().registerItemRenderer(this, type.ordinal(), name + "_" + type.getName());
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemStack, @Nullable final World world, final List<String> list, final ITooltipFlag flag) {
        list.add(LanguageUtility.transelate(getUnlocalizedName(itemStack) + ".tooltip"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull final CreativeTabs tab, @Nonnull final NonNullList<ItemStack> list) {
        if (isInCreativeTab(tab)) {
            for (final EnumAntimatterCell type : EnumAntimatterCell.values()) {
                list.add(new ItemStack(this, 1, type.ordinal()));
            }
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
