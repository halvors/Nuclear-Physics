package org.halvors.nuclearphysics.common.container.machine;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraftforge.items.SlotItemHandler;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.tile.machine.TileChemicalExtractor;

public class ContainerChemicalExtractor extends ContainerBase<TileChemicalExtractor> {
    public ContainerChemicalExtractor(final PlayerInventory playerInventory, final TileChemicalExtractor tile) {
        super(7, playerInventory, tile);

        // Battery
        addSlot(new SlotItemHandler(tile.getInventory(), 0, 80, 50));

        // Process Input (Uranium)
        addSlot(new SlotItemHandler(tile.getInventory(), 1, 53, 25));

        // Process Output
        addSlot(new SlotItemHandler(tile.getInventory(), 2, 107, 25));

        // Fluid input fill
        addSlot(new SlotItemHandler(tile.getInventory(), 3, 25, 19));

        // Fluid input drain
        addSlot(new SlotItemHandler(tile.getInventory(), 4, 25, 50));

        // Fluid output fill
        addSlot(new SlotItemHandler(tile.getInventory(), 5, 135, 19));

        // Fluid output drain
        addSlot(new SlotItemHandler(tile.getInventory(), 6, 135, 50));

        // Player inventory
        addPlayerInventory(playerInventory.player);
    }
}