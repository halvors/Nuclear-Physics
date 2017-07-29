package org.halvors.quantum.common.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.halvors.quantum.common.network.PacketHandler;
import org.halvors.quantum.common.tile.ITileNetwork;
import org.halvors.quantum.common.utility.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * This packet i used by tile tile entities to send custom information from server to client.
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

	public <T extends TileEntity & ITileNetwork> PacketTileEntity(T tile) {
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

		PacketHandler.writeObjects(objects, dataStream);
	}

	public static class PacketTileEntityMessage implements IMessageHandler<PacketTileEntity, IMessage> {
		@Override
		public IMessage onMessage(PacketTileEntity message, MessageContext messageContext) {
			TileEntity tileEntity = message.getLocation().getTileEntity();

			if (tileEntity != null && tileEntity instanceof ITileNetwork) {
				ITileNetwork tileNetworkable = (ITileNetwork) tileEntity;

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