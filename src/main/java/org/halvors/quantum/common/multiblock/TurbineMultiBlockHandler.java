package org.halvors.quantum.common.multiblock;

import net.minecraft.tileentity.TileEntity;
import org.halvors.quantum.common.tile.reactor.TileElectricTurbine;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.lib.multiblock.MultiBlockHandler;

public class TurbineMultiBlockHandler extends MultiBlockHandler<TileElectricTurbine> {
    public TurbineMultiBlockHandler(TileElectricTurbine wrapper) {
        super(wrapper);
    }

    @Override
    public TileElectricTurbine getWrapperAt(Vector3 position) {
        TileEntity tileEntity = position.getTileEntity(self.getWorld());

        if (tileEntity != null && wrapperClass.isAssignableFrom(tileEntity.getClass())) {
            TileElectricTurbine tileTurbine = (TileElectricTurbine) tileEntity;

            if (tileTurbine.getDirection() == self.getDirection() && ((TileElectricTurbine) tileEntity).tier == self.tier) {
                return tileTurbine;
            }
        }

        return null;
    }
}
