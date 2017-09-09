package org.halvors.nuclearphysics.common.container.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.SlotItemHandler;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.tile.machine.TileNuclearBoiler;

public class ContainerNuclearBoiler extends ContainerBase<TileNuclearBoiler> {
    public ContainerNuclearBoiler(InventoryPlayer inventoryPlayer, TileNuclearBoiler tile) {
        super(inventoryPlayer, tile);

        slotCount = 4;

        // Battery
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 0, 56, 26));

        // Yellowcake Input
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 1, 81, 26));

        // Fluid input fill
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 2, 25, 19));

        // Fluid input drain
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 3, 25, 50));

        // Fluid output drain
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 4, 135, 50));

        // Players inventory
        addPlayerInventory(inventoryPlayer.player);
    }

    /** Called to transfer a stack from one inventory to the other eg. when shift clicking. */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        ItemStack copyStack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(slotId);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack = slot.getStack();

            if (!itemStack.isEmpty()) {
                copyStack = itemStack.copy();

                if (slotId >= slotCount) {
                    if (getSlot(0).isItemValid(itemStack)) {
                        if (!mergeItemStack(itemStack, 0, 1, false)) {
                            return null;
                        }
                    } else if (ModFluids.fluidStackWater.isFluidEqual(FluidUtil.getFluidContained(itemStack))) {
                        if (!mergeItemStack(itemStack, 1, 2, false)) {
                            return null;
                        }
                    } else if (getSlot(3).isItemValid(itemStack)) {
                        if (!mergeItemStack(itemStack, 3, 4, false)) {
                            return null;
                        }
                    } else if (slotId < 27 + slotCount) {
                        if (!mergeItemStack(itemStack, 27 + slotCount, 36 + slotCount, false)) {
                            return null;
                        }
                    } else if (slotId >= 27 + slotCount && slotId < 36 + slotCount && !mergeItemStack(itemStack, 4, 30, false)) {
                        return null;
                    }
                } else if (!mergeItemStack(itemStack, slotCount, 36 + slotCount, false)) {
                    return null;
                }

                if (itemStack.isEmpty()) {
                    slot.putStack(ItemStack.EMPTY);
                } else {
                    slot.onSlotChanged();
                }

                if (itemStack.getCount() == copyStack.getCount()) {
                    return null;
                }

                slot.onTake(player, itemStack);
            }
        }

        return copyStack;
    }
}
