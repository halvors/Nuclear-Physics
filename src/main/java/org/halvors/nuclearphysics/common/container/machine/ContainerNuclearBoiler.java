package org.halvors.nuclearphysics.common.container.machine;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraftforge.items.SlotItemHandler;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.tile.machine.TileNuclearBoiler;

public class ContainerNuclearBoiler extends ContainerBase<TileNuclearBoiler> {
    public ContainerNuclearBoiler(final PlayerInventory playerInventory, final TileNuclearBoiler tile) {
        super(5, playerInventory, tile);

        // Battery
        addSlot(new SlotItemHandler(tile.getInventory(), 0, 56, 26));

        // Yellowcake Input
        addSlot(new SlotItemHandler(tile.getInventory(), 1, 81, 26));

        // Fluid input fill
        addSlot(new SlotItemHandler(tile.getInventory(), 2, 25, 19));

        // Fluid input drain
        addSlot(new SlotItemHandler(tile.getInventory(), 3, 25, 50));

        // Fluid output drain
        addSlot(new SlotItemHandler(tile.getInventory(), 4, 135, 50));

        // Player inventory
        addPlayerInventory(playerInventory.player);
    }
}
