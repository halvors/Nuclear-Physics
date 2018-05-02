package org.halvors.nuclearphysics.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import org.halvors.nuclearphysics.client.render.block.BlockRenderingHandler;
import org.halvors.nuclearphysics.client.render.block.ConnectedTextureRenderer;
import org.halvors.nuclearphysics.client.render.block.ISimpleBlockRenderer;

public class BlockConnectedTexture extends BlockContainerBase implements ICustomBlockRenderer {
    public BlockConnectedTexture(final String name, final Material material) {
        super(name, material);
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
        return new ConnectedTextureRenderer(this, name);
    }
}