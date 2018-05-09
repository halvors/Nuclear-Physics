package org.halvors.nuclearphysics.common.event.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.network.packet.PacketConfiguration;

public class PlayerEventHandler {
	@SubscribeEvent
	public void onPlayerLoggedInEvent(final PlayerLoggedInEvent event) {
		final EntityPlayer player = event.player;
		final World world = player.getEntityWorld();

		if (!world.isRemote) {
			NuclearPhysics.getPacketHandler().sendTo(new PacketConfiguration(), (EntityPlayerMP) player);
			NuclearPhysics.getLogger().info("Sent configuration to '" + player.getDisplayName() + "'.");
		}
	}
}
