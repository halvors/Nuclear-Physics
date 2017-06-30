package org.halvors.quantum.common.tile.particle;

import cofh.api.energy.EnergyStorage;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import org.halvors.quantum.common.tile.TileElectric;

import java.util.EnumSet;

public class TileFulmination extends TileElectric implements ITickable {
    private static final long energyCapacity = 10000000000000L;

    public TileFulmination() {
        energyStorage = new EnergyStorage((int) energyCapacity);
    }

    @Override
    public void invalidate() {
        FulminationHandler.INSTANCE.unregister(this);
    }

    @Override
    public void update() {
        if (world.getWorldTime() == 0) {
            FulminationHandler.INSTANCE.register(this);
        }

        produce();

        // Slowly lose energy.
        energyStorage.extractEnergy(1, false);
    }

    @Override
    public EnumSet<EnumFacing> getExtractingDirections() {
        return EnumSet.allOf(EnumFacing.class);
    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        return 0;
    }
}
