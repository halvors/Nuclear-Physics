package org.halvors.nuclearphysics.common.block.reactor;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.halvors.nuclearphysics.api.item.IReactorComponent;
import org.halvors.nuclearphysics.common.block.BlockInventory;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.tile.reactor.TileReactorCell;
import org.halvors.nuclearphysics.common.utility.PlayerUtility;

import javax.annotation.Nonnull;

public class BlockReactorCell extends BlockInventory {
    public BlockReactorCell() {
        super("reactor_cell", Material.IRON);

        setHardness(1.0F);
        setResistance(1.0F);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderInLayer(IBlockState state, @Nonnull BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack itemStack, EnumFacing side, float hitX, float hitY, float hitZ) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileReactorCell) {
            TileReactorCell tileReactorCell = (TileReactorCell) tile;
            FluidStack fluidStack = tileReactorCell.getTank().getFluid();
            IItemHandlerModifiable inventory = tileReactorCell.getInventory();
            ItemStack itemStackInSlot = inventory.getStackInSlot(0);

            if (player.isSneaking()) {
                if (!ModFluids.fluidStackPlasma.isFluidEqual(fluidStack) && itemStack == null && itemStackInSlot != null) {
                    ItemHandlerHelper.giveItemToPlayer(player, itemStackInSlot.copy());
                    inventory.setStackInSlot(0, null);

                    return true;
                }
            } else {
                if (!ModFluids.fluidStackPlasma.isFluidEqual(fluidStack) && itemStack != null && itemStackInSlot == null && itemStack.getItem() instanceof IReactorComponent) {
                    inventory.insertItem(0, itemStack.copy(), false);
                    player.inventory.decrStackSize(player.inventory.currentItem, 1);
                } else {
                    PlayerUtility.openGui(player, world, pos);
                }

                return true;
            }
        }

        return false;
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileReactorCell(name);
    }
}
