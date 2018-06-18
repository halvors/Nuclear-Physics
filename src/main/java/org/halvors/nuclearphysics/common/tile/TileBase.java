package org.halvors.nuclearphysics.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.common.NuclearPhysics;

public class TileBase extends TileEntity {
    protected BlockPos pos = BlockPos.ORIGIN;

    public TileBase() {

    }

    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);

        // Initialize BlockPos object with the actual coordinates.
        pos = new BlockPos(xCoord, yCoord, zCoord);
    }

    public BlockPos getPos() {
        return pos;
    }
}
