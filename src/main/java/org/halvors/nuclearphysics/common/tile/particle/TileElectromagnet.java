package org.halvors.nuclearphysics.common.tile.particle;

import net.minecraft.tileentity.TileEntity;
import org.halvors.nuclearphysics.api.tile.IElectromagnet;

public class TileElectromagnet extends TileEntity implements IElectromagnet {
    public TileElectromagnet() {

    }

    @Override
    public boolean isRunning() {
        return true;
    }
}