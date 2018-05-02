package org.halvors.nuclearphysics.common.block.reactor.fission;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.common.block.BlockBase;

import javax.annotation.Nonnull;

public class BlockControlRod extends BlockBase {
    public BlockControlRod() {
        super("control_rod", Material.IRON);

        setHardness(0.6F);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess access, final BlockPos pos) {
        return new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, 1, 0.6875);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
}