package org.halvors.quantum.atomic.common.multiblock;

import net.minecraft.tileentity.TileEntity;
import org.halvors.quantum.atomic.common.tile.reactor.TileElectricTurbine;
import org.halvors.quantum.atomic.common.utility.location.Position;

public class ElectricTurbineMultiBlockHandler extends MultiBlockHandler<TileElectricTurbine> {
    public ElectricTurbineMultiBlockHandler(TileElectricTurbine wrapper) {
        super(wrapper);
    }

    @Override
    public TileElectricTurbine getWrapperAt(Position position) {
        TileEntity tile = position.getTileEntity(self.getWorldObject());

        if (tile != null && wrapperClass.isAssignableFrom(tile.getClass())) {
            TileElectricTurbine tileTurbine = (TileElectricTurbine) tile;

            if (tileTurbine.tier == self.tier) {
                return tileTurbine;
            }
        }

        return null;
    }
}
