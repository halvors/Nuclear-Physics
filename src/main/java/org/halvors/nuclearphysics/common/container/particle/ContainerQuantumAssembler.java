package org.halvors.nuclearphysics.common.container.particle;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.SlotItemHandler;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.tile.particle.TileQuantumAssembler;

public class ContainerQuantumAssembler extends ContainerBase<TileQuantumAssembler> {
    public ContainerQuantumAssembler(InventoryPlayer inventoryPlayer, TileQuantumAssembler tile) {
        super(7, inventoryPlayer, tile);

        yInventoryDisplacement = 148;
        yHotBarDisplacement = 206;

        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 0, 80, 40));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 1, 53, 56));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 2, 107, 56));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 3, 53, 88));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 4, 107, 88));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 5, 80, 103));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 6, 80, 72));

        // Player inventory
        addPlayerInventory(inventoryPlayer.player);
    }
}
