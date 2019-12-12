package org.halvors.nuclearphysics.common.container.machine;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraftforge.items.SlotItemHandler;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.tile.machine.TileQuantumAssembler;

public class ContainerQuantumAssembler extends ContainerBase<TileQuantumAssembler> {
    public ContainerQuantumAssembler(final PlayerInventory playerInventory, final TileQuantumAssembler tile) {
        super(7, playerInventory, tile);

        yInventoryDisplacement = 148;
        yHotBarDisplacement = 206;

        addSlot(new SlotItemHandler(tile.getInventory(), 0, 80, 40));
        addSlot(new SlotItemHandler(tile.getInventory(), 1, 53, 56));
        addSlot(new SlotItemHandler(tile.getInventory(), 2, 107, 56));
        addSlot(new SlotItemHandler(tile.getInventory(), 3, 53, 88));
        addSlot(new SlotItemHandler(tile.getInventory(), 4, 107, 88));
        addSlot(new SlotItemHandler(tile.getInventory(), 5, 80, 103));
        addSlot(new SlotItemHandler(tile.getInventory(), 6, 80, 72));

        // Player inventory
        addPlayerInventory(playerInventory.player);
    }
}
