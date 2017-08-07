package org.halvors.quantum.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import org.halvors.quantum.common.tile.TileQuantum;

public class ContainerQuantum extends ContainerBase {
    private TileQuantum tile;

    public ContainerQuantum(InventoryPlayer inventoryPlayer, TileQuantum tile) {
        super(inventoryPlayer);

        this.tile = tile;

        if (tile != null) {
            tile.open(inventoryPlayer.player);
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        if (tile != null) {
            tile.close(player);
        }
    }
}
