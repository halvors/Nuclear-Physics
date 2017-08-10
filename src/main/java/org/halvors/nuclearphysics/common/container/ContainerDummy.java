package org.halvors.nuclearphysics.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class ContainerDummy extends Container {
    protected TileEntity tile;

    public ContainerDummy() {

    }

    public ContainerDummy(TileEntity tile) {
        this.tile = tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return !(tile instanceof IInventory) || ((IInventory) tile).isUsableByPlayer(player);

    }
}
