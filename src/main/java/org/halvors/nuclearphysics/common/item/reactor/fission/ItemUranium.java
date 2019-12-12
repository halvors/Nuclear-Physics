package org.halvors.nuclearphysics.common.item.reactor.fission;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.NuclearPhysics;
import org.halvors.nuclearphysics.common.item.ItemRadioactive;

import javax.annotation.Nonnull;

public class ItemUranium extends ItemRadioactive {
    public ItemUranium() {
        super("uranium");

        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public void registerItemModel() {
        for (final EnumUranium type : EnumUranium.values()) {
            NuclearPhysics.getProxy().registerItemRenderer(this, type.ordinal(), name);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull final CreativeTabs tab, @Nonnull final NonNullList<ItemStack> list) {
        if (isInCreativeTab(tab)) {
            for (final EnumUranium type : EnumUranium.values()) {
                list.add(new ItemStack(this, 1, type.ordinal()));
            }
        }
    }

    public enum EnumUranium {
        URANIUM_235,
        URANIUM_238;

        public String getName() {
            return name().toLowerCase();
        }
    }
}