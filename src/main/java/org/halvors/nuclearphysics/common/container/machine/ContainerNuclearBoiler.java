package org.halvors.nuclearphysics.common.container.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.container.slot.SlotEnergyItem;
import org.halvors.nuclearphysics.common.container.slot.SlotSpecific;
import org.halvors.nuclearphysics.common.tile.machine.TileNuclearBoiler;

import java.util.ArrayList;
import java.util.List;

public class ContainerNuclearBoiler extends ContainerBase<TileNuclearBoiler> {
    public ContainerNuclearBoiler(final InventoryPlayer inventoryPlayer, final TileNuclearBoiler tile) {
        super(5, inventoryPlayer, tile);

        // Battery
        addSlotToContainer(new SlotEnergyItem(tile, 0, 56, 26));

        final List<ItemStack> itemStackList = new ArrayList<>();
        itemStackList.addAll(OreDictionary.getOres("oreUranium"));
        itemStackList.addAll(OreDictionary.getOres("dustUranium"));

        // Yellowcake Input
        addSlotToContainer(new SlotSpecific(tile, 1, 81, 26, itemStackList.toArray(new ItemStack[0])));

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
