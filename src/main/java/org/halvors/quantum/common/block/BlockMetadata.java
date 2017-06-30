package org.halvors.quantum.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public abstract class BlockMetadata extends BlockTextured {
    protected BlockMetadata(String name, Material material) {
        super(name, material);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getBlock().getMetaFromState(state);
    }
}
