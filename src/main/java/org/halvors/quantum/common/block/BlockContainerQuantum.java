package org.halvors.quantum.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public abstract class BlockContainerQuantum extends BlockQuantum {
    public BlockContainerQuantum(String name, Material material) {
        super(name, material);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
}
