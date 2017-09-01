package org.halvors.nuclearphysics.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import org.halvors.nuclearphysics.common.tile.TileBase;

public class ContainerNuclearPhysics extends ContainerBase {
    private TileBase tile;

    public ContainerNuclearPhysics(InventoryPlayer inventoryPlayer, TileBase tile) {
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
