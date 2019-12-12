package org.halvors.nuclearphysics.common.block.reactor;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.BlockContainerBase;
import org.halvors.nuclearphysics.common.block.states.BlockStateSiren;
import org.halvors.nuclearphysics.common.tile.reactor.TileSiren;
import org.halvors.nuclearphysics.common.utility.WrenchUtility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockSiren extends BlockContainerBase {
    public BlockSiren() {
        super("siren", Material.IRON);

        setHardness(0.6F);
        setDefaultState(blockState.getBaseState().withProperty(BlockStateSiren.PITCH, 0));
    }

    @Override
    public void registerBlockModel() {
        NuclearPhysics.getProxy().registerBlockRendererAndIgnore(this, BlockStateSiren.PITCH);
    }

    @Override
    @Nonnull
    public BlockStateContainer createBlockState() {
        return new BlockStateSiren(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public IBlockState getStateFromMeta(final int metadata) {
        return getDefaultState().withProperty(BlockStateSiren.PITCH, metadata);
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockStateSiren.PITCH);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (WrenchUtility.hasUsableWrench(player, hand, pos)) {
            int pitch = state.getValue(BlockStateSiren.PITCH);

            if (player.isSneaking()) {
                pitch--;
            } else {
                pitch++;
            }

            return world.setBlockState(pos, state.withProperty(BlockStateSiren.PITCH, Math.max(pitch % 16, 0)));
        }

        return false;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileSiren();
    }
}
