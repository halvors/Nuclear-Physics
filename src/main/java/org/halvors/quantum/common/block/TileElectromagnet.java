package org.halvors.quantum.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.item.ItemBlockMetadata;
import org.halvors.quantum.lib.render.ConnectedTextureRenderer;
import org.halvors.quantum.lib.tile.TileBase;
import org.halvors.quantum.lib.tile.TileRender;

import java.util.List;

public class TileElectromagnet extends TileBase implements IElectromagnet {
    private static IIcon iconTop, iconGlass;

    public TileElectromagnet() {
        super("electromagnet", Material.iron);

        blockResistance = 20;
        isOpaqueCube = false;
        itemBlock = ItemBlockMetadata.class;
    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        if (metadata == 1) {
            return iconGlass;
        }

        if (side == 0 || side == 1) {
            return iconTop;
        }

        return super.getIcon(side, metadata);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);

        iconTop = iconRegister.registerIcon(Reference.PREFIX + "electromagnet_top");
        iconGlass = iconRegister.registerIcon(Reference.PREFIX + "electromagnetGlass");
    }

    @Override
    public int metadataDropped(int metadata, int fortune) {
        return metadata;
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
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        super.getSubBlocks(item, creativeTabs, list);

        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected TileRender newRenderer() {
        return new ConnectedTextureRenderer(this, Reference.PREFIX + "atomic_edge");
    }
}
