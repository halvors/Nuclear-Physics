package org.halvors.quantum.common.block.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.client.render.BlockRenderingHandler;
import org.halvors.quantum.client.render.ConnectedTextureRenderer;
import org.halvors.quantum.client.render.IBlockCustomRender;
import org.halvors.quantum.client.render.ISimpleBlockRenderer;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.block.BlockTextured;
import org.halvors.quantum.common.tile.particle.TileFulmination;

public class BlockFulmination extends BlockTextured implements IBlockCustomRender {
    public BlockFulmination() {
        super("fulmination", Material.iron);

        setHardness(10);
        setResistance(25000);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return BlockRenderingHandler.getInstance().getRenderId();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ISimpleBlockRenderer getRenderer() {
        return new ConnectedTextureRenderer(this, Reference.PREFIX + "atomic_edge");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileFulmination();
    }
}
