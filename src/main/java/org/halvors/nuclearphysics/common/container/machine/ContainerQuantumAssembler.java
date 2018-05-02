package org.halvors.nuclearphysics.common.container.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.tile.machine.TileQuantumAssembler;

public class ContainerQuantumAssembler extends ContainerBase<TileQuantumAssembler> {
    public ContainerQuantumAssembler(final InventoryPlayer inventoryPlayer, final TileQuantumAssembler tile) {
        super(7, inventoryPlayer, tile);

        yInventoryDisplacement = 148;
        yHotBarDisplacement = 206;

        addSlotToContainer(new Slot(tile, 0, 80, 40));
        addSlotToContainer(new Slot(tile, 1, 53, 56));
        addSlotToContainer(new Slot(tile, 2, 107, 56));
        addSlotToContainer(new Slot(tile, 3, 53, 88));
        addSlotToContainer(new Slot(tile, 4, 107, 88));
        addSlotToContainer(new Slot(tile, 5, 80, 103));
        addSlotToContainer(new Slot(tile, 6, 80, 72));

        // Player inventory
        addPlayerInventory(inventoryPlayer.player);
    }
}
