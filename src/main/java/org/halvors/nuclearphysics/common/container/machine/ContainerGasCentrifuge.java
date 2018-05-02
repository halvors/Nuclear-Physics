package org.halvors.nuclearphysics.common.container.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.container.slot.SlotEnergyItem;
import org.halvors.nuclearphysics.common.tile.machine.TileGasCentrifuge;

public class ContainerGasCentrifuge extends ContainerBase<TileGasCentrifuge> {
    public ContainerGasCentrifuge(final InventoryPlayer inventoryPlayer, final TileGasCentrifuge tile) {
        super(4, inventoryPlayer, tile);

        // Battery
        addSlotToContainer(new SlotEnergyItem(tile, 0, 131, 26));

        // Uranium Gas Tank
        addSlotToContainer(new Slot(tile, 1, 25, 50));

        // Output Uranium 235
        addSlotToContainer(new SlotFurnace(inventoryPlayer.player, tile, 2, 81, 26));

        // Output Uranium 238
        addSlotToContainer(new SlotFurnace(inventoryPlayer.player, tile, 3, 101, 26));

        // Player inventory
        addPlayerInventory(inventoryPlayer.player);
    }
}
