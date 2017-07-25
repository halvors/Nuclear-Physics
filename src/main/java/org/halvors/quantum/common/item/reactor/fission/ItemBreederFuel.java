package org.halvors.quantum.common.item.reactor.fission;

import net.minecraft.item.ItemStack;
import org.halvors.quantum.api.item.IReactorComponent;
import org.halvors.quantum.api.tile.IReactor;
import org.halvors.quantum.common.item.ItemRadioactive;

public class ItemBreederFuel extends ItemRadioactive implements IReactorComponent {
    public ItemBreederFuel() {
        super("breeder_fuel");

        setMaxDamage(ItemFissileFuel.decay);
        setMaxStackSize(1);
        setNoRepair();
    }

    @Override
    public void onReact(ItemStack itemStack, IReactor reactor) {
        reactor.heat(ItemFissileFuel.energyPerTick / 2);

        if (reactor.getWorldObject().getWorldTime() % 20 == 0) {
            itemStack.setItemDamage(Math.min(itemStack.getMetadata() + 1, itemStack.getMaxDamage()));
        }
    }
}