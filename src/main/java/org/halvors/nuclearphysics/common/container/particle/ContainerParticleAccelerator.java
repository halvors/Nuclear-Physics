package org.halvors.nuclearphysics.common.container.particle;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.tile.particle.TileParticleAccelerator;

public class ContainerParticleAccelerator extends ContainerBase<TileParticleAccelerator> {
    public ContainerParticleAccelerator(final InventoryPlayer inventoryPlayer, final TileParticleAccelerator tile) {
        super(4, inventoryPlayer, tile);

        // Inputs
        addSlotToContainer(new Slot(tile, 0, 132, 26));
        addSlotToContainer(new Slot(tile, 1, 132, 51));

        // Output
        addSlotToContainer(new SlotFurnace(inventoryPlayer.player, tile, 2, 132, 75));
        addSlotToContainer(new SlotFurnace(inventoryPlayer.player, tile, 3, 106, 75));

        // Player inventory
        addPlayerInventory(inventoryPlayer.player);
    }
}
