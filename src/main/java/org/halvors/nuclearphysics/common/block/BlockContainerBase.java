package org.halvors.nuclearphysics.common.block;

import net.minecraft.block.material.Material;

public class BlockContainerBase extends BlockBase {
    public BlockContainerBase(String name, Material material) {
        super(name, material);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }
}
