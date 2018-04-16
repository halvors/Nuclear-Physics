package org.halvors.nuclearphysics.common.container.process;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.SlotItemHandler;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.tile.machine.TileGasCentrifuge;

public class ContainerGasCentrifuge extends ContainerBase<TileGasCentrifuge> {
    public ContainerGasCentrifuge(InventoryPlayer inventoryPlayer, TileGasCentrifuge tile) {
        super(4, inventoryPlayer, tile);

        // Battery
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 0, 131, 26));

        // Uranium Gas Tank
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 1, 25, 50));

        // Output Uranium 235
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 2, 81, 26));

        // Output Uranium 238
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 3, 101, 26));

        // Player inventory
        addPlayerInventory(inventoryPlayer.player);
    }
}
