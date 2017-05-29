package org.halvors.atomicscience.common.block;

import net.minecraft.block.material.Material;

public abstract class BlockMetadata extends Block {
    protected BlockMetadata(String name, Material material) {
        super(name, material);
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }
}
