package org.halvors.quantum.common.block.reactor.fission;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.api.item.IReactorComponent;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.block.BlockRotatable;
import org.halvors.quantum.common.tile.reactor.fission.TileReactorCell;
import org.halvors.quantum.common.utility.InventoryUtility;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

public class BlockReactorCell extends BlockRotatable {
    public BlockReactorCell() {
        super("reactor_cell", Material.IRON);

        //setTextureName(Reference.PREFIX + "machine");
        setHardness(1.0F);
        setResistance(1.0F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack itemStack, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileReactorCell tile = (TileReactorCell) world.getTileEntity(pos);

        if (player.inventory.getCurrentItem() != null) {
            if (tile.getStackInSlot(0) == null) {
                if (player.inventory.getCurrentItem().getItem() instanceof IReactorComponent) {
                    //ItemStack itemStack = player.inventory.getCurrentItem().copy();
                    itemStack.stackSize = 1;
                    tile.setInventorySlotContents(0, itemStack);
                    player.inventory.decrStackSize(player.inventory.currentItem, 1);

                    return true;
                }
            }
        } else if (player.isSneaking() && tile.getStackInSlot(0) != null) {
            InventoryUtility.dropItemStack(world, new Vector3(player), tile.getStackInSlot(0), 0);
            tile.setInventorySlotContents(0, null);

            return true;
        } else {
            player.openGui(Quantum.getInstance(), 0, world, tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());

            return true;
        }

        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entityLiving, ItemStack itemStack) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileReactorCell) {
            ((TileReactorCell) tileEntity).updatePositionStatus();
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighborPos) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileReactorCell) {
            ((TileReactorCell) tile).updatePositionStatus();
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileReactorCell();
    }
}
