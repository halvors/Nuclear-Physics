package org.halvors.quantum.common.multiblock;

import net.minecraft.tileentity.TileEntity;
import org.halvors.quantum.common.tile.reactor.TileElectricTurbine;
import org.halvors.quantum.common.utility.location.Position;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

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
