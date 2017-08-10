package org.halvors.nuclearphysics.common.item.reactor.fission;

import net.minecraft.item.ItemStack;
import org.halvors.nuclearphysics.api.item.IReactorComponent;
import org.halvors.nuclearphysics.api.tile.IReactor;
import org.halvors.nuclearphysics.common.item.ItemRadioactive;

public class ItemBreederFuel extends ItemRadioactive implements IReactorComponent {
    public ItemBreederFuel() {
        super("breeder_fuel");

        setMaxStackSize(1);
        setMaxDamage(ItemFissileFuel.decay);
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