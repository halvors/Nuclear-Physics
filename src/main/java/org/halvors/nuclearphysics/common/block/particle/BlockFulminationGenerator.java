package org.halvors.nuclearphysics.common.block.particle;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.block.BlockConnectedTexture;
import org.halvors.nuclearphysics.common.tile.particle.TileFulminationGenerator;

public class BlockFulminationGenerator extends BlockConnectedTexture {
    public BlockFulminationGenerator() {
        super("fulmination_generator", Material.iron);

        setHardness(10);
        setResistance(25000);
    }

    @Override
    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileFulminationGenerator();
    }
}
