package org.halvors.nuclearphysics.common.block;

import net.minecraft.block.material.Material;

public class BlockContainerBase extends BlockBase {
    public BlockContainerBase(final String name, final Material material) {
        super(name, material);
    }

    @Override
    public boolean hasTileEntity(final int metadata) {
        return true;
    }
}
