package org.halvors.quantum.atomic.common.block.reactor;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.atomic.common.block.BlockConnectedTexture;
import org.halvors.quantum.atomic.common.tile.reactor.TileGasFunnel;

import javax.annotation.Nonnull;

public class BlockGasFunnel extends BlockConnectedTexture {
    public BlockGasFunnel() {
        super("gas_funnel", Material.IRON);
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileGasFunnel();
    }
}