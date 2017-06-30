package org.halvors.quantum.common.block;

import net.minecraft.block.material.Material;

public class BlockTextured extends BlockQuantum {
    public BlockTextured(String name, Material material) {
        super(name, material);

        //setTextureName(Reference.PREFIX + name);
    }
}
