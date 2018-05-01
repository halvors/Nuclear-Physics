package org.halvors.nuclearphysics.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.common.block.states.BlockStateConnectedTexture;

import javax.annotation.Nonnull;

public class BlockConnectedTexture extends BlockContainerBase {
    public BlockConnectedTexture(final String name, final Material material) {
        super(name, material);

        // By default none of the sides are connected
        setDefaultState(blockState.getBaseState().withProperty(BlockStateConnectedTexture.CONNECTED_DOWN,  Boolean.FALSE)
                                                 .withProperty(BlockStateConnectedTexture.CONNECTED_EAST,  Boolean.FALSE)
                                                 .withProperty(BlockStateConnectedTexture.CONNECTED_NORTH, Boolean.FALSE)
                                                 .withProperty(BlockStateConnectedTexture.CONNECTED_SOUTH, Boolean.FALSE)
                                                 .withProperty(BlockStateConnectedTexture.CONNECTED_UP,    Boolean.FALSE)
                                                 .withProperty(BlockStateConnectedTexture.CONNECTED_WEST,  Boolean.FALSE));
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public IBlockState getActualState(final @Nonnull IBlockState state, final IBlockAccess world, final BlockPos pos) {
        // Creates the state to use for the block. This is where we check if every side is
        // connectable or not.
        return state.withProperty(BlockStateConnectedTexture.CONNECTED_DOWN,  isSideConnectable(world, pos, EnumFacing.DOWN))
                    .withProperty(BlockStateConnectedTexture.CONNECTED_EAST,  isSideConnectable(world, pos, EnumFacing.EAST))
                    .withProperty(BlockStateConnectedTexture.CONNECTED_NORTH, isSideConnectable(world, pos, EnumFacing.NORTH))
                    .withProperty(BlockStateConnectedTexture.CONNECTED_SOUTH, isSideConnectable(world, pos, EnumFacing.SOUTH))
                    .withProperty(BlockStateConnectedTexture.CONNECTED_UP,    isSideConnectable(world, pos, EnumFacing.UP))
                    .withProperty(BlockStateConnectedTexture.CONNECTED_WEST,  isSideConnectable(world, pos, EnumFacing.WEST));
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateConnectedTexture(this);
    }

    // Since the block has state information but we are not switching the meta value, we have
    // to override this method to return 0
    @Override
    public int getMetaFromState(final IBlockState state) {
        return 0;
    }

    /**
     * Checks if a specific side of a block can connect to this block. For this example, a side
     * is connectable if the block is the same block as this one.
     *
     * @param world The world to run the check in.
     * @param pos The position of the block to check for.
     * @param side The side of the block to check.
     * @return Whether or not the side is connectable.
     */
    private boolean isSideConnectable(final IBlockAccess world, final BlockPos pos, final EnumFacing side) {
        final IBlockState originalState = world.getBlockState(pos);
        final IBlockState connectedState = world.getBlockState(pos.offset(side));

        return canConnect(originalState, connectedState);
    }

    /**
     * Checks if this block should connect to another block
     * @param originalState BlockState to check
     * @param connectedState BlockState to check
     * @return True if the block is valid to connect
     */
    protected boolean canConnect(final @Nonnull IBlockState originalState, final @Nonnull IBlockState connectedState) {
        return originalState.getBlock() == connectedState.getBlock();
    }
}