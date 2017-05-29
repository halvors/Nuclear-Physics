package org.halvors.atomicscience.common.tile.machine;

import cofh.api.energy.IEnergyReceiver;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.electrometrics.common.base.MachineType;

import java.util.EnumSet;

/**
 * When extended, this makes a TileEntity able to receive electricity.
 *
 * @author halvors
 */
public class TileEntityElectricityReceiver extends TileEntityElectricityStorage implements IEnergyReceiver {
	protected TileEntityElectricityReceiver(MachineType machineType, int capacity) {
		super(machineType, capacity);
	}

	protected TileEntityElectricityReceiver(MachineType machineType, int capacity, int maxReceive, int maxExtract) {
		super(machineType, capacity, maxReceive, maxExtract);
	}

	protected TileEntityElectricityReceiver(MachineType machineType, int capacity, int maxTransfer) {
		super(machineType, capacity, maxTransfer);
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		if (getReceivingDirections().contains(from)) {
			return storage.receiveEnergy(maxReceive, simulate);
		}

		return 0;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return storage.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return getReceivingDirections().contains(from);
	}

	protected EnumSet<ForgeDirection> getReceivingDirections() {
		EnumSet<ForgeDirection> directions = EnumSet.allOf(ForgeDirection.class);
		directions.remove(ForgeDirection.UNKNOWN);

		return directions;
	}
}