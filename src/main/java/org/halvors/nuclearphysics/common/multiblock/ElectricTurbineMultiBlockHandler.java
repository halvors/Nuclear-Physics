package org.halvors.nuclearphysics.common.multiblock;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.halvors.nuclearphysics.common.tile.reactor.TileElectricTurbine;

public class ElectricTurbineMultiBlockHandler extends MultiBlockHandler<TileElectricTurbine> {
    public ElectricTurbineMultiBlockHandler(TileElectricTurbine wrapper) {
        super(wrapper);
    }

    @Override
    public TileElectricTurbine getWrapperAt(BlockPos pos) {
        TileEntity tile = self.getWorldObject().getTileEntity(pos);

        if (tile != null && wrapperClass.isAssignableFrom(tile.getClass())) {
            TileElectricTurbine tileTurbine = (TileElectricTurbine) tile;

            if (tileTurbine.tier == self.tier) {
                return tileTurbine;
            }
        }

        return null;
    }
}
