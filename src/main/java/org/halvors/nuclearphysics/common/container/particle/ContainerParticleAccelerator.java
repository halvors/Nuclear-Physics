package org.halvors.nuclearphysics.common.container.particle;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.SlotItemHandler;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.tile.particle.TileParticleAccelerator;

public class ContainerParticleAccelerator extends ContainerBase<TileParticleAccelerator> {
    public ContainerParticleAccelerator(InventoryPlayer inventoryPlayer, TileParticleAccelerator tile) {
        super(4, inventoryPlayer, tile);

        // Inputs
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 0, 132, 26));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 1, 132, 51));

        // Output
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 2, 132, 75));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 3, 106, 75));

        // Player inventory
        addPlayerInventory(inventoryPlayer.player);
    }
}
