package org.halvors.nuclearphysics.common.container.particle;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraftforge.items.SlotItemHandler;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.tile.particle.TileParticleAccelerator;

public class ContainerParticleAccelerator extends ContainerBase<TileParticleAccelerator> {
    public ContainerParticleAccelerator(final PlayerInventory playerInventory, final TileParticleAccelerator tile) {
        super(4, playerInventory, tile);

        // Inputs
        addSlot(new SlotItemHandler(tile.getInventory(), 0, 132, 26));
        addSlot(new SlotItemHandler(tile.getInventory(), 1, 132, 51));

        // Output
        addSlot(new SlotItemHandler(tile.getInventory(), 2, 132, 75));
        addSlot(new SlotItemHandler(tile.getInventory(), 3, 106, 75));

        // Player inventory
        addPlayerInventory(playerInventory.player);
    }
}
