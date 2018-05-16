package org.halvors.nuclearphysics.common.container.particle;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.SlotItemHandler;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.tile.particle.TileParticleAccelerator;

public class ContainerParticleAccelerator extends ContainerBase<TileParticleAccelerator> {
    public ContainerParticleAccelerator(final InventoryPlayer inventoryPlayer, final TileParticleAccelerator tile) {
        super(4, inventoryPlayer, tile);

        // Inputs
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 0, 151, 19));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 1, 151, 43));

        // Output
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 2, 151, 67));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 3, 128, 67));

        // Player inventory
        addPlayerInventory(inventoryPlayer.player);
    }
}
