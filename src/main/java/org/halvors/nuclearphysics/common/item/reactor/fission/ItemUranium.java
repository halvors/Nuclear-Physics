package org.halvors.nuclearphysics.common.item.reactor.fission;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.halvors.nuclearphysics.common.item.ItemRadioactive;

import java.util.List;

public class ItemUranium extends ItemRadioactive {
    public ItemUranium() {
        super("uranium");

        setHasSubtypes(true);
        setMaxDurability(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item item, final CreativeTabs tab, final List list) {
        for (final EnumUranium type : EnumUranium.values()) {
            list.add(new ItemStack(item, 1, type.ordinal()));
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