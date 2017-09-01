package org.halvors.nuclearphysics.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public abstract class BlockContainerNuclearPhysics extends BlockNuclearPhysics {
    public BlockContainerNuclearPhysics(String name, Material material) {
        super(name, material);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
}
