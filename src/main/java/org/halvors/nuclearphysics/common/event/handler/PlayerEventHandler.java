package org.halvors.nuclearphysics.common.event.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.network.packet.PacketConfiguration;

@EventBusSubscriber
public class PlayerEventHandler {
	@SubscribeEvent
	public static void onPlayerLoggedInEvent(final PlayerLoggedInEvent event) {
		final EntityPlayer player = event.player;
		final World world = player.getEntityWorld();

		if (!world.isRemote) {
			NuclearPhysics.getPacketHandler().sendTo(new PacketConfiguration(), (EntityPlayerMP) player);
			NuclearPhysics.getLogger().info("Sent configuration to '" + player.getDisplayName() + "'.");
		}
	}
}
