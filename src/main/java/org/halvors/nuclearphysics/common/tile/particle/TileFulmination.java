package org.halvors.nuclearphysics.common.tile.particle;

import net.minecraft.util.EnumFacing;
import org.halvors.nuclearphysics.common.event.handler.FulminationHandler;
import org.halvors.nuclearphysics.common.tile.TileGenerator;

import java.util.EnumSet;

public class TileFulmination extends TileGenerator {
    public TileFulmination() {
        super((int) 10000000000000L);
    }

    @Override
    public void invalidate() {
        FulminationHandler.INSTANCE.unregister(this);
    }

    @Override
    public void update() {
        super.update();

        if (world.getWorldTime() == 0) {
            FulminationHandler.INSTANCE.register(this);
        }

        // TODO: Fix generation output.
        //generateEnergy();

        // Slowly lose energy.
        energyStorage.extractEnergy(1, false);
    }

    @Override
    public EnumSet<EnumFacing> getExtractingDirections() {
        return EnumSet.allOf(EnumFacing.class);
    }
}
