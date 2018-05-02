package org.halvors.nuclearphysics.common.item.reactor.fission;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.halvors.nuclearphysics.common.item.ItemRadioactive;

import java.util.List;

public class ItemFuel extends ItemRadioactive {
    public static final int decay = 2500;

    // The energy in one KG of uranium is: 72PJ, 100TJ in one cell of uranium.
    public static final long energyDensity = 100000000000L;

    // Approximately 20,000,000J per tick. 400 MW.
    public static final long energyPerTick = energyDensity / 50000;

    public ItemFuel(final String name) {
        super(name);

        setMaxStackSize(1);
        setMaxDurability(decay);
        setNoRepair();
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item item, final CreativeTabs tab, final List list) {
        list.add(new ItemStack(item));
        list.add(new ItemStack(item, 1, getMaxDurability() - 1));
    }
}