package org.halvors.quantum.common.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.halvors.quantum.common.utility.location.Location;

/**
 * This is a packet that provides a location, and is meant to be extended.
 */
public abstract class PacketLocation implements IMessage {
	private BlockPos pos;

	public PacketLocation() {

	}

	public PacketLocation(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf dataStream) {
		this.pos = new BlockPos(dataStream.readInt(), dataStream.readInt(), dataStream.readInt());
	}

	@Override
	public void toBytes(ByteBuf dataStream) {
		dataStream.writeInt(pos.getX());
		dataStream.writeInt(pos.getY());
		dataStream.writeInt(pos.getZ());
	}

	public BlockPos getPos() {
		return pos;
	}
}