package org.halvors.quantum.common.tile.reactor.fusion;

import net.minecraft.tileentity.TileEntity;
import org.halvors.quantum.api.IElectromagnet;

public class TileElectromagnet extends TileEntity implements IElectromagnet {
    public TileElectromagnet() {

    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public boolean isRunning() {
        return true;
    }
}