package org.halvors.nuclearphysics.common.container.reactor;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraftforge.items.SlotItemHandler;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.tile.reactor.TileReactorCell;

public class ContainerReactorCell extends ContainerBase<TileReactorCell> {
    public ContainerReactorCell(final PlayerInventory playerInventory, final TileReactorCell tile) {
        super(1, playerInventory, tile);

        addSlot(new SlotItemHandler(tile.getInventory(), 0, 79, 17));

        // Player inventory
        addPlayerInventory(playerInventory.player);
    }
}
