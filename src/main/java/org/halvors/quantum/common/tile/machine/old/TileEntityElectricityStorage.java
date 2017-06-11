package org.halvors.quantum.common.tile.machine.old;

import cofh.api.energy.EnergyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import org.halvors.quantum.common.base.MachineType;
import org.halvors.quantum.common.base.tile.ITileNetworkable;

import java.util.List;

/**
 * This provides electricity storage to a TileEntity when extended.
 *
 * @author halvors
 */
public class TileEntityElectricityStorage extends TileEntityMachine implements ITileNetworkable {
	// The internal energy storage.
	protected final EnergyStorage storage;

	protected TileEntityElectricityStorage(MachineType machineType, int capacity) {
		super(machineType);

		storage = new EnergyStorage(capacity);
	}

	protected TileEntityElectricityStorage(MachineType machineType, int capacity, int maxReceive, int maxExtract) {
		this(machineType, capacity);

		storage.setMaxReceive(maxReceive);
		storage.setMaxExtract(maxExtract);
	}

	protected TileEntityElectricityStorage(MachineType machineType, int capacity, int maxTransfer) {
		this(machineType, capacity, maxTransfer, maxTransfer);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);

		storage.readFromNBT(nbtTagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);

		storage.writeToNBT(nbtTagCompound);
	}

	@Override
	public void handlePacketData(ByteBuf dataStream) throws Exception {
		super.handlePacketData(dataStream);

		storage.setEnergyStored(dataStream.readInt());
	}

	@Override
	public List<Object> getPacketData(List<Object> objects) {
		super.getPacketData(objects);

		objects.add(storage.getEnergyStored());

		return objects;
	}

	protected int getExtract() {
		return Math.min(storage.getMaxExtract(), storage.getEnergyStored());
	}

	public EnergyStorage getStorage() {
		return storage;
	}
}