package org.halvors.quantum.common.reactor.fission;

import net.minecraft.item.ItemStack;
import org.halvors.quantum.common.reactor.fission.IReactor;

public interface IReactorComponent {
    void onReact(ItemStack itemStack, IReactor reactor);
}
