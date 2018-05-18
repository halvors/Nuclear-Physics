package org.halvors.nuclearphysics.common.container.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.container.slot.SlotEnergyItem;
import org.halvors.nuclearphysics.common.container.slot.SlotSpecific;
import org.halvors.nuclearphysics.common.init.ModBlocks;
import org.halvors.nuclearphysics.common.tile.machine.TileChemicalExtractor;

public class ContainerChemicalExtractor extends ContainerBase<TileChemicalExtractor> {
    public ContainerChemicalExtractor(final InventoryPlayer inventoryPlayer, final TileChemicalExtractor tile) {
        super(7, inventoryPlayer, tile);

        // Battery
        addSlotToContainer(new SlotEnergyItem(tile, 0, 80, 50));
        
        // Yellowcake Input
        addSlotToContainer(new SlotSpecific(tile, 1, 81, 26, OreDictionary.getOres("oreUranium").toArray(new ItemStack[0])));

        // Process Input (Uranium)
        addSlotToContainer(new SlotSpecific(tile, 1, 53, 25, new ItemStack(ModBlocks.blockUraniumOre)));

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

        // Player inventory
        addPlayerInventory(inventoryPlayer.player);
    }
}