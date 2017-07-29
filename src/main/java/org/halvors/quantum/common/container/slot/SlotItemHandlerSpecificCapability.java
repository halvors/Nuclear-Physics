package org.halvors.quantum.common.container.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotItemHandlerSpecificCapability extends SlotItemHandler {
    private Capability<?> capability;

    public SlotItemHandlerSpecificCapability(IItemHandler inventory, int index, int x, int y, Capability<?> capability) {
        super(inventory, index, x, y);

        this.capability = capability;
    }

    @Override
    public boolean isItemValid(ItemStack compareStack) {
        return compareStack.hasCapability(capability, null);
    }
}