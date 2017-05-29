package org.halvors.atomicscience.common.tile.machine;

import org.halvors.electrometrics.common.ConfigurationManager.General;
import org.halvors.electrometrics.common.base.MachineType;
import org.halvors.electrometrics.common.tile.TileEntityRotatable;
import org.halvors.electrometrics.common.util.location.Location;

public class TileEntityMachine extends TileEntityRotatable {
	private final MachineType machineType;

	protected TileEntityMachine(MachineType machineType) {
		super(machineType.getLocalizedName());

		this.machineType = machineType;
	}

    @Override
    public void updateEntity() {
        // Remove disabled blocks.
        if (!worldObj.isRemote && General.destroyDisabledBlocks) {
            MachineType machineType = MachineType.getType(getBlockType(), getBlockMetadata());

            if (machineType != null && !machineType.isEnabled()) {
                org.halvors.electrometrics.AtomicScience.getLogger().info("Destroying machine of type '" + machineType.getLocalizedName() + "' at " + new Location(this) + " as according to configuration.");
                worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            }
        }
    }

	public MachineType getMachineType() {
		return machineType;
	}
}
