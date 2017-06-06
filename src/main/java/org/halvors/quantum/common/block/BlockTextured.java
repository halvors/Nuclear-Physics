package org.halvors.quantum.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.halvors.quantum.client.render.DefaultIcon;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.base.tile.ITileActiveState;
import org.halvors.quantum.common.base.tile.ITileRotatable;
import org.halvors.quantum.common.utility.render.Orientation;

public abstract class BlockTextured extends BlockMetadata {
    protected final IIcon[][] iconMetadataList = new IIcon[16][16];

    protected DefaultIcon defaultBlockIcon;

    protected BlockTextured(String name, Material material) {
        super(name, material);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(Reference.PREFIX + name);
        defaultBlockIcon = DefaultIcon.getAll(blockIcon);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        int metadata = tileEntity.getBlockMetadata();
        boolean isActive = false;

        // Check if this implements ITileActiveState, if it do we get the state from it.
        if (tileEntity instanceof ITileActiveState) {
            ITileActiveState tileActiveState = (ITileActiveState) tileEntity;
            isActive = tileActiveState.isActive();
        }

        // Check if this implements ITileRotatable.
        if (tileEntity instanceof ITileRotatable) {
            ITileRotatable tileRotatable = (ITileRotatable) tileEntity;

            return iconMetadataList[metadata][Orientation.getBaseOrientation(side, tileRotatable.getFacing()) + (isActive ? 6 : 0)];
        }

        return iconMetadataList[metadata][side + (isActive ? 6 : 0)];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        // Workaround to for when block is not rendered in world, by swapping the front and back sides.
        switch (side) {
            case 2: // Back
                side = 3;
                break;

            case 3: // Front
                side = 2;
                break;
        }

        return iconMetadataList[metadata][side];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube() {
        return false;
    }
}
