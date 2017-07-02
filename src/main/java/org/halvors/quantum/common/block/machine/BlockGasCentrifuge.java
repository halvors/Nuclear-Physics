package org.halvors.quantum.common.block.machine;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.common.tile.machine.TileGasCentrifuge;

public class BlockGasCentrifuge extends BlockMachine {
    public BlockGasCentrifuge() {
        super("gas_centrifuge");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileGasCentrifuge();
    }
}
