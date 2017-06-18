package org.halvors.quantum.lib.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import org.halvors.quantum.lib.IPlayerUsing;

public class ContainerDummy extends Container {
    protected TileEntity tile;

    public ContainerDummy() {

    }

    public ContainerDummy(TileEntity tile) {
        this.tile = tile;
    }

    public ContainerDummy(EntityPlayer player, TileEntity tile) {
        this(tile);

        if (tile instanceof IPlayerUsing) {
            ((IPlayerUsing) tile).getPlayersUsing().add(player);
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        if (tile instanceof IPlayerUsing) {
            ((IPlayerUsing) tile).getPlayersUsing().remove(player);
        }

        super.onContainerClosed(player);
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
        return !(tile instanceof IInventory) || ((IInventory) tile).isUseableByPlayer(par1EntityPlayer);

    }
}
