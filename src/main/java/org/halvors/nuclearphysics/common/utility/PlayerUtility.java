package org.halvors.nuclearphysics.common.utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.NuclearPhysics;

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

	public static boolean isOp(EntityPlayer player) {
		return player instanceof EntityPlayerMP && ((EntityPlayerMP) player).mcServer.getConfigurationManager().canSendCommands(player.getGameProfile());
	}

	public static void openGui(EntityPlayer player, World world, int x, int y, int z) {
		player.openGui(NuclearPhysics.getInstance(), 0, world, x, y, z);
	}
}
