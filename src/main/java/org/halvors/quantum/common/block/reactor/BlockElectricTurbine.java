package org.halvors.quantum.common.block.reactor;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.block.BlockContainerQuantum;
import org.halvors.quantum.common.block.BlockQuantum;
import org.halvors.quantum.common.tile.reactor.TileElectricTurbine;

public class BlockElectricTurbine extends BlockContainerQuantum {
    public BlockElectricTurbine() {
        super("electricTurbine", Material.IRON);

        //setTextureName(Reference.PREFIX + "machine");
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack itemStack, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileElectricTurbine) {
            TileElectricTurbine tileTurbine = (TileElectricTurbine) tile;

            if (player.isSneaking()) {
                // TODO: Need to sync this between client and server in order for clients to be updated as well.
                if (!world.isRemote) {
                    return tileTurbine.getMultiBlock().toggleConstruct();
                }
            }
        }

        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileElectricTurbine) {
            TileElectricTurbine tileTurbine = (TileElectricTurbine) tileEntity;
            tileTurbine.getMultiBlock().deconstruct();
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    /*
    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return BlockRenderingHandler.getInstance().getRenderId();
    }
    */

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileElectricTurbine();
    }
}
