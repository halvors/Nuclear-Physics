package org.halvors.nuclearphysics.common.utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
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
        return getFilledContainer(new ItemStack(ModItems.itemCell), new FluidStack(fluid, ItemCell.capacity));
    }

    public static ItemStack getFilledContainer(ItemStack stack, FluidStack fluidStack) {
        IFluidHandler fluidHandler = FluidUtil.getFluidHandler(stack);

        if (fluidHandler != null) {
            fluidHandler.fill(fluidStack, true);
        }

        return stack;
    }

    public static void transferFluidToNeighbors(IBlockAccess world, BlockPos pos, IFluidTank tank, FluidStack requestFluidStack) {
        if (tank != null && requestFluidStack != null && tank.getFluid() != null) {
            requestFluidStack.amount = (tank.getCapacity() - tank.getFluid().amount);

            for (EnumFacing side : EnumFacing.values()) {
                TileEntity tile = world.getTileEntity(pos.offset(side));

                if (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, side.getOpposite())) {
                    IFluidHandler fluidHandler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, side.getOpposite());
                    FluidStack receiveFluidStack = fluidHandler.drain(requestFluidStack, true);

                    if (receiveFluidStack != null && receiveFluidStack.amount > 0) {
                        if (tank.fill(receiveFluidStack, false) > 0) {
                            tank.fill(receiveFluidStack, true);
                        }
                    }
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Does all the work needed to fill or drain an item of fluid when a player clicks on the block.
    public static boolean playerActivatedFluidItem(World world, BlockPos pos, EntityPlayer player, EnumFacing side) {
        /*
        TileEntity tile = world.getTileEntity(pos);
        ItemStack current = entityplayer.inventory.getCurrentItem();

        if (current != null && tile != null) {
            if (tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null))  {
                IFluidHandler tank = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
                FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(current);

                if (FluidContainerRegistry.isFilledContainer(current)) {
                    if (tank.fill(fluid, false) == fluid.amount) {
                        tank.fill(fluid, true);

                        if (!entityplayer.capabilities.isCreativeMode) {
                            InventoryUtility.consumeHeldItem(entityplayer);
                        }

                        return true;
                    }
                } else if (FluidContainerRegistry.isEmptyContainer(current)) {
                    FluidStack available = tank.drain(Integer.MAX_VALUE, false);

                    if (available != null) {
                        ItemStack filled = FluidContainerRegistry.fillFluidContainer(available, current);

                        fluid = FluidContainerRegistry.getFluidForFilledItem(filled);

                        if (fluid != null) {
                            if (!entityplayer.capabilities.isCreativeMode) {
                                if (current.stackSize > 1) {
                                    if (!entityplayer.inventory.addItemStackToInventory(filled)) {
                                        return false;
                                    } else {
                                        entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, AutoCraftingManager.consumeItem(current, 1));
                                    }
                                } else {
                                    entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, AutoCraftingManager.consumeItem(current, 1));
                                    entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, filled);
                                }
                            }

                            tank.drain(fluid.amount, true);

                            return true;
                        }
                    }
                }
            }
        }
        */

        return false;
    }
}
