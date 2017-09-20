package org.halvors.nuclearphysics.common.recipe.old.input;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.halvors.nuclearphysics.common.utility.OreDictionaryHelper;

public class AdvancedMachineInput extends MachineInput<AdvancedMachineInput> {
    public ItemStack itemStack;
    public Fluid fluid;

    public AdvancedMachineInput(ItemStack itemStack, Fluid fluid) {
        this.itemStack = itemStack;
        this.fluid = fluid;
    }

    public AdvancedMachineInput() {
    }

    @Override
    public void load(NBTTagCompound nbtTags) {
        itemStack = ItemStack.loadItemStackFromNBT(nbtTags.getCompoundTag("input"));
        fluid = FluidRegistry.getFluid(nbtTags.getString("fluid"));
    }

    @Override
    public AdvancedMachineInput copy() {
        return new AdvancedMachineInput(itemStack.copy(), fluid);
    }

    @Override
    public boolean isValid() {
        return itemStack != null && fluid != null;
    }

    public boolean useItem(IItemHandlerModifiable inventory, int index, boolean deplete) {
        if (inputContains(inventory.getStackInSlot(index), itemStack)) {
            if (deplete) {
                // TODO: Consume item?
                //inventory[index] = StackUtils.subtract(inventory[index], itemStack);
            }

            return true;
        }

        return false;
    }

    public boolean useSecondary(FluidTank fluidTank, int amountToUse, boolean deplete) {
        if (fluidTank.getFluid().getFluid() == fluid && fluidTank.getFluidAmount() >= amountToUse) {
            fluidTank.drain(amountToUse, deplete);
            return true;
        }

        return false;
    }

    public boolean matches(AdvancedMachineInput input) {
        return OreDictionaryHelper.equalsWildcard(itemStack, input.itemStack) && input.itemStack.stackSize >= itemStack.stackSize;
    }

    @Override
    public boolean testEquality(AdvancedMachineInput other) {
        if (!isValid()) {
            return !other.isValid();
        }

        return OreDictionaryHelper.equalsWildcardWithNBT(itemStack, other.itemStack) && fluid == other.fluid;
    }
}

