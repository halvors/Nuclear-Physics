package org.halvors.nuclearphysics.common.utility;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.NuclearPhysics;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtility {
	public static List<ServerPlayerEntity> getPlayers() {
		final List<ServerPlayerEntity> playerList = new ArrayList<>();
		final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

		if (server != null) {
			return server.getPlayerList().getPlayers();
		}

		return playerList;
	}

	public static boolean isOp(final PlayerEntity player) {
		return player instanceof ServerPlayerEntity && ((ServerPlayerEntity) player).server.getPlayerList().canSendCommands(player.getGameProfile());
	}

	public static void openGui(final PlayerEntity player, final IWorld world, final BlockPos pos) {
		player.openGui(NuclearPhysics.getInstance(), 0, (World) world, pos.getX(), pos.getY(), pos.getZ());
	}
}
