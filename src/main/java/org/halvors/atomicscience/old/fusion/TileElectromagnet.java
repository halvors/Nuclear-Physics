package org.halvors.atomicscience.old.fusion;

import calclavia.api.atomicscience.IElectromagnet;
import calclavia.lib.content.module.TileBase;
import calclavia.lib.content.module.TileRender;
import calclavia.lib.prefab.item.ItemBlockMetadata;
import calclavia.lib.render.ConnectedTextureRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import universalelectricity.api.UniversalElectricity;

public class TileElectromagnet
        extends TileBase
        implements IElectromagnet
{
    private static Icon iconTop;
    private static Icon iconGlass;

    public TileElectromagnet()
    {
        super(UniversalElectricity.machine);
        this.blockResistance = 20.0F;
        this.isOpaqueCube = false;
        this.itemBlock = ItemBlockMetadata.class;
    }

    public Icon getIcon(int side, int metadata)
    {
        if (metadata == 1) {
            return iconGlass;
        }
        if ((side == 0) || (side == 1)) {
            return iconTop;
        }
        return super.getIcon(side, metadata);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        super.registerIcons(iconRegister);
        iconTop = iconRegister.func_94245_a(this.domain + this.textureName + "_top");
        iconGlass = iconRegister.func_94245_a(this.domain + "electromagnetGlass");
    }

    public int metadataDropped(int meta, int fortune)
    {
        return meta;
    }

    public boolean canUpdate()
    {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side)
    {
        return true;
    }

    public int getRenderBlockPass()
    {
        return 0;
    }

    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }

    public boolean isRunning()
    {
        return true;
    }

    protected TileRender newRenderer()
    {
        return new ConnectedTextureRenderer(this, "atomicscience:atomic_edge");
    }
}
