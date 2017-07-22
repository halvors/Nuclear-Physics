package org.halvors.quantum.common.block.machine;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.common.tile.particle.TileAccelerator;

import javax.annotation.Nonnull;

public class BlockAccelerator extends BlockMachine {
    public BlockAccelerator() {
        super("accelerator");
    }

    @Override
    @Nonnull
    public TileEntity createNewTileEntity(@Nonnull World world, int metadata) {
        return new TileAccelerator();
    }
}
