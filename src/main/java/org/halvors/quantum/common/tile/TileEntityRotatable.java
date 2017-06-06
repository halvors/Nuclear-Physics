package org.halvors.quantum.common.tile;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import org.halvors.quantum.common.base.tile.ITileNetworkable;
import org.halvors.quantum.common.base.tile.ITileRotatable;
import org.halvors.quantum.common.network.NetworkHandler;
import org.halvors.quantum.common.network.packet.PacketTileEntity;
import org.halvors.quantum.common.utility.location.Location;

import java.util.List;

public class TileEntityRotatable extends TileEntity implements ITileNetworkable, ITileRotatable {
	// The direction this TileEntity's block is facing.
	protected int facing;

	protected TileEntityRotatable() {

	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);

		facing = nbtTagCompound.getInteger("facing");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);

		nbtTagCompound.setInteger("facing", facing);
	}

	@Override
	public Packet getDescriptionPacket() {
		return NetworkHandler.getPacketFrom(new PacketTileEntity(this));
	}

	@Override
	public void handlePacketData(Location location, ByteBuf dataStream) throws Exception {
		facing = dataStream.readInt();

		// Re-render the block.
		worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);

		// Update potentially connected redstone blocks.
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
	}

	@Override
	public List<Object> getPacketData(List<Object> objects) {
		objects.add(facing);

		return objects;
	}

	@Override
	public boolean canSetFacing(int facing) {
		return true;
	}

	@Override
	public int getFacing() {
		return facing;
	}

	@Override
	public void setFacing(int facing) {
		if (canSetFacing(facing)) {
			this.facing = facing;
		}

		if (!worldObj.isRemote) {
			NetworkHandler.sendToReceivers(new PacketTileEntity(this), this);
		}
	}
}