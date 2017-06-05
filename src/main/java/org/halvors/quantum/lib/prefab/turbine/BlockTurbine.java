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

    @Override
    public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (tileEntity instanceof TileTurbine) {
            TileTurbine tileTurbine = (TileTurbine) tileEntity;

            // TODO: Need to sync this between client and server in order for clients to be updated as well.
            if (!world.isRemote) {
                return tileTurbine.getMultiBlock().toggleConstruct();
            }

            return true;
        }

        return false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if (tileEntity instanceof TileTurbine) {
            TileTurbine tileTurbine = (TileTurbine) tileEntity;
            tileTurbine.getMultiBlock().deconstruct();
        }

        super.breakBlock(world, x, y, z, block, metadata);
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
