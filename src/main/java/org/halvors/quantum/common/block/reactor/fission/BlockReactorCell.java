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
import org.halvors.quantum.common.block.BlockRotatableMeta;
import org.halvors.quantum.common.tile.reactor.fission.TileReactorCell;
import org.halvors.quantum.common.utility.InventoryUtility;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

import javax.annotation.Nonnull;

public class BlockReactorCell extends BlockRotatableMeta {
    public BlockReactorCell() {
        super("reactor_cell", Material.IRON);

        //setTextureName(Reference.PREFIX + "machine");
        setHardness(1.0F);
        setResistance(1.0F);
    }

    @Override
    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(@Nonnull IBlockState blockState, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(@Nonnull IBlockState blockState) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(@Nonnull IBlockState blockState) {
        return false;
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, ItemStack itemStack, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
        final TileReactorCell tile = (TileReactorCell) world.getTileEntity(pos);

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
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileReactorCell) {
            ((TileReactorCell) tile).updatePositionStatus();
        }
    }

    @Override
    public void onNeighborChange(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull BlockPos neighborPos) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileReactorCell) {
            ((TileReactorCell) tile).updatePositionStatus();
        }
    }

    @Override
    @Nonnull
    public TileEntity createNewTileEntity(@Nonnull World world, int metadata) {
        return new TileReactorCell();
    }
}
