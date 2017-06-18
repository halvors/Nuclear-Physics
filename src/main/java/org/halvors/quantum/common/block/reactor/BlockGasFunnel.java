package org.halvors.quantum.common.block.reactor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.tile.reactor.TileGasFunnel;

public class BlockGasFunnel extends BlockContainer {
    private static IIcon iconTop;

    public BlockGasFunnel() {
        super(Material.iron);

        setUnlocalizedName("gasFunnel");
        setTextureName(Reference.PREFIX + "gasFunnel");
        setCreativeTab(Quantum.getCreativeTab());
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
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileGasFunnel();
    }

    /*
    @SideOnly(Side.CLIENT)
    @Override
    protected TileRender newRenderer() {
        return new ConnectedTextureRenderer(this, Reference.PREFIX + "gasFunnel_edge");
    }
    */
}
