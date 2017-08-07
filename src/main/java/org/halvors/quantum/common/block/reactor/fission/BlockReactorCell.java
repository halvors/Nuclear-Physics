package org.halvors.quantum.common.block.reactor.fission;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.halvors.quantum.api.item.IReactorComponent;
import org.halvors.quantum.common.block.BlockInventory;
import org.halvors.quantum.common.block.states.BlockStateReactorCell;
import org.halvors.quantum.common.tile.reactor.fission.TileReactorCell;
import org.halvors.quantum.common.utility.PlayerUtility;

import javax.annotation.Nonnull;

public class BlockReactorCell extends BlockInventory {
    public BlockReactorCell() {
        super("reactor_cell", Material.IRON);

        setHardness(1.0F);
        setResistance(1.0F);
        setDefaultState(blockState.getBaseState().withProperty(BlockStateReactorCell.TYPE, EnumReactorCell.TOP));
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @Nonnull
    public BlockStateContainer createBlockState() {
        return new BlockStateReactorCell(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int metadata) {
        return getDefaultState().withProperty(BlockStateReactorCell.TYPE, EnumReactorCell.values()[metadata]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockStateReactorCell.TYPE).ordinal();
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileReactorCell) {
            ((TileReactorCell) tile).updatePositionStatus();
        }

        super.onBlockAdded(world, pos, state);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack itemStack) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileReactorCell) {
            ((TileReactorCell) tile).updatePositionStatus();
        }

        super.onBlockPlacedBy(world, pos, state, entity, itemStack);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileReactorCell) {
            TileReactorCell tileReactorCell = (TileReactorCell) tile;

            IItemHandlerModifiable inventory = tileReactorCell.getMultiBlock().get().getInventory();
            ItemStack itemStackInSlot = inventory.getStackInSlot(0);
            ItemStack itemStack = player.getHeldItemMainhand();

            if (!itemStack.isEmpty()) {
                if (itemStackInSlot.isEmpty()) {
                    if (itemStack.getItem() instanceof IReactorComponent) {
                        inventory.insertItem(0, itemStack.copy(), false);
                        player.inventory.decrStackSize(player.inventory.currentItem, 1);

                        return true;
                    }
                }
            } else if (player.isSneaking() && !itemStackInSlot.isEmpty()) {
                inventory.setStackInSlot(0, ItemStack.EMPTY);
                ItemHandlerHelper.giveItemToPlayer(player, itemStackInSlot.copy());

                return true;
            } else {
                PlayerUtility.openGui(player, world, pos);

                return true;
            }
        }

        return false;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighborPos) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileReactorCell) {
            ((TileReactorCell) tile).updatePositionStatus();
        }
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileReactorCell();
    }

    public enum EnumReactorCell implements IStringSerializable {
        TOP("top"),
        MIDDLE("middle"),
        BOTTOM("bottom");

        private String name;

        EnumReactorCell(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name.toLowerCase();
        }
    }
}
