package org.halvors.nuclearphysics.common.tile;

import net.minecraft.tileentity.TileEntity;
import org.halvors.nuclearphysics.api.BlockPos;

public class TileBase extends TileEntity {
    protected final BlockPos pos;

    public TileBase() {
        this.pos = new BlockPos(xCoord, yCoord, zCoord);
    }

    public BlockPos getPos() {
        return pos;
    }
}
