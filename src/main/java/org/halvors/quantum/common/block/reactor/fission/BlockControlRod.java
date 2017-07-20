package org.halvors.quantum.common.block.reactor.fission;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.halvors.quantum.common.block.BlockQuantum;

import javax.annotation.Nonnull;

public class BlockControlRod extends BlockQuantum {
    public BlockControlRod() {
        super("control_rod", Material.IRON);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess access, @Nonnull BlockPos pos) {
        return new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, 1, 0.6875);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(@Nonnull IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(@Nonnull IBlockState state) {
        return false;
    }
}