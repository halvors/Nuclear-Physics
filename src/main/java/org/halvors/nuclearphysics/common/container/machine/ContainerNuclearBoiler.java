package org.halvors.nuclearphysics.common.container.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.SlotItemHandler;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.tile.machine.TileNuclearBoiler;

public class ContainerNuclearBoiler extends ContainerBase<TileNuclearBoiler> {
    public ContainerNuclearBoiler(final InventoryPlayer inventoryPlayer, final TileNuclearBoiler tile) {
        super(5, inventoryPlayer, tile);

        // Fluid input fill
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 2, 35, 19));

        // Fluid input drain
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 3, 35, 50));

        // Yellowcake Input
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 1, 65, 26));

        // Battery
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 0, 85, 26));

        // Gas output drain
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 4, 135, 50));

        // Player inventory
        addPlayerInventory(inventoryPlayer.player);
    }
}
