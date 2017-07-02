package org.halvors.quantum.common.block.reactor.fission;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.halvors.quantum.common.block.BlockQuantum;

public class BlockControlRod extends BlockQuantum {
    public BlockControlRod() {
        super("control_rod", Material.IRON);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
        return new AxisAlignedBB(0.3F, 0, 0.3F, 0.7F, 1, 0.7F);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
        return new AxisAlignedBB(0.3F, 0, 0.3F, 0.7F, 1, 0.7F);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
}