package org.halvors.quantum.common.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import org.halvors.quantum.common.base.tile.ITileNetworkable;
import org.halvors.quantum.common.network.NetworkHandler;
import org.halvors.quantum.common.tile.TileEntity;
import org.halvors.quantum.common.util.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a packet that provides common information for all TileEntity packets, and is meant to be extended.
 *
 * @author halvors
 */
public class PacketTileEntity extends PacketLocation implements IMessage {
	private List<Object> objects;
	private ByteBuf storedBuffer = null;

	public PacketTileEntity() {

	}

	public PacketTileEntity(Location location, List<Object> objects) {
		super(location);

		this.objects = objects;
	}

	public <T extends TileEntity & ITileNetworkable> PacketTileEntity(T tile) {
		this(new Location(tile), tile.getPacketData(new ArrayList<>()));
	}

	@Override
	public void fromBytes(ByteBuf dataStream) {
		super.fromBytes(dataStream);

		storedBuffer = dataStream.copy();
	}

	@Override
	public void toBytes(ByteBuf dataStream) {
		super.toBytes(dataStream);

		NetworkHandler.writeObjects(objects, dataStream);
	}

	public static class PacketTileEntityMessage implements IMessageHandler<PacketTileEntity, IMessage> {
		@Override
		public IMessage onMessage(PacketTileEntity message, MessageContext messageContext) {
			TileEntity tileEntity = message.getLocation().getTileEntity(NetworkHandler.getWorld(messageContext));

			if (tileEntity != null && tileEntity instanceof ITileNetworkable) {
				ITileNetworkable tileNetworkable = (ITileNetworkable) tileEntity;

				try {
					tileNetworkable.handlePacketData(message.storedBuffer);
				} catch (Exception e) {
					e.printStackTrace();
				}

				message.storedBuffer.release();
			}

			return null;
		}
	}
}