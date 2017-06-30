package org.halvors.quantum.common.utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * Fluid interactions.
 *
 * @author DarkCow, Calclavia
 */
public class FluidUtility {
    // Does all the work needed to fill or drain an item of fluid when a player clicks on the block.
    public static boolean playerActivatedFluidItem(World world, BlockPos pos, EntityPlayer entityplayer, EnumFacing side) {
        ItemStack current = entityplayer.inventory.getCurrentItem();

        if (current != null && world.getTileEntity(pos) instanceof IFluidHandler) {
            FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(current);
            IFluidHandler tank = (IFluidHandler) world.getTileEntity(pos);

            if (FluidContainerRegistry.isFilledContainer(current)) {
                if (tank.fill(side, fluid, false) == fluid.amount) {
                    tank.fill(side, fluid, true);

                    if (!entityplayer.capabilities.isCreativeMode) {
                        InventoryUtility.consumeHeldItem(entityplayer);
                    }

                    return true;
                }
            } else if (FluidContainerRegistry.isEmptyContainer(current)) {
                FluidStack available = tank.drain(side, Integer.MAX_VALUE, false);

                if (available != null) {
                    ItemStack filled = FluidContainerRegistry.fillFluidContainer(available, current);

                    fluid = FluidContainerRegistry.getFluidForFilledItem(filled);

                    if (fluid != null) {
                        /*
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
                        */

                        tank.drain(null, fluid.amount, true);

                        return true;
                    }
                }
            }
        }

        return false;
    }
}
