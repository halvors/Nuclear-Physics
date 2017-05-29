package org.halvors.atomicscience.old.fission;

import com.calclavia.core.api.atomicscience.IReactor;
import com.calclavia.core.api.atomicscience.IReactorComponent;
import net.minecraft.item.ItemStack;

public class ItemBreederFuel extends ItemRadioactive implements IReactorComponent {
    public ItemBreederFuel() {
        setMaxDurability(2500);
        setMaxStackSize(1);
        setNoRepair();
    }

    @Override
    public void onReact(ItemStack itemStack, IReactor reactor) {
        reactor.heat(2000000L);

        if (reactor.getWorld().getWorldTime() % 20L == 0L) {
            itemStack.setMetadata(Math.min(itemStack.getMetadata() + 1, itemStack.getMaxDurability()));
        }
    }
}
