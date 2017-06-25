package org.halvors.quantum.lib.container;

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
    public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
        return !(tile instanceof IInventory) || ((IInventory) tile).isUseableByPlayer(par1EntityPlayer);

    }
}
