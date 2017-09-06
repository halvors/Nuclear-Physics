package org.halvors.nuclearphysics.common.block.reactor;

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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.halvors.nuclearphysics.api.item.IReactorComponent;
import org.halvors.nuclearphysics.common.block.BlockInventory;
import org.halvors.nuclearphysics.common.block.states.BlockStateReactorCell;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.init.ModItems;
import org.halvors.nuclearphysics.common.multiblock.MultiBlockHandler;
import org.halvors.nuclearphysics.common.tile.reactor.TileReactorCell;
import org.halvors.nuclearphysics.common.utility.PlayerUtility;

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
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    /*
    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    @Nonnull
    public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess access, @Nonnull BlockPos pos) {
        TileEntity tile = access.getTileEntity(pos);

        if (tile instanceof TileReactorCell) {
            TileReactorCell tileReactorCell = ((TileReactorCell) tile);

            return new AxisAlignedBB(0, -tileReactorCell.getHeightIndex(), 0, 1, tileReactorCell.getHeight(), 1);
        }

        return super.getBoundingBox(state, access, pos);
    }
    */

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
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileReactorCell) {
            ((TileReactorCell) tile).updatePositionStatus();
        }

        super.onNeighborChange(world, pos, neighbor);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack itemStack, EnumFacing side, float hitX, float hitY, float hitZ) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileReactorCell) {
            TileReactorCell tileReactorCell = (TileReactorCell) tile;
            MultiBlockHandler<TileReactorCell> multiBlockHandler = tileReactorCell.getMultiBlock();

            // Insert fuel rod only in the primary multi-block.
            if (multiBlockHandler.isConstructed()) {
                FluidStack fluidStack = multiBlockHandler.get().getTank().getFluid();

                if (fluidStack != null && !fluidStack.isFluidEqual(ModFluids.fluidStackPlasma)) {
                    IItemHandlerModifiable inventory = multiBlockHandler.getPrimary().getInventory();
                    ItemStack itemStackInSlot = inventory.getStackInSlot(0);

                    if (player.isSneaking()) {
                        if (itemStack == null && itemStackInSlot != null) {
                            ItemHandlerHelper.giveItemToPlayer(player, itemStackInSlot.copy());
                            inventory.setStackInSlot(0, null);

                            return true;
                        }
                    } else {
                        if (itemStack != null && itemStackInSlot == null && itemStack.getItem() == ModItems.itemFissileFuel) {//&& OreDictionaryHelper.isFuel(itemStack)) {
                            if (itemStack.getItem() instanceof IReactorComponent) {
                                inventory.insertItem(0, itemStack.copy(), false);
                                player.inventory.decrStackSize(player.inventory.currentItem, 1);
                            }
                        } else {
                            PlayerUtility.openGui(player, world, pos);
                        }

                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileReactorCell(name);
    }

    public enum EnumReactorCell implements IStringSerializable {
        NORMAL("normal"),
        TOP("top"),
        MIDDLE("middle"),
        BOTTOM("bottom");

        private String name;

        EnumReactorCell(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
