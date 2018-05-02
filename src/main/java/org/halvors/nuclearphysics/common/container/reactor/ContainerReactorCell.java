package org.halvors.nuclearphysics.common.container.reactor;

import net.minecraft.entity.player.InventoryPlayer;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.container.slot.SlotSpecific;
import org.halvors.nuclearphysics.common.item.reactor.fission.ItemBreederFuel;
import org.halvors.nuclearphysics.common.item.reactor.fission.ItemFissileFuel;
import org.halvors.nuclearphysics.common.tile.reactor.TileReactorCell;

public class ContainerReactorCell extends ContainerBase<TileReactorCell> {
    public ContainerReactorCell(final InventoryPlayer inventoryPlayer, final TileReactorCell tile) {
        super(1, inventoryPlayer, tile);

        addSlotToContainer(new SlotSpecific(tile, 0, 79, 17, ItemFissileFuel.class, ItemBreederFuel.class));

        // Player inventory
        addPlayerInventory(inventoryPlayer.player);
    }
}
