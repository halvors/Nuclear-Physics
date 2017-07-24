package org.halvors.quantum.common.block;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.halvors.quantum.common.tile.ITileRotatable;

import javax.annotation.Nonnull;

public abstract class BlockRotatable extends BlockContainerQuantum {
    public BlockRotatable(String name, Material material) {
        super(name, material);

        setDefaultState(blockState.getBaseState().withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
    }

    @Override
    @Nonnull
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockHorizontal.FACING);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof ITileRotatable) {
            ITileRotatable tileRotatable = (ITileRotatable) tile;

            state.withProperty(BlockHorizontal.FACING, tileRotatable.getFacing());
        }

        return state;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof ITileRotatable) {
            ITileRotatable tileRotatable = (ITileRotatable) tile;

            tileRotatable.setFacing(placer.getHorizontalFacing().getOpposite());
            world.markBlockRangeForRenderUpdate(pos, pos.add(1,1,1));
        }
    }

    /*
    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof ITileRotatable) {
            ITileRotatablee tileRotatable = (ITileRotatable) tile;
            tileRotatable.setFacing(axis);

            return true;
        }

        return false;
    }
    */
}