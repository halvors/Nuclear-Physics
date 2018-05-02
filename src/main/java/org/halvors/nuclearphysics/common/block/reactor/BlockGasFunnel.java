package org.halvors.nuclearphysics.common.block.reactor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.block.BlockConnectedTexture;
import org.halvors.nuclearphysics.common.tile.reactor.TileGasFunnel;

public class BlockGasFunnel extends BlockConnectedTexture {
    @SideOnly(Side.CLIENT)
    private static IIcon iconTop;

    public BlockGasFunnel() {
        super("gas_funnel", Material.iron);

        setHardness(0.6F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister iconRegister) {
        super.registerIcons(iconRegister);

        iconTop = iconRegister.registerIcon(Reference.PREFIX + name + "_top");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int side, final int metadata) {
        return side == 1 || side == 0 ? iconTop : super.getIcon(side, metadata);
    }

    @Override
    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileGasFunnel();
    }
}