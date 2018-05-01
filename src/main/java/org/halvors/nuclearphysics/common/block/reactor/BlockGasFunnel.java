package org.halvors.nuclearphysics.common.block.reactor;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.block.BlockConnectedTexture;
import org.halvors.nuclearphysics.common.tile.reactor.TileGasFunnel;

import javax.annotation.Nonnull;

public class BlockGasFunnel extends BlockConnectedTexture {
    public BlockGasFunnel() {
        super("gas_funnel", Material.IRON);

        setHardness(0.6F);
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(final @Nonnull World world, final @Nonnull IBlockState state) {
        return new TileGasFunnel();
    }
}