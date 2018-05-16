package org.halvors.nuclearphysics.common.container.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.SlotItemHandler;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.tile.machine.TileChemicalExtractor;

public class ContainerChemicalExtractor extends ContainerBase<TileChemicalExtractor> {
    public ContainerChemicalExtractor(final InventoryPlayer inventoryPlayer, final TileChemicalExtractor tile) {
        super(7, inventoryPlayer, tile);

        // Fluid input fill
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 3, 35, 19));

        // Fluid input drain
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 4, 35, 50));

        // Process Input (Uranium)
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 1, 59, 25));

        // Process Output
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 2, 111, 25));

        // Battery
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 0, 85, 50));

        // Fluid output fill
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 5, 135, 19));

        // Fluid output drain
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 6, 135, 50));

        // Player inventory
        addPlayerInventory(inventoryPlayer.player);
    }
}