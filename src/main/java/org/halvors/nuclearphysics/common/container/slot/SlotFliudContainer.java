package org.halvors.nuclearphysics.common.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class SlotFliudContainer extends Slot {
    private final FluidStack validFluidStacks[];

    /**
     * Slot for simple & advanced filled fiuid containers
     *
     * @param validFluidStacks - array of FluidStacks that can be accepted by input tank
     */
    public SlotFliudContainer(IInventory inventory, int index, int x, int y, FluidStack... validFluidStacks) {
        super(inventory, index, x, y);

        this.validFluidStacks = validFluidStacks;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        final Item item = itemStack.getItem();

        if (FluidContainerRegistry.isContainer(itemStack)) {
            if (FluidContainerRegistry.isFilledContainer(itemStack)) {
                for (final FluidStack fluidStackValid : validFluidStacks) {
                    if (FluidContainerRegistry.containsFluid(itemStack, fluidStackValid)) {
                        return true;
                    }
                }
            }
        }

        if (item instanceof IFluidContainerItem) {
            final IFluidContainerItem fluidContainerItem = (IFluidContainerItem) item;
            final FluidStack fluidStack = fluidContainerItem.getFluid(itemStack);

            if (fluidStack != null) {
                for (final FluidStack fluidStackValid : validFluidStacks) {
                    if (fluidStack.isFluidEqual(fluidStackValid)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
