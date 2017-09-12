package org.halvors.nuclearphysics.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.block.states.BlockStateFacing;
import org.halvors.nuclearphysics.common.tile.ITileRotatable;
import org.halvors.nuclearphysics.common.utility.WrenchUtility;

import javax.annotation.Nonnull;

public abstract class BlockRotatable extends BlockContainerBase {
    protected BlockRotatable(String name, Material material) {
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
    public IBlockState getStateFromMeta(int metadata) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof ITileRotatable) {
            ITileRotatable tileRotatable = (ITileRotatable) tile;

            state = state.withProperty(BlockStateFacing.FACING, tileRotatable.getFacing());
        }

        return state;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack itemStack, EnumFacing side, float hitX, float hitY, float hitZ) {
        final TileEntity tile = world.getTileEntity(pos);

        return tile instanceof ITileRotatable && WrenchUtility.hasUsableWrench(player, hand, pos);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof ITileRotatable) {
            ITileRotatable tileRotatable = (ITileRotatable) tile;

            tileRotatable.setFacing(placer.getHorizontalFacing().getOpposite());
        }
    }

    @Override
    public EnumFacing[] getValidRotations(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        EnumFacing[] valid = new EnumFacing[6];

        if (tile instanceof ITileRotatable) {
            ITileRotatable tileRotatable = (ITileRotatable) tile;

            for (EnumFacing facing : EnumFacing.VALUES) {
                if (tileRotatable.canSetFacing(facing)) {
                    valid[facing.ordinal()] = facing;
                }
            }
        }

        return valid;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing side) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof ITileRotatable) {
            ITileRotatable tileRotatable = (ITileRotatable) tile;

            //if (tileRotatable.canSetFacing(side)) {
                tileRotatable.setFacing(side);
            //}

            return true;
        }

        return false;
    }
}