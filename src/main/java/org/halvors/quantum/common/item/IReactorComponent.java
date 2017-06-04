package org.halvors.quantum.common.item;

import net.minecraft.item.ItemStack;

public interface IReactorComponent
{
    public abstract void onReact(ItemStack itemStack, IReactor reactor);
}
