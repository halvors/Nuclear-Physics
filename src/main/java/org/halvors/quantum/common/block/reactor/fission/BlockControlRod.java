package org.halvors.quantum.common.block.reactor.fission;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.block.BlockTextured;

public class BlockControlRod extends BlockTextured {
    public BlockControlRod() {
        super("controlRod", Material.iron);

        setBlockBounds(0.3f, 0f, 0.3f, 0.7f, 1f, 0.7f);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}