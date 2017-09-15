package org.halvors.nuclearphysics.common.tile.particle;

import net.minecraft.util.EnumFacing;
import org.halvors.nuclearphysics.common.capabilities.energy.EnergyStorage;
import org.halvors.nuclearphysics.common.event.handler.FulminationEventHandler;
import org.halvors.nuclearphysics.common.tile.TileGenerator;

import java.util.EnumSet;

public class TileFulmination extends TileGenerator {
    public TileFulmination() {
        energyStorage = new EnergyStorage((int) 10000000000000L);
    }

    @Override
    public void validate() {
        super.validate();

        FulminationEventHandler.register(this);
    }

    @Override
    public void invalidate() {
        super.invalidate();

        FulminationEventHandler.unregister(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void update() {
        super.update();

        if (!world.isRemote) {
            // Slowly lose energy.
            energyStorage.extractEnergy(1, false);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public EnumSet<EnumFacing> getExtractingDirections() {
        return EnumSet.allOf(EnumFacing.class);
    }
}
