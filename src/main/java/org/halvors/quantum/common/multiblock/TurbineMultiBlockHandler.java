package org.halvors.quantum.common.multiblock;

import net.minecraft.tileentity.TileEntity;
import org.halvors.quantum.common.tile.reactor.TileElectricTurbineXY;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.lib.multiblock.MultiBlockHandler;

public class TurbineMultiBlockHandler extends MultiBlockHandler<TileElectricTurbineXY> {
    public TurbineMultiBlockHandler(TileElectricTurbineXY wrapper) {
        super(wrapper);
    }

    @Override
    public TileElectricTurbineXY getWrapperAt(Vector3 position) {
        TileEntity tileEntity = position.getTileEntity(self.getWorld());

        if (tileEntity != null && wrapperClass.isAssignableFrom(tileEntity.getClass())) {
            TileElectricTurbineXY tileTurbine = (TileElectricTurbineXY) tileEntity;

            if (tileTurbine.getDirection() == self.getDirection() && ((TileElectricTurbineXY) tileEntity).tier == self.tier) {
                return tileTurbine;
            }
        }

        return null;
    }
}
