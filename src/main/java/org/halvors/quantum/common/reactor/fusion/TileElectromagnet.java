package org.halvors.quantum.common.reactor.fusion;

import net.minecraft.tileentity.TileEntity;
import org.halvors.quantum.common.block.IElectromagnet;

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