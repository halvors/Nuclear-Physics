package org.halvors.quantum.common.fluid.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;

public class FluidHandlerItem extends FluidHandlerItemStackSimple {
    /**
     * @param container The container itemStack, data is stored on it directly as NBT.
     * @param capacity  The maximum capacity of this fluid tank.
     */
    public FluidHandlerItem(ItemStack container, int capacity) {
        super(container, capacity);
    }

    @Override
    public void setFluid(FluidStack fluid) {
        super.setFluid(fluid);
    }
}
