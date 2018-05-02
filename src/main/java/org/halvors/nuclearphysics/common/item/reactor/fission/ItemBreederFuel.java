package org.halvors.nuclearphysics.common.item.reactor.fission;

import net.minecraft.item.ItemStack;
import org.halvors.nuclearphysics.api.item.IReactorComponent;
import org.halvors.nuclearphysics.api.tile.IReactor;

public class ItemBreederFuel extends ItemFuel implements IReactorComponent {
    public ItemBreederFuel() {
        super("breeder_fuel");
    }

    @Override
    public void onReact(final ItemStack itemStack, final IReactor reactor) {
        reactor.heat(energyPerTick / 2);

        if (reactor.getWorldObject().getWorldTime() % 20 == 0) {
            itemStack.setMetadata(Math.min(itemStack.getMetadata() + 1, itemStack.getMaxDurability()));
        }
    }
}