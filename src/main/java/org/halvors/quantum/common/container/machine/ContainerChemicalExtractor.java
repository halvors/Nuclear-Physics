package org.halvors.quantum.common.container.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.SlotItemHandler;
import org.halvors.quantum.common.QuantumBlocks;
import org.halvors.quantum.common.container.ContainerQuantum;
import org.halvors.quantum.common.container.slot.SlotItemHandlerSpecificCapability;
import org.halvors.quantum.common.container.slot.SlotItemHandlerSpecificItem;
import org.halvors.quantum.common.tile.machine.TileChemicalExtractor;

public class ContainerChemicalExtractor extends ContainerQuantum {
    public ContainerChemicalExtractor(InventoryPlayer inventoryPlayer, TileChemicalExtractor tile) {
        super(inventoryPlayer, tile);

        // Battery
        addSlotToContainer(new SlotItemHandlerSpecificCapability(tile.getInventory(), 0, 80, 50, CapabilityEnergy.ENERGY));

        // Process Input (Uranium)
        addSlotToContainer(new SlotItemHandlerSpecificItem(tile.getInventory(), 1, 53, 25, new ItemStack(QuantumBlocks.blockUraniumOre)));

        // Process Output
        //addSlotToContainer(new SlotFurnaceOutput(inventoryPlayer.player, tile, 2, 107, 25));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 2, 107, 25));

        // Fluid input fill
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 3, 25, 19));

        // Fluid input drain
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 4, 25, 50));

        // Fluid output drain
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 5, 135, 19));

        addPlayerInventory(inventoryPlayer.player);
    }
}