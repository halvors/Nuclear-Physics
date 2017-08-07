package org.halvors.quantum.common.block.reactor.fission;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.halvors.quantum.common.block.BlockContainerQuantum;
import org.halvors.quantum.common.tile.reactor.fission.TileSiren;
import org.halvors.quantum.common.utility.WrenchUtility;

import javax.annotation.Nonnull;

public class BlockSiren extends BlockContainerQuantum {
    public BlockSiren() {
        super("siren", Material.IRON);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (WrenchUtility.hasUsableWrench(player, pos)) {
            int metadata = state.getBlock().getMetaFromState(state);

            if (player.isSneaking()) {
                metadata--;
            } else {
                metadata++;
            }

            metadata = Math.max(metadata % 16, 0);

            world.setBlockState(pos, state.getBlock().getStateFromMeta(metadata));

            return true;
        }

        return false;
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileSiren();
    }
}
