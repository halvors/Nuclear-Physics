package org.halvors.quantum.common.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import org.halvors.quantum.common.utility.location.Location;

/**
 * This is a packet that provides a location, and is meant to be extended.
 *
 * @author halvors
 */
public class PacketLocation implements IMessage {
	private Location location;

	public PacketLocation() {

	}

	public PacketLocation(Location location) {
		this.location = location;
	}

	public PacketLocation(Entity entity) {
		this(new Location(entity));
	}

	public PacketLocation(TileEntity tileEntity) {
		this(new Location(tileEntity));
	}

	@Override
	public void fromBytes(ByteBuf dataStream) {
		this.location = new Location(dataStream.readInt(), dataStream.readInt(), dataStream.readInt(), dataStream.readInt());
	}

	@Override
	public void toBytes(ByteBuf dataStream) {
		dataStream.writeInt(location.getDimensionId());
		dataStream.writeInt(location.getX());
		dataStream.writeInt(location.getY());
		dataStream.writeInt(location.getZ());
	}

	public Location getLocation() {
		return location;
	}

	public static class PacketLocationMessage implements IMessageHandler<PacketLocation, IMessage> {
		@Override
		public IMessage onMessage(PacketLocation message, MessageContext messageContext) {
			return null;
		}
	}
}