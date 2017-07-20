package org.halvors.quantum.common.block.machine;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.common.tile.machine.TileGasCentrifuge;

import javax.annotation.Nonnull;

public class BlockGasCentrifuge extends BlockMachineModel {
    public BlockGasCentrifuge() {
        super("gas_centrifuge");
    }

    @Override
    @Nonnull
    public TileEntity createNewTileEntity(@Nonnull World world, int metadata) {
        return new TileGasCentrifuge();
    }
}
