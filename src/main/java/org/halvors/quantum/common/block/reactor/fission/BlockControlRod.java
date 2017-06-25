package org.halvors.quantum.common.block.reactor.fission;

import net.minecraft.block.material.Material;
import org.halvors.quantum.common.block.BlockTextured;

public class BlockControlRod extends BlockTextured {
    public BlockControlRod() {
        super("controlRod", Material.iron);

        setBlockBounds(0.3F, 0, 0.3F, 0.7F, 1, 0.7F);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}