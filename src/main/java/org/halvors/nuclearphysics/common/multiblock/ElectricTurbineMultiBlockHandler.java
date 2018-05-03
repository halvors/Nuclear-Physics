package org.halvors.nuclearphysics.common.multiblock;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.halvors.nuclearphysics.common.tile.reactor.TileElectricTurbine;

public class ElectricTurbineMultiBlockHandler extends MultiBlockHandler<TileElectricTurbine> {
    public ElectricTurbineMultiBlockHandler(final TileElectricTurbine tile) {
        super(tile);
    }

    @Override
    public TileElectricTurbine getWrapperAt(final BlockPos pos) {
        final TileEntity tile = self.getWorldObject().getTileEntity(pos);

        if (tile != null && wrapperClass.isAssignableFrom(tile.getClass())) {
            final TileElectricTurbine tileElectricTurbine = (TileElectricTurbine) tile;

            if (tileElectricTurbine.tier == self.tier) {
                return tileElectricTurbine;
            }
        }

        return null;
    }
}
