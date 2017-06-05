package org.halvors.quantum.lib;

import net.minecraft.entity.player.EntityPlayer;

import java.util.HashSet;

public interface IPlayerUsing {
    HashSet<EntityPlayer> getPlayersUsing();
}
