package org.halvors.nuclearphysics.common.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import org.halvors.nuclearphysics.common.type.Position;

/**
 * This is a packet that provides a location, and is meant to be extended.
 */
public abstract class PacketLocation implements IMessage {
	private int x;
	private int y;
	private int z;

	public PacketLocation() {

	}

	public PacketLocation(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public PacketLocation(Position pos) {
		this(pos.getIntX(), pos.getIntY(), pos.getIntZ());
	}

	@Override
	public void fromBytes(ByteBuf dataStream) {
		this.x = dataStream.readInt();
		this.y = dataStream.readInt();
		this.z = dataStream.readInt();
	}

	@Override
	public void toBytes(ByteBuf dataStream) {
		dataStream.writeInt(x);
		dataStream.writeInt(y);
		dataStream.writeInt(z);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}
}