package org.halvors.quantum.common.block.particle;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.common.block.BlockConnectedTexture;
import org.halvors.quantum.common.tile.particle.TileFulmination;

import javax.annotation.Nonnull;

public class BlockFulmination extends BlockConnectedTexture {
    public BlockFulmination() {
        super("fulmination", Material.IRON);

        setHardness(10);
        setResistance(25000);
    }

    /*
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ISimpleBlockRenderer getRenderer() {
        return new ConnectedTextureRenderer(this, Reference.PREFIX + "atomic_edge");
    }
    */

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileFulmination();
    }
}
