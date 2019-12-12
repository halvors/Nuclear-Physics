package org.halvors.nuclearphysics.common.block.particle;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import org.halvors.nuclearphysics.common.block.BlockConnectedTexture;
import org.halvors.nuclearphysics.common.tile.particle.TileFulminationGenerator;

import javax.annotation.Nullable;

public class BlockFulminationGenerator extends BlockConnectedTexture {
    public BlockFulminationGenerator() {
        super("fulmination_generator", Material.IRON);

        setHardness(10);
        setResistance(25000);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileFulminationGenerator();
    }
}
