package org.halvors.atomicscience.old;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import org.halvors.atomicscience.AtomicScience;

public class TabAS extends CreativeTabs
{
    public static final TabAS INSTANCE = new TabAS("atomicscience");

    public TabAS(String par2Str)
    {
        super(CreativeTabs.getNextID(), par2Str);
    }

    public ItemStack getIconItemStack()
    {
        return new ItemStack(AtomicScience.blockReactorCell);
    }
}
