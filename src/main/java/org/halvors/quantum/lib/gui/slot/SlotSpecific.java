package org.halvors.quantum.lib.gui.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/* Creates a slot with a specific amount of items that matches the slot's requirements. Allows easy
 * shift right clicking management and slot blocking in classes. In your container you can use
 * this.getSlot(i).isItemValid to justify the player's shift clicking actions to match the slot.
 */
public class SlotSpecific extends Slot {
    public ItemStack[] validItemStacks = new ItemStack[0];
    public Class[] validClasses = new Class[0];

    public boolean isInverted = false;
    public boolean isMetadataSensitive = false;

    public SlotSpecific(IInventory inventory, int par3, int par4, int par5, ItemStack... itemStacks) {
        super(inventory, par3, par4, par5);

        setItemStacks(itemStacks);
    }

    public SlotSpecific(IInventory inventory, int par3, int par4, int par5, Class... validClasses) {
        super(inventory, par3, par4, par5);

        setClasses(validClasses);
    }

    public SlotSpecific setMetadataSensitive() {
        isMetadataSensitive = true;

        return this;
    }

    public SlotSpecific setItemStacks(ItemStack... validItemStacks) {
        this.validItemStacks = validItemStacks;

        return this;
    }

    public SlotSpecific setClasses(Class... validClasses) {
        this.validClasses = validClasses;

        return this;
    }

    public SlotSpecific toggleInverted() {
        isInverted = !isInverted;

        return this;
    }

    // Check if the stack is a valid item for this slot. Always true beside for the armor slots.
    @Override
    public boolean isItemValid(ItemStack compareStack) {
        boolean returnValue = false;

        for (ItemStack itemStack : validItemStacks) {
            if (compareStack.isItemEqual(itemStack) || (!isMetadataSensitive && compareStack == itemStack)) {
                returnValue = true;
                break;
            }
        }

        if (!returnValue) {
            for (Class clazz : validClasses) {
                if (clazz.equals(compareStack.getItem().getClass()) || clazz.isInstance(compareStack.getItem())) {
                    returnValue = true;

                    break;
                }
            }
        }

        if (isInverted) {
            return !returnValue;
        }

        return returnValue;
    }
}
