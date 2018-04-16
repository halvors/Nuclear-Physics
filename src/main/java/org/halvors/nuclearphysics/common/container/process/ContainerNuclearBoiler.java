package org.halvors.nuclearphysics.common.container.process;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.container.slot.SlotEnergyItem;
import org.halvors.nuclearphysics.common.container.slot.SlotSpecific;
import org.halvors.nuclearphysics.common.init.ModBlocks;
import org.halvors.nuclearphysics.common.init.ModItems;
import org.halvors.nuclearphysics.common.tile.machine.TileNuclearBoiler;

public class ContainerNuclearBoiler extends ContainerBase<TileNuclearBoiler> {
    public ContainerNuclearBoiler(InventoryPlayer inventoryPlayer, TileNuclearBoiler tile) {
        super(5, inventoryPlayer, tile);

        // Battery
        addSlotToContainer(new SlotEnergyItem(tile, 0, 56, 26));

        // Yellowcake Input
        addSlotToContainer(new SlotSpecific(tile, 1, 81, 26, new ItemStack(ModItems.itemYellowCake), new ItemStack(ModBlocks.blockUraniumOre)));

        // Fluid input fill
        addSlotToContainer(new Slot(tile, 2, 25, 19));

        // Fluid input drain
        addSlotToContainer(new Slot(tile, 3, 25, 50));

        // Fluid output drain
        addSlotToContainer(new Slot(tile, 4, 135, 50));

        // Player inventory
        addPlayerInventory(inventoryPlayer.player);
    }
}
