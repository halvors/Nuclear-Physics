package org.halvors.quantum.api.item;

import net.minecraft.item.ItemStack;
import org.halvors.quantum.api.tile.IReactor;


/**
 * All items that act as components in the reactor cell implements this method.
 */
public interface IReactorComponent {
    void onReact(ItemStack itemStack, IReactor reactor);
}
