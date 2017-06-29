package org.halvors.quantum.common.tile.particle;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.common.tile.TileElectric;

import java.util.EnumSet;

public class TileFulmination extends TileElectric implements IEnergyReceiver {
    private static final long energyCapacity = 10000000000000L;

    public TileFulmination() {
        energyStorage = new EnergyStorage((int) energyCapacity);
    }

    @Override
    public void invalidate() {
        FulminationHandler.INSTANCE.unregister(this);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (worldObj.getWorldTime() == 0) {
            FulminationHandler.INSTANCE.register(this);
        }

        produce();

        // Slowly lose energy.
        energyStorage.extractEnergy(1, false);
    }

    @Override
    public EnumSet<ForgeDirection> getExtractingDirections() {
        return EnumSet.allOf(ForgeDirection.class);
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        return 0;
    }
}
