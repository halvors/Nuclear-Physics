package org.halvors.quantum.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.halvors.quantum.common.block.machine.BlockMachine;
import org.halvors.quantum.common.block.states.BlockStateFacing;
import org.halvors.quantum.common.block.states.BlockStateMachine;
import org.halvors.quantum.common.tile.ITileRotatable;

import javax.annotation.Nonnull;

public abstract class BlockRotatable extends BlockContainerQuantum {
    public BlockRotatable(String name, Material material) {
        super(name, material);
    }

    @Override
    @Nonnull
    public BlockStateContainer createBlockState() {
        return new BlockStateFacing(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int metadata) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof ITileRotatable) {
            ITileRotatable tileRotatable = (ITileRotatable) tile;

            state = state.withProperty(BlockStateFacing.facingProperty, tileRotatable.getFacing());
        }

        return state;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof ITileRotatable) {
            ITileRotatable tileRotatable = (ITileRotatable) tile;

            tileRotatable.setFacing(placer.getHorizontalFacing().getOpposite());
            //world.markBlockRangeForRenderUpdate(pos, pos.add(1,1,1));
        }
    }
}