package org.halvors.quantum.common.item.reactor.fission;

import net.minecraft.item.ItemStack;
import org.halvors.quantum.common.item.ItemRadioactive;
import org.halvors.quantum.common.tile.reactor.fission.IReactor;
import org.halvors.quantum.common.tile.reactor.fission.IReactorComponent;

public class ItemBreederFuel extends ItemRadioactive implements IReactorComponent {
    public ItemBreederFuel() {
        super("rodBreederFuel");

        setMaxDurability(ItemFissileFuel.decay);
        setMaxStackSize(1);
        setNoRepair();
    }

    @Override
    public void onReact(ItemStack itemStack, IReactor reactor) {
        reactor.heat(ItemFissileFuel.energyPerTick / 2);

        if (reactor.getWorld().getWorldTime() % 20 == 0) {
            itemStack.setMetadata(Math.min(itemStack.getMetadata() + 1, itemStack.getMaxDurability()));
        }
    }
}