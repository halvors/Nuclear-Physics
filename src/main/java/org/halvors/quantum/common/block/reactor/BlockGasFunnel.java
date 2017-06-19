package org.halvors.quantum.common.block.reactor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.tile.reactor.TileGasFunnel;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.lib.render.BlockRenderingHandler;

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
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return BlockRenderingHandler.getId();
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
    public boolean renderAsNormalBlock() {
        return false;
    }

    /*
    @Override
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side) {
        Vector3 neighborPosition = new Vector3(x, y, z).translate(ForgeDirection.getOrientation(side).getOpposite());
        Block block = access.getBlock(x, y, z);
        int metadata = access.getBlockMetadata(x, y, z);
        Block neighborBlock = neighborPosition.getBlock(access);
        int neighborMetadata = neighborPosition.getBlockMetadata(access);

        // Transparent electromagnetic glass.
        if (block == this && neighborBlock == this && metadata == 1 && neighborMetadata == 1) {
            return false;
        }

        return true;
    }
    */

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileGasFunnel();
    }
}
