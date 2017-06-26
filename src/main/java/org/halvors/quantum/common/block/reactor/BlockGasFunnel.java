package org.halvors.quantum.common.block.reactor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.halvors.quantum.client.render.BlockRenderingHandler;
import org.halvors.quantum.client.render.ConnectedTextureRenderer;
import org.halvors.quantum.client.render.IBlockCustomRender;
import org.halvors.quantum.client.render.ISimpleBlockRenderer;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.block.BlockTextured;
import org.halvors.quantum.common.tile.reactor.TileGasFunnel;

public class BlockGasFunnel extends BlockTextured implements IBlockCustomRender {
    private static IIcon iconTop;

    public BlockGasFunnel() {
        super("gasFunnel", Material.iron);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);

        iconTop = iconRegister.registerIcon(Reference.PREFIX + "gasFunnel_top");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        return side == 1 || side == 0 ? iconTop : super.getIcon(side, metadata);
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
        return new ConnectedTextureRenderer(this, Reference.PREFIX + "gasFunnel_edge");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileGasFunnel();
    }
}
