package org.halvors.quantum.common.multiblock;

import net.minecraft.tileentity.TileEntity;
import org.halvors.quantum.common.tile.reactor.TileTurbine;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.lib.multiblock.MultiBlockHandler;

public class TurbineMultiBlockHandler extends MultiBlockHandler<TileTurbine> {
    public TurbineMultiBlockHandler(TileTurbine wrapper) {
        super(wrapper);
    }

    @Override
    public TileTurbine getWrapperAt(Vector3 position) {
        TileEntity tileEntity = position.getTileEntity(self.getWorld());

        if (tileEntity != null && wrapperClass.isAssignableFrom(tileEntity.getClass())) {
            TileTurbine tileTurbine = (TileTurbine) tileEntity;

            if (tileTurbine.getDirection() == self.getDirection() && ((TileTurbine) tileEntity).tier == self.tier) {
                return tileTurbine;
            }
        }

        return null;
    }
}
