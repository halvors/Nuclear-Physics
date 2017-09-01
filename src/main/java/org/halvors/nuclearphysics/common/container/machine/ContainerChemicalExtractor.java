package org.halvors.nuclearphysics.common.container.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.SlotItemHandler;
import org.halvors.nuclearphysics.common.container.ContainerNuclearPhysics;
import org.halvors.nuclearphysics.common.tile.machine.TileChemicalExtractor;

public class ContainerChemicalExtractor extends ContainerNuclearPhysics {
    public ContainerChemicalExtractor(InventoryPlayer inventoryPlayer, TileChemicalExtractor tile) {
        super(inventoryPlayer, tile);

        // Battery
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 0, 80, 50));

        // Process Input (Uranium)
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 1, 53, 25));

        // Process Output
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 2, 107, 25));

        // Fluid input fill
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 3, 25, 19));

        // Fluid input drain
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 4, 25, 50));

        // Fluid output fill
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 5, 135, 19));

        // Fluid output drain
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 6, 135, 50));

        addPlayerInventory(inventoryPlayer.player);
    }
}