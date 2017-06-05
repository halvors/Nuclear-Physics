package org.halvors.quantum.lib.prefab.turbine;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.lib.prefab.block.BlockRotatable;

public class BlockTurbine extends BlockRotatable {
    public BlockTurbine(Material material) {
        super(material);
        this.rotationMask = Byte.parseByte("000001", 2);
    }

    public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (tileEntity instanceof TileTurbine) {
            if (!world.isRemote) {
                return ((TileTurbine) tileEntity).getMultiBlock().toggleConstruct();
            }

            return true;
        }

        return false;
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int par6) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if ((tileEntity instanceof TileTurbine)) {
            ((TileTurbine)tileEntity).getMultiBlock().deconstruct();
        }

        super.breakBlock(world, x, y, z, block, par6);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }
}
