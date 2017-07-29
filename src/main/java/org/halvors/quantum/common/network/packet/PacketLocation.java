package org.halvors.quantum.common.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.halvors.quantum.common.utility.location.Location;

/**
 * This is a packet that provides a location, and is meant to be extended.
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

	public PacketLocation(TileEntity tile) {
		this(new Location(tile));
	}

	@Override
	public void fromBytes(ByteBuf dataStream) {
		this.location = new Location(DimensionManager.getWorld(dataStream.readInt()), new BlockPos(dataStream.readInt(), dataStream.readInt(), dataStream.readInt()));
	}

	@Override
	public void toBytes(ByteBuf dataStream) {
		dataStream.writeInt(location.getWorld().provider.getDimension());
		dataStream.writeInt(location.getPos().getX());
		dataStream.writeInt(location.getPos().getY());
		dataStream.writeInt(location.getPos().getZ());
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