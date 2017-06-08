package org.halvors.quantum.common.tile.machine.old;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.common.base.MachineType;
import org.halvors.quantum.common.utility.MachineUtils;

import java.util.EnumSet;

/**
 * When extended, this makes a TileEntity able to provide electricity.
 *
 * @author halvors
 */
public class TileEntityElectricityProvider extends TileEntityElectricMachine implements IEnergyProvider {
	protected TileEntityElectricityProvider(MachineType machineType, int capacity) {
		super(machineType, capacity);
	}

	protected TileEntityElectricityProvider(MachineType machineType, int capacity, int maxReceive, int maxExtract) {
		super(machineType, capacity, maxReceive, maxExtract);
	}

	protected TileEntityElectricityProvider(MachineType machineType, int capacity, int maxTransfer) {
		super(machineType, capacity, maxTransfer);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (!worldObj.isRemote) {
			distributeEnergy();
		}
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return getReceivingDirections().contains(from) || getExtractingDirections().contains(from);
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		if (getExtractingDirections().contains(from)) {
			return storage.extractEnergy(maxExtract, simulate);
		}

		return 0;
	}

	protected EnumSet<ForgeDirection> getExtractingDirections() {
		return EnumSet.noneOf(ForgeDirection.class);
	}

	/**
	 * Transfer energy to any blocks demanding energy that are connected to
	 * this one.
	 */
	protected void distributeEnergy() {
		if (MachineUtils.canFunction(this)) {
			for (ForgeDirection direction : getExtractingDirections()) {
				TileEntity tileEntity = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);

				if (tileEntity instanceof IEnergyReceiver) {
					IEnergyReceiver receiver = (IEnergyReceiver) tileEntity;
					int actualEnergyAmount = extractEnergy(direction, getExtract(), true);

					if (actualEnergyAmount > 0) {
						extractEnergy(direction, receiver.receiveEnergy(direction.getOpposite(), actualEnergyAmount, false), false);
					}
				}
			}
		}
	}
}
