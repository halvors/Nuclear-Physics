package org.halvors.quantum.common.block.reactor.fusion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.client.render.BlockRenderingHandler;
import org.halvors.quantum.client.render.ConnectedTextureRenderer;
import org.halvors.quantum.client.render.IBlockCustomRender;
import org.halvors.quantum.client.render.IBlockRenderer;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.tile.reactor.fusion.TileElectromagnet;
import org.halvors.quantum.common.transform.vector.Vector3;

import java.util.List;

public class BlockElectromagnet extends BlockContainer implements IBlockCustomRender {
    private static IIcon iconTop, iconGlass;

    public BlockElectromagnet() {
        super(Material.iron);

        setUnlocalizedName("electromagnet");
        setTextureName(Reference.PREFIX + "electromagnet");
        setCreativeTab(Quantum.getCreativeTab());
        setResistance(20);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);

        iconTop = iconRegister.registerIcon(Reference.PREFIX + "electromagnet_top");
        iconGlass = iconRegister.registerIcon(Reference.PREFIX + "electromagnetGlass");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        if (metadata == 1) {
            return iconGlass;
        }

        if (side == 0 || side == 1) {
            return iconTop;
        }

        return super.getIcon(side, metadata);
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

        return super.shouldSideBeRendered(access, x, y, z, side);
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        super.getSubBlocks(item, creativeTabs, list);

        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileElectromagnet();
    }

    @Override
    public IBlockRenderer getRenderer() {
        return new ConnectedTextureRenderer(this, Reference.PREFIX + "atomic_edge");
    }
}