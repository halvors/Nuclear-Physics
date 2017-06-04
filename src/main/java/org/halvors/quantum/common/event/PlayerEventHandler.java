package org.halvors.quantum.common.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.network.NetworkHandler;
import org.halvors.quantum.common.network.packet.PacketConfiguration;

/**
 * This is the event handler that handles player events.
 *
 * @author halvors
 */
public class PlayerEventHandler {
	@SubscribeEvent
	public void onPlayerLoginEvent(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		World world = player.worldObj;

		if (!world.isRemote) {
			NetworkHandler.sendTo(new PacketConfiguration(), (EntityPlayerMP) player);

			Quantum.getLogger().info("Sent configuration to '" + player.getDisplayName() + "'.");
		}
	}
}
