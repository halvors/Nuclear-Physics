package org.halvors.nuclearphysics.common.utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;
import org.halvors.nuclearphysics.common.init.ModItems;
import org.halvors.nuclearphysics.common.item.ItemCell;
import org.halvors.nuclearphysics.common.type.Position;

public class FluidUtility {
    public static boolean isEmptyContainer(ItemStack itemStack) {
        Item item = itemStack.getItem();

        if (item instanceof IFluidContainerItem) {
            IFluidContainerItem fluidContainerItem = (IFluidContainerItem) item;

            return fluidContainerItem.getFluid(itemStack) == null;
        }

        return false;
    }

    public static boolean isFilledContainer(ItemStack itemStack) {
        Item item = itemStack.getItem();

        if (item instanceof IFluidContainerItem) {
            IFluidContainerItem fluidContainerItem = (IFluidContainerItem) item;
            FluidStack fluidStack = fluidContainerItem.getFluid(itemStack);

            if (fluidStack != null) {
                return fluidStack.amount > 0;
            }
        }

        return false;
    }

    public static boolean isFilledContainer(ItemStack itemStack, Fluid fluid) {
        Item item = itemStack.getItem();

        if (item instanceof IFluidContainerItem) {
            IFluidContainerItem fluidContainerItem = (IFluidContainerItem) item;
            FluidStack fluidStack = fluidContainerItem.getFluid(itemStack);

            if (fluid != null && fluidStack != null) {
                return fluid == fluidStack.getFluid() && fluidStack.amount > 0;
            }
        }

        return false;
    }

    public static boolean isFilledContainerEqual(ItemStack itemStackInput, ItemStack itemStackOutput) {
        Item itemInput = itemStackInput.getItem();
        Item itemOutput = itemStackOutput.getItem();

        if (itemInput instanceof IFluidContainerItem && itemOutput instanceof IFluidContainerItem) {
            FluidStack fluidStackInput = ((IFluidContainerItem) itemInput).getFluid(itemStackInput);
            FluidStack fluidStackOutput = ((IFluidContainerItem) itemInput).getFluid(itemStackOutput);

            return fluidStackInput != null && fluidStackInput.isFluidEqual(fluidStackOutput);
        }

        return false;
    }

    public static ItemStack getFilledCell(Fluid fluid) {
        return getFilledContainer(new ItemStack(ModItems.itemCell), new FluidStack(fluid, ItemCell.capacity));
    }

    public static ItemStack getFilledContainer(ItemStack itemStack, FluidStack fluidStack) {
        Item item = itemStack.getItem();

        if (item instanceof IFluidContainerItem) {
            IFluidContainerItem fluidContainerItem = (IFluidContainerItem) item;
            fluidContainerItem.fill(itemStack, fluidStack, true);
        }

        return itemStack;
    }

    public static void transferFluidToNeighbors(World world, int x, int y, int z, IFluidHandler from) {
        if (from != null) {
            for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                Position pos = new Position(x, y, z).offset(side);
                TileEntity tile = world.getTileEntity(pos.getIntX(), pos.getIntY(), pos.getIntZ());

                if (tile instanceof IFluidHandler) {
                    IFluidHandler to = (IFluidHandler) tile;

                    FluidStack fluidStack = from.drain(side.getOpposite(), Integer.MAX_VALUE, false);

                    if (fluidStack != null && to.fill(side.getOpposite(), fluidStack, false) > 0) {
                        to.fill(side.getOpposite(), from.drain(side.getOpposite(), fluidStack, true), true);
                    }
                }
            }
        }

    }
    // Does all the work needed to fill or drain an item of fluid when a player clicks on the block.
    public static boolean playerActivatedFluidItem(World world, int x, int y, int z, EntityPlayer player, ItemStack itemStack, ForgeDirection side){
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof IFluidHandler) {
            IFluidHandler fluidHandler = (IFluidHandler) tile;

            if (itemStack != null) {
                if (isFilledContainer(itemStack)) {
                    Item item = itemStack.getItem();

                    if (item instanceof IFluidContainerItem) {
                        FluidStack fluidStack = ((IFluidContainerItem) item).getFluid(itemStack);

                        if (fluidHandler.fill(side, fluidStack, false) > 0) {
                            if (!player.capabilities.isCreativeMode) {
                                player.inventory.decrStackSize(player.inventory.currentItem, 1);
                            }

                            fluidHandler.fill(side, fluidStack, true);

                            return true;
                        }
                    }
                } else if (isEmptyContainer(itemStack)) {
                    FluidStack available = fluidHandler.drain(side, Integer.MAX_VALUE, false);

                    if (available != null) {
                        ItemStack itemStackFilled = itemStack.copy();
                        itemStackFilled.stackSize = 1;
                        itemStackFilled = getFilledContainer(itemStackFilled, available);
                        Item itemFilled = itemStackFilled.getItem();

                        if (itemFilled instanceof IFluidContainerItem) {
                            FluidStack fluidStack = ((IFluidContainerItem) itemFilled).getFluid(itemStackFilled);

                            if (fluidStack != null) {
                                fluidHandler.drain(side, fluidStack.amount, true);

                                if (!player.capabilities.isCreativeMode) {
                                    player.inventory.addItemStackToInventory(itemStackFilled);
                                    player.inventory.decrStackSize(player.inventory.currentItem, 1);
                                }

                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }
}