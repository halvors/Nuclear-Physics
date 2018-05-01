package org.halvors.nuclearphysics.common.block;

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
import org.halvors.nuclearphysics.common.block.states.BlockStateFacing;
import org.halvors.nuclearphysics.common.tile.ITileRotatable;

import javax.annotation.Nonnull;

public class BlockRotatable extends BlockContainerBase {
    protected BlockRotatable(final String name, final Material material) {
        super(name, material);

        setDefaultState(blockState.getBaseState().withProperty(BlockStateFacing.FACING, EnumFacing.NORTH));
    }

    @Override
    @Nonnull
    public BlockStateContainer createBlockState() {
        return new BlockStateFacing(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public IBlockState getStateFromMeta(final int metadata) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public IBlockState getActualState(final @Nonnull IBlockState state, final IBlockAccess world, final BlockPos pos) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof ITileRotatable) {
            final ITileRotatable tileRotatable = (ITileRotatable) tile;

            return state.withProperty(BlockStateFacing.FACING, tileRotatable.getFacing());
        }

        return super.getActualState(state, world, pos);
    }

    @Override
    public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase entity, final ItemStack itemStack) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof ITileRotatable) {
            ITileRotatable tileRotatable = (ITileRotatable) tile;

            tileRotatable.setFacing(entity.getHorizontalFacing().getOpposite());
        }
    }

    @Override
    @Nonnull
    public EnumFacing[] getValidRotations(final World world, final @Nonnull BlockPos pos) {
        final TileEntity tile = world.getTileEntity(pos);
        final EnumFacing[] valid = new EnumFacing[6];

        if (tile instanceof ITileRotatable) {
            final ITileRotatable tileRotatable = (ITileRotatable) tile;

            for (EnumFacing facing : EnumFacing.VALUES) {
                if (tileRotatable.canSetFacing(facing)) {
                    valid[facing.ordinal()] = facing;
                }
            }
        }

        return valid;
    }

    @Override
    public boolean rotateBlock(final World world, final @Nonnull BlockPos pos, final EnumFacing side) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof ITileRotatable) {
            final ITileRotatable tileRotatable = (ITileRotatable) tile;

            if (tileRotatable.canSetFacing(side)) {
                tileRotatable.setFacing(side);
            }

            return true;
        }

        return false;
    }
}