package org.halvors.nuclearphysics.common.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

/**
 * Slot for empty and partial-empty containers accepting given fluids
 *
 * @author nictrace
 */
public class SlotFluidContainerEmpty extends Slot {
    private FluidStack[] validFluidStacks;
    private FluidContainerData[] fluidContainerDatas;

    /**
     * Slot for empty or partial empty simple & advanced fluid containers
     *
     * @param validFluidStacks - array of FluidStacks that can be accepted by inserted containers
     */
    public SlotFluidContainerEmpty(IInventory inventory, int index, int x, int y, FluidStack... validFluidStacks) {
        super(inventory, index, x, y);

        this.validFluidStacks = validFluidStacks;
        this.fluidContainerDatas = FluidContainerRegistry.getRegisteredFluidContainerData();
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        final Item item = itemStack.getItem();

        if (FluidContainerRegistry.isContainer(itemStack)) {
            if (FluidContainerRegistry.isEmptyContainer(itemStack)) {
                for (final FluidContainerData fluidContainerData : fluidContainerDatas) {
                    if (itemStack.isItemEqual(fluidContainerData.emptyContainer) && inFluidArray(validFluidStacks, fluidContainerData.fluid)) {
                        return true;    // this container can contains at least one of given liquids
                    }
                }
            }
        } else if (item instanceof IFluidContainerItem) {
            // I think it can contains *any* of registered liquids
            final IFluidContainerItem tank = (IFluidContainerItem) item;
            final FluidStack probe = tank.getFluid(itemStack);

            if (probe == null) {
                return true;    // tank is empty
            }

            // in case of not empty
            return (probe.amount < tank.getCapacity(itemStack)) && (inFluidArray(validFluidStacks, probe));
        }

        return false;
    }

    protected boolean inFluidArray(FluidStack[] store, FluidStack fluidStack) {
        for (final FluidStack validFluidStack : store) {
            if (validFluidStack.isFluidEqual(fluidStack)) {
                return true;
            }
        }

        return false;
    }
}
