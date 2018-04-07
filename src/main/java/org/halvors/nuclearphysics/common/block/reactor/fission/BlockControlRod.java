package org.halvors.nuclearphysics.common.block.reactor.fission;

import net.minecraft.block.material.Material;
import org.halvors.nuclearphysics.common.block.BlockBase;

public class BlockControlRod extends BlockBase {
    public BlockControlRod() {
        super("control_rod", Material.iron);

        setHardness(0.6F);
        setBlockBounds(0.3125F, 0, 0.3125F, 0.6875F, 1, 0.6875F);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}