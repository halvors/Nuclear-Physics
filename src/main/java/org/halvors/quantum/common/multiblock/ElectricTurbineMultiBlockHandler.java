package org.halvors.quantum.common.multiblock;

import net.minecraft.tileentity.TileEntity;
import org.halvors.quantum.common.tile.reactor.TileElectricTurbine;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

public class ElectricTurbineMultiBlockHandler extends MultiBlockHandler<TileElectricTurbine> {
    public ElectricTurbineMultiBlockHandler(TileElectricTurbine wrapper) {
        super(wrapper);
    }

    @Override
    public TileElectricTurbine getWrapperAt(Vector3 position) {
        TileEntity tileEntity = position.getTileEntity(self.getWorld());

        if (tileEntity != null && wrapperClass.isAssignableFrom(tileEntity.getClass())) {
            TileElectricTurbine tileTurbine = (TileElectricTurbine) tileEntity;

            if (tileTurbine.tier == self.tier) {
                return tileTurbine;
            }
        }

        return null;
    }
}
