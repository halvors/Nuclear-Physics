package org.halvors.quantum.common.utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

public class PlayerUtility {
	@SuppressWarnings("unchecked")
	public static List<EntityPlayerMP> getPlayers() {
		List<EntityPlayerMP> playerList = new ArrayList<>();
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

		if (server != null) {
			return server.getPlayerList().getPlayers();
		}

		return playerList;
	}

	public static boolean isOp(EntityPlayer player) {
		return player instanceof EntityPlayerMP && ((EntityPlayerMP) player).mcServer.getPlayerList().canSendCommands(player.getGameProfile());
	}
}
