package org.halvors.atomicscience.common.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerUtils {
	@SideOnly(Side.CLIENT)
	public static EntityPlayer getClientPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}

	/**
	 * Get a player from it's unique id.
	 * @param uuid the uuid of the player.
	 * @return the EntityPlayerMP object.
	 */
	public static EntityPlayerMP getPlayerFromUUID(UUID uuid) {
		for (EntityPlayerMP player : getPlayers()) {
			if (uuid.equals(player.getPersistentID())) {
				return player;
			}
		}

		return null;
	}

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
