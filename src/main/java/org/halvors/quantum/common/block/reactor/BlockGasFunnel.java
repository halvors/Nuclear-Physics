package org.halvors.quantum.common.block.reactor;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.block.BlockContainerQuantum;
import org.halvors.quantum.common.block.BlockQuantum;
import org.halvors.quantum.common.tile.reactor.TileGasFunnel;

public class BlockGasFunnel extends BlockContainerQuantum { //implements IBlockCustomRender {
    //private static IIcon iconTop;

    public BlockGasFunnel() {
        super("gasFunnel", Material.IRON);
    }

    /*
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
    */

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    /*
    @Override
    @SideOnly(Side.CLIENT)
    public ISimpleBlockRenderer getRenderer() {
        return new ConnectedTextureRenderer(this, Reference.PREFIX + "gasFunnel_edge");
    }
    */

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileGasFunnel();
    }
}
