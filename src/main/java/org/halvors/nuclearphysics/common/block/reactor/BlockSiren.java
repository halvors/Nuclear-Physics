package org.halvors.nuclearphysics.common.block.reactor;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.BlockContainerBase;
import org.halvors.nuclearphysics.common.block.states.BlockStateSiren;
import org.halvors.nuclearphysics.common.tile.reactor.TileSiren;
import org.halvors.nuclearphysics.common.utility.WrenchUtility;

import javax.annotation.Nonnull;

public class BlockSiren extends BlockContainerBase {
    public BlockSiren() {
        super("siren", Material.IRON);

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
    public IBlockState getStateFromMeta(int metadata) {
        return getDefaultState().withProperty(BlockStateSiren.PITCH, metadata);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockStateSiren.PITCH);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
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

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileSiren();
    }
}
