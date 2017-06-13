package org.halvors.quantum.common.container.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import org.halvors.quantum.common.tile.machine.TileChemicalExtractor;
import org.halvors.quantum.lib.gui.ContainerBase;
import org.halvors.quantum.lib.gui.slot.SlotEnergyItem;

public class ContainerChemicalExtractor extends ContainerBase {
    public ContainerChemicalExtractor(InventoryPlayer inventoryPlayer, TileChemicalExtractor tile) {
        super(tile);

        // Battery
        addSlotToContainer(new SlotEnergyItem(tile, 0, 80, 50));

        // Process Input (Cell or Uranium)
        addSlotToContainer(new Slot(tile, 1, 53, 25));

        // Process Output
        addSlotToContainer(new SlotFurnace(inventoryPlayer.player, tile, 2, 107, 25));

        // Fluid input fill
        addSlotToContainer(new Slot(tile, 3, 25, 19));

        // Fluid input drain
        addSlotToContainer(new Slot(tile, 4, 25, 50));

        // Fluid output fill
        addSlotToContainer(new Slot(tile, 5, 135, 19));

        // Fluid output drain
        addSlotToContainer(new Slot(tile, 6, 135, 50));
        addPlayerInventory(inventoryPlayer.player);
    }
}