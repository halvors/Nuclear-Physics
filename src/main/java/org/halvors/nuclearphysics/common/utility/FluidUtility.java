package org.halvors.nuclearphysics.common.utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.halvors.nuclearphysics.common.init.ModItems;
import org.halvors.nuclearphysics.common.item.ItemCell;

public class FluidUtility {
    public static boolean isEmptyContainer(ItemStack itemStack) {
        return itemStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null) && FluidUtil.getFluidContained(itemStack) == null;
    }

    public static boolean isFilledContainer(ItemStack itemStack) {
        if (itemStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            FluidStack fluidStack = FluidUtil.getFluidContained(itemStack);

            if (fluidStack != null) {
                return fluidStack.amount > 0;
            }
        }

        return false;
    }

    public static boolean isFilledContainer(ItemStack itemStack, Fluid fluid) {
        if (itemStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            FluidStack fluidStack = FluidUtil.getFluidContained(itemStack);

            if (fluid != null && fluidStack != null) {
                return fluid == fluidStack.getFluid() && fluidStack.amount > 0;
            }
        }

        return false;
    }

    public static boolean isFilledContainerEqual(ItemStack itemStackInput, ItemStack itemStackOutput) {
        FluidStack fluidStackInput = FluidUtil.getFluidContained(itemStackInput);
        FluidStack fluidStackOutput = FluidUtil.getFluidContained(itemStackOutput);

        return fluidStackInput != null && fluidStackInput.isFluidEqual(fluidStackOutput);
    }

    public static ItemStack getFilledCell(Fluid fluid) {
        return getFilledCell(new ItemStack(ModItems.itemCell), fluid);
    }

    public static ItemStack getFilledCell(ItemStack itemStack, Fluid fluid) {
        return getFilledContainer(itemStack, new FluidStack(fluid, ItemCell.capacity));
    }

    public static ItemStack getFilledContainer(ItemStack itemStack, FluidStack fluidStack) {
        IFluidHandler fluidHandler = FluidUtil.getFluidHandler(itemStack);

        if (fluidHandler != null) {
            fluidHandler.fill(fluidStack, true);
        }

        return itemStack;
    }

    public static void transferFluidToNeighbors(World world, BlockPos pos, IFluidHandler from) {
        if (from != null) {
            for (EnumFacing side : EnumFacing.values()) {
                IFluidHandler to = FluidUtil.getFluidHandler(world, pos.offset(side), side.getOpposite());

                if (to != null) {
                    FluidStack fluidStack = from.drain(Integer.MAX_VALUE, false);

                    if (fluidStack != null && to.fill(fluidStack, false) > 0) {
                        to.fill(from.drain(fluidStack, true), true);
                    }
                }
            }
        }
    }

    // Does all the work needed to fill or drain an item of fluid when a player clicks on the block.
    public static boolean playerActivatedFluidItem(World world, BlockPos pos, EntityPlayer player, ItemStack itemStack, EnumFacing side) {
        final IFluidHandler fluidHandler = FluidUtil.getFluidHandler(world, pos, side);

        if (!itemStack.isEmpty() && fluidHandler != null) {
            if (isFilledContainer(itemStack)) {
                FluidStack fluidStack = FluidUtil.getFluidContained(itemStack);

                if (fluidHandler.fill(fluidStack, false) > 0) {
                    if (!player.isCreative()) {
                        player.inventory.decrStackSize(player.inventory.currentItem, 1);
                    }

                    fluidHandler.fill(fluidStack, true);

                    return true;
                }
            } else if (isEmptyContainer(itemStack)) {
                FluidStack available = fluidHandler.drain(Integer.MAX_VALUE, false);

                if (available != null) {
                    ItemStack itemStackFilled = itemStack.copy();
                    itemStackFilled.setCount(1);
                    itemStackFilled = getFilledContainer(itemStackFilled, available);
                    FluidStack fluidStack = FluidUtil.getFluidContained(itemStackFilled);

                    if (fluidStack != null) {
                        fluidHandler.drain(fluidStack.amount, true);

                        if (!player.isCreative()) {
                            ItemHandlerHelper.giveItemToPlayer(player, itemStackFilled);
                            player.inventory.decrStackSize(player.inventory.currentItem, 1);
                        }

                        return true;
                    }
                }
            }
        }

        return false;
    }
}
