package org.halvors.atomicscience.common.tile.machine;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import org.halvors.electrometrics.common.base.MachineType;
import org.halvors.electrometrics.common.base.RedstoneControlType;
import org.halvors.electrometrics.common.base.tile.ITileNetworkable;
import org.halvors.electrometrics.common.base.tile.ITileRedstoneControl;
import org.halvors.electrometrics.common.network.NetworkHandler;
import org.halvors.electrometrics.common.network.packet.PacketTileEntity;

import java.util.List;

/**
 * This is a basic TileEntity that is meant to be extended by other TileEntities.
 *
 * @author halvors
 */
public class TileEntityElectricMachine extends TileEntityElectricityReceiver implements ITileNetworkable, ITileRedstoneControl {
	// The current RedstoneControlType of this TileEntity.
	private RedstoneControlType redstoneControlType = RedstoneControlType.DISABLED;

	// The current and past redstone state.
	protected boolean isPowered;
	protected boolean wasPowered;

	protected TileEntityElectricMachine(MachineType machineType, int capacity) {
		super(machineType, capacity);
	}

	protected TileEntityElectricMachine(MachineType machineType, int capacity, int maxReceive, int maxExtract) {
		super(machineType, capacity, maxReceive, maxExtract);
	}

	protected TileEntityElectricMachine(MachineType machineType, int capacity, int maxTransfer) {
		super(machineType, capacity, maxTransfer);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		// Update wasPowered to the current isPowered.
		wasPowered = isPowered;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);

		redstoneControlType = RedstoneControlType.values()[nbtTagCompound.getInteger("redstoneControlType")];
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);

		nbtTagCompound.setInteger("redstoneControlType", redstoneControlType.ordinal());
	}

	@Override
	public void handlePacketData(ByteBuf dataStream) throws Exception {
		super.handlePacketData(dataStream);

		redstoneControlType = RedstoneControlType.values()[dataStream.readInt()];
	}

	@Override
	public List<Object> getPacketData(List<Object> objects) {
		super.getPacketData(objects);

		objects.add(redstoneControlType.ordinal());

		return objects;
	}

	@Override
	public RedstoneControlType getControlType() {
		return redstoneControlType;
	}

	@Override
	public void setControlType(RedstoneControlType redstoneControlType) {
		this.redstoneControlType = redstoneControlType;
	}

	@Override
	public boolean isPowered() {
		return isPowered;
	}

	@Override
	public void setPowered(boolean isPowered) {
		this.isPowered = isPowered;
	}

	@Override
	public boolean wasPowered() {
		return wasPowered;
	}

	@Override
	public boolean canPulse() {
		return false;
	}

	public void onNeighborChange() {
		if (!worldObj.isRemote) {
			boolean redstonePower = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);

			if (isPowered != redstonePower) {
				isPowered = redstonePower;

				NetworkHandler.sendToReceivers(new PacketTileEntity(this), this);
			}
		}
	}
}
