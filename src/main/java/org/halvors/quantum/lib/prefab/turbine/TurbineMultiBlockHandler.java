package org.halvors.quantum.lib.prefab.turbine;

import net.minecraft.tileentity.TileEntity;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.lib.multiblock.MultiBlockHandler;

public class TurbineMultiBlockHandler extends MultiBlockHandler<TileTurbine> {
    public TurbineMultiBlockHandler(TileTurbine wrapper) {
        super(wrapper);
    }

    public TileTurbine getWrapperAt(Vector3 position) {
        TileEntity tileEntity = position.getTileEntity(self.getWorld());

        if (tileEntity != null && wrapperClass.isAssignableFrom(tileEntity.getClass())) {
            if (((TileTurbine) tileEntity).getDirection() == self.getDirection() && ((TileTurbine) tileEntity).tier == self.tier) {
                return (TileTurbine) tileEntity;
            }
        }

        return null;
    }
}
