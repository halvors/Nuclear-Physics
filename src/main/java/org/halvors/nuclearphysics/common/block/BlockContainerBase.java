package org.halvors.nuclearphysics.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class BlockContainerBase extends BlockBase {
    public BlockContainerBase(final String name, final Material material) {
        super(name, material);
    }

    @Override
    public boolean hasTileEntity(final IBlockState state) {
        return true;
    }
}
