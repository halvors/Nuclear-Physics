package org.halvors.quantum.common.block.machine;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.common.tile.machine.TileCentrifuge;

public class BlockCentrifuge extends BlockMachine {
    public BlockCentrifuge() {
        super("centrifuge");
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileCentrifuge();
    }
}
