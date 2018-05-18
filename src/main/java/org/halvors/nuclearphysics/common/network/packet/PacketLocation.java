package org.halvors.nuclearphysics.common.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import org.halvors.nuclearphysics.api.BlockPos;

/**
 * This is a packet that provides a location, and is meant to be extended.
 */
public abstract class PacketLocation implements IMessage {
	private BlockPos pos;

	public PacketLocation() {

	}

	public PacketLocation(final BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void fromBytes(final ByteBuf dataStream) {
		pos = new BlockPos(dataStream.readInt(), dataStream.readInt(), dataStream.readInt());
	}

	@Override
	public void toBytes(final ByteBuf dataStream) {
		dataStream.writeInt(pos.getX());
		dataStream.writeInt(pos.getY());
		dataStream.writeInt(pos.getZ());
	}

	public BlockPos getPos() {
		return pos;
	}
}