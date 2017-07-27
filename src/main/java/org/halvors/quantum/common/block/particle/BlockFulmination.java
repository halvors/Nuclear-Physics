package org.halvors.quantum.common.block.particle;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.common.block.BlockContainerQuantum;
import org.halvors.quantum.common.tile.particle.TileFulmination;

import javax.annotation.Nonnull;

public class BlockFulmination extends BlockContainerQuantum {
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
    public TileEntity createNewTileEntity(@Nonnull World world, int metadata) {
        return new TileFulmination();
    }
}
