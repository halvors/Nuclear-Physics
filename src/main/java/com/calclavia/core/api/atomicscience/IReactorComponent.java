package com.calclavia.core.api.atomicscience;

import net.minecraft.item.ItemStack;

public abstract interface IReactorComponent
{
    public abstract void onReact(ItemStack paramItemStack, IReactor paramIReactor);
}
