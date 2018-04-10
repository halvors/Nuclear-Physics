package org.halvors.nuclearphysics.common.tile.particle;

import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.common.capabilities.energy.EnergyStorage;
import org.halvors.nuclearphysics.common.event.handler.FulminationEventHandler;
import org.halvors.nuclearphysics.common.tile.TileGenerator;

import java.util.EnumSet;

public class TileFulminationGenerator extends TileGenerator {
    public TileFulminationGenerator() {
        energyStorage = new EnergyStorage(40000);
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
    public void updateEntity() {
        super.updateEntity();

        if (!worldObj.isRemote) {
            // Slowly lose energy.
            energyStorage.extractEnergy(1, false);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public EnumSet<ForgeDirection> getExtractingDirections() {
        return EnumSet.allOf(ForgeDirection.class);
    }
}
