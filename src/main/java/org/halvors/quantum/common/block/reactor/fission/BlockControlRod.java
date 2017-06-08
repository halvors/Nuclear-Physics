package org.halvors.quantum.common.block.reactor.fission;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.Reference;

public class BlockControlRod extends Block {
    public BlockControlRod() {
        super(Material.iron);

        setUnlocalizedName("controlRod");
        setTextureName(Reference.PREFIX + "controlRod");
        setCreativeTab(Quantum.getCreativeTab());
        setBlockBounds(0.3f, 0f, 0.3f, 0.7f, 1f, 0.7f);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}