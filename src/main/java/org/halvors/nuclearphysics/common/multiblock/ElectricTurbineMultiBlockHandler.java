package org.halvors.nuclearphysics.common.multiblock;

import net.minecraft.tileentity.TileEntity;
import org.halvors.nuclearphysics.common.tile.reactor.TileElectricTurbine;
import org.halvors.nuclearphysics.common.type.Position;

public class ElectricTurbineMultiBlockHandler extends MultiBlockHandler<TileElectricTurbine> {
    public ElectricTurbineMultiBlockHandler(final TileElectricTurbine tile) {
        super(tile);
    }

    @Override
    public TileElectricTurbine getWrapperAt(final Position position) {
        final TileEntity tile = position.getTileEntity(self.getWorldObject());

        if (tile != null && wrapperClass.isAssignableFrom(tile.getClass())) {
            final TileElectricTurbine tileElectricTurbine = (TileElectricTurbine) tile;

            if (tileElectricTurbine.tier == self.tier) {
                return tileElectricTurbine;
            }
        }

        return null;
    }
}
