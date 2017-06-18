package org.halvors.quantum.common.block.reactor.fusion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.tile.reactor.fusion.TileElectromagnet;
import org.halvors.quantum.lib.render.BlockRenderingHandler;

import java.util.List;

public class BlockElectromagnet extends BlockContainer {
    private static IIcon iconTop, iconGlass;

    public BlockElectromagnet() {
        super(Material.iron);

        setUnlocalizedName("electromagnet");
        setTextureName(Reference.PREFIX + "electromagnet");
        setCreativeTab(Quantum.getCreativeTab());
        setResistance(20);

        //itemBlock = ItemBlockMetadata.class;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube() {
        return false;
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
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public int getRenderBlockPass() {
        return 0;
    }

    @Override
    public int getRenderType() {
        return BlockRenderingHandler.getId();
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

    /*
    @Override
    @SideOnly(Side.CLIENT)
    protected TileRender newRenderer() {
        return new ConnectedTextureRenderer(this, Reference.PREFIX + "atomic_edge");
    }
    */
}