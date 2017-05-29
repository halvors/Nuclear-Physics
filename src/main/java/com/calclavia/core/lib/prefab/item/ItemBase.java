package com.calclavia.core.lib.prefab.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

public class ItemBase extends Item
{
    public ItemBase(String name, Configuration config, String prefix, CreativeTabs tab)
    {

        setUnlocalizedName(prefix + name);
        setCreativeTab(tab);
        setTextureName(prefix + name);
    }
}