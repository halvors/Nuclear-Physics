package org.halvors.nuclearphysics.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import org.halvors.nuclearphysics.common.tile.TileBase;

public class ContainerDummy<T extends TileBase> extends Container {
    protected T tile;

    public ContainerDummy() {

    }

    public ContainerDummy(T tile) {
        this.tile = tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return !(tile instanceof IInventory) || ((IInventory) tile).isUsableByPlayer(player);

    }
}
