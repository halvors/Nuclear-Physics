package org.halvors.nuclearphysics.common.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import org.halvors.nuclearphysics.common.ConfigurationManager;

/**
 * This is a packet that synchronizes the configuration from the server to the client.
 */
public class PacketConfiguration implements IMessage {
	public PacketConfiguration() {

	}

	@Override
	public void fromBytes(final ByteBuf dataStream) {
		ConfigurationManager.readConfiguration(dataStream);
	}

	@Override
	public void toBytes(final ByteBuf dataStream) {
		ConfigurationManager.writeConfiguration(dataStream);
	}

	public static class PacketConfigurationMessage implements IMessageHandler<PacketConfiguration, IMessage> {
		@Override
		public IMessage onMessage(final PacketConfiguration message, final MessageContext messageContext) {
			return null;
		}
	}
}
