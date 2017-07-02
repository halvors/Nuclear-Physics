package org.halvors.quantum.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.network.packet.PacketConfiguration;

/**
 * This is the event handler that handles player events.
 *
 * @author halvors
 */
public class PlayerEventHandler {
	@SubscribeEvent
	public void onPlayerLoginEvent(PlayerEvent.PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		World world = player.getEntityWorld();

		if (!world.isRemote) {
			Quantum.getPacketHandler().sendTo(new PacketConfiguration(), (EntityPlayerMP) player);

			Quantum.getLogger().info("Sent configuration to '" + player.getDisplayName() + "'.");
		}
	}
}
