package org.halvors.nuclearphysics.common.container.reactor;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.SlotItemHandler;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.tile.reactor.TileReactorCell;

public class ContainerReactorCell extends ContainerBase<TileReactorCell> {
    public ContainerReactorCell(final InventoryPlayer inventoryPlayer, final TileReactorCell tile) {
        super(1, inventoryPlayer, tile);

        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 0, 79, 17));

        // Player inventory
        addPlayerInventory(inventoryPlayer.player);
    }
}
