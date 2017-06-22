package org.halvors.quantum.common.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.tileentity.TileEntity;
import org.halvors.quantum.common.base.tile.ITileNetworkable;
import org.halvors.quantum.common.network.PacketHandler;

public class PacketRequestData extends PacketLocation implements IMessage {
	public PacketRequestData() {

	}

	public <T extends TileEntity & ITileNetworkable> PacketRequestData(T tile) {
		super(tile);
	}

	public static class PacketRequestDataMessage implements IMessageHandler<PacketRequestData, IMessage>{
		@Override
		public IMessage onMessage(PacketRequestData message, MessageContext messageContext) {
			return onPacketRequestDataMessage(message, messageContext);
		}

		@SuppressWarnings("unchecked")
		public <T extends TileEntity & ITileNetworkable> IMessage onPacketRequestDataMessage(PacketRequestData message, MessageContext messageContext) {
			TileEntity tileEntity = message.getLocation().getTileEntity(PacketHandler.getWorld(messageContext));

			if (tileEntity != null && tileEntity instanceof ITileNetworkable) {
				return new PacketTileEntity((T) tileEntity);
			}

			return null;
		}
	}
}
