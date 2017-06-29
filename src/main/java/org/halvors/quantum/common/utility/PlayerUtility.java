package org.halvors.quantum.common.utility;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtility {
	@SuppressWarnings("unchecked")
	public static List<EntityPlayerMP> getPlayers() {
		List<EntityPlayerMP> playerList = new ArrayList<>();
		MinecraftServer server = MinecraftServer.getServer();

		if (server != null) {
			return server.getConfigurationManager().playerEntityList;
		}

		return playerList;
	}
}
