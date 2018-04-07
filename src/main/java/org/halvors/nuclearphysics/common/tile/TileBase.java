package org.halvors.nuclearphysics.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import java.util.HashSet;
import java.util.Set;

public class TileBase extends TileEntity {
    private final Set<EntityPlayer> playersUsing = new HashSet<>();

    public void open(EntityPlayer player) {
        playersUsing.add(player);
    }

    public void close(EntityPlayer player) {
        playersUsing.remove(player);
    }

    public Set<EntityPlayer> getPlayersUsing() {
        return playersUsing;
    }

    public TileBase() {

    }
}
