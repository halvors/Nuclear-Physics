package org.halvors.nuclearphysics.common.utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import org.halvors.nuclearphysics.common.init.ModItems;
import org.halvors.nuclearphysics.common.item.ItemCell;
import org.halvors.nuclearphysics.common.type.Position;

public class FluidUtility {
    public static boolean isEmptyContainer(final ItemStack itemStack) {
        return FluidContainerRegistry.isEmptyContainer(itemStack);
    }

    public static boolean isFilledContainer(final ItemStack itemStack) {
        final Item item = itemStack.getItem();

        if (item instanceof IFluidContainerItem) {
            final IFluidContainerItem fluidContainerItem = (IFluidContainerItem) item;
            FluidStack fluidStack = fluidContainerItem.getFluid(itemStack);

            return fluidStack != null && fluidStack.amount > 0;
        }

        return FluidContainerRegistry.isFilledContainer(itemStack);
    }

    public static boolean isFilledContainer(final ItemStack itemStack, final Fluid fluid) {
        final Item item = itemStack.getItem();
        FluidStack fluidStack;

        if (item instanceof IFluidContainerItem) {
            final IFluidContainerItem fluidContainerItem = (IFluidContainerItem) item;
            fluidStack = fluidContainerItem.getFluid(itemStack);
        } else {
            fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStack);
        }

        if (fluid != null && fluidStack != null) {
            return fluid == fluidStack.getFluid() && fluidStack.amount > 0;
        }

        return false;
    }

    public static ItemStack getFilledCell(final Fluid fluid) {
        return getFilledContainer(new ItemStack(ModItems.itemCell), new FluidStack(fluid, ItemCell.capacity));
    }

    public static ItemStack getFilledContainer(final ItemStack itemStack, final FluidStack fluidStack) {
        final Item item = itemStack.getItem();

        if (item instanceof IFluidContainerItem) {
            final IFluidContainerItem fluidContainerItem = (IFluidContainerItem) item;
            fluidContainerItem.fill(itemStack, fluidStack, true);
        }

        return itemStack;
    }

    public static void transferFluidToNeighbors(final World world, final int x, final int y, final int z, final IFluidHandler from) {
        if (from != null) {
            for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                final Position pos = new Position(x, y, z).offset(side);
                final TileEntity tile = world.getTileEntity(pos.getIntX(), pos.getIntY(), pos.getIntZ());

                if (tile instanceof IFluidHandler) {
                    final IFluidHandler to = (IFluidHandler) tile;
                    final FluidStack fluidStack = from.drain(side.getOpposite(), Integer.MAX_VALUE, false);

                    if (fluidStack != null && to.fill(side.getOpposite(), fluidStack, false) > 0) {
                        to.fill(side.getOpposite(), from.drain(side.getOpposite(), fluidStack, true), true);
                    }
                }
            }
        }
    }

    // Does all the work needed to fill or drain an item of fluid when a player clicks on the block.
    public static boolean playerActivatedFluidItem(final World world, final int x, final int y, final int z, final EntityPlayer player, final ItemStack itemStack, final ForgeDirection side) {
        final TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof IFluidHandler) {
            final IFluidHandler fluidHandler = (IFluidHandler) tile;

            if (itemStack != null) {
                if (isFilledContainer(itemStack)) {
                    final Item item = itemStack.getItem();

                    if (item instanceof IFluidContainerItem) {
                        final FluidStack fluidStack = ((IFluidContainerItem) item).getFluid(itemStack);

                        if (fluidHandler.fill(side, fluidStack, false) > 0) {
                            if (!player.capabilities.isCreativeMode) {
                                player.inventory.decrStackSize(player.inventory.currentItem, 1);
                            }

                            fluidHandler.fill(side, fluidStack, true);

                            return true;
                        }
                    }
                } else if (isEmptyContainer(itemStack)) {
                    final FluidStack available = fluidHandler.drain(side, Integer.MAX_VALUE, false);

                    if (available != null) {
                        ItemStack itemStackFilled = itemStack.copy();
                        itemStackFilled.stackSize = 1;
                        itemStackFilled = getFilledContainer(itemStackFilled, available);
                        final Item itemFilled = itemStackFilled.getItem();

                        if (itemFilled instanceof IFluidContainerItem) {
                            final FluidStack fluidStack = ((IFluidContainerItem) itemFilled).getFluid(itemStackFilled);

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