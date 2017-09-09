package org.halvors.nuclearphysics.common.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.halvors.nuclearphysics.common.ConfigurationManager;

/**
 * This is a packet that synchronizes the configuration from the server to the client.
 */
public class PacketConfiguration implements IMessage {
	public PacketConfiguration() {

	}

	@Override
	public void fromBytes(ByteBuf dataStream) {
		ConfigurationManager.readConfiguration(dataStream);
	}

	@Override
	public void toBytes(ByteBuf dataStream) {
		ConfigurationManager.writeConfiguration(dataStream);
	}

	public static class PacketConfigurationMessage implements IMessageHandler<PacketConfiguration, IMessage> {
		@Override
		public IMessage onMessage(PacketConfiguration message, MessageContext messageContext) {
			return null;
		}
	}
}
