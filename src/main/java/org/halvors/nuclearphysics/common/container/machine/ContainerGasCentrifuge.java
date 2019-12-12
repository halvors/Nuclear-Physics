package org.halvors.nuclearphysics.common.container.machine;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraftforge.items.SlotItemHandler;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.tile.machine.TileGasCentrifuge;

public class ContainerGasCentrifuge extends ContainerBase<TileGasCentrifuge> {
    public ContainerGasCentrifuge(final PlayerInventory playerInventory, final TileGasCentrifuge tile) {
        super(4, playerInventory, tile);

        // Battery
        addSlot(new SlotItemHandler(tile.getInventory(), 0, 131, 26));

        // Uranium Gas Tank
        addSlot(new SlotItemHandler(tile.getInventory(), 1, 25, 50));

        // Output Uranium 235
        addSlot(new SlotItemHandler(tile.getInventory(), 2, 81, 26));

        // Output Uranium 238
        addSlot(new SlotItemHandler(tile.getInventory(), 3, 101, 26));

        // Player inventory
        addPlayerInventory(playerInventory.player);
    }
}
