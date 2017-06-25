package org.halvors.quantum.api;

import net.minecraft.item.ItemStack;
import org.halvors.quantum.api.IReactor;

public interface IReactorComponent {
    void onReact(ItemStack itemStack, IReactor reactor);
}
