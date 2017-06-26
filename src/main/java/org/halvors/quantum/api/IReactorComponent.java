package org.halvors.quantum.api;

import net.minecraft.item.ItemStack;


/**
 * All items that act as components in the reactor cell implements this method.
 */
public interface IReactorComponent {
    void onReact(ItemStack itemStack, IReactor reactor);
}
