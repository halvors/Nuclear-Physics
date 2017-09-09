package org.halvors.nuclearphysics.api.item;

import net.minecraft.item.ItemStack;
import org.halvors.nuclearphysics.api.tile.IReactor;


/**
 * All items that act as components in the reactor cell implements this method.
 */
public interface IReactorComponent {
    void onReact(ItemStack itemStack, IReactor reactor);
}
