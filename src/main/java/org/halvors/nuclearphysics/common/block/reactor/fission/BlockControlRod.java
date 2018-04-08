package org.halvors.nuclearphysics.common.block.reactor.fission;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import org.halvors.nuclearphysics.common.block.BlockBase;

public class BlockControlRod extends BlockBase {
    public BlockControlRod() {
        super("control_rod", Material.iron);

        setHardness(0.6F);
        setBlockBounds(0.3125F, 0, 0.3125F, 0.6875F, 1, 0.6875F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderAsNormalBlock() {
        return false;
    }
}