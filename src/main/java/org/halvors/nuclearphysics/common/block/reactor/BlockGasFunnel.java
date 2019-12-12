package org.halvors.nuclearphysics.common.block.reactor;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import org.halvors.nuclearphysics.common.block.BlockConnectedTexture;
import org.halvors.nuclearphysics.common.tile.reactor.TileGasFunnel;

import javax.annotation.Nullable;

public class BlockGasFunnel extends BlockConnectedTexture {
    public BlockGasFunnel() {
        super("gas_funnel", Material.IRON);

        setHardness(0.6F);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileGasFunnel();
    }
}