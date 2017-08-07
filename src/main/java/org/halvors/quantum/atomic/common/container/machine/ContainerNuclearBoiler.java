package org.halvors.quantum.atomic.common.container.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.items.SlotItemHandler;
import org.halvors.quantum.atomic.common.QuantumFluids;
import org.halvors.quantum.atomic.common.container.ContainerQuantum;
import org.halvors.quantum.atomic.common.tile.machine.TileNuclearBoiler;

public class ContainerNuclearBoiler extends ContainerQuantum {
    private static final int slotCount = 4;
    private TileNuclearBoiler tile;

    public ContainerNuclearBoiler(InventoryPlayer inventoryPlayer, TileNuclearBoiler tile) {
        super(inventoryPlayer, tile);

        this.tile = tile;

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

        addPlayerInventory(inventoryPlayer.player);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        //return tile.inventory.isUsableByPlayer(player);
        return true;
    }

    /** Called to transfer a stack from one inventory to the other eg. when shift clicking. */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        ItemStack copyStack = null;
        Slot slot = inventorySlots.get(slotId);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack = slot.getStack();

            if (itemStack != null) {
                copyStack = itemStack.copy();

                if (slotId >= slotCount) {
                    if (getSlot(0).isItemValid(itemStack)) {
                        if (!mergeItemStack(itemStack, 0, 1, false)) {
                            return null;
                        }
                    } else if (QuantumFluids.fluidStackWater.isFluidEqual(FluidContainerRegistry.getFluidForFilledItem(itemStack))) {
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

                if (itemStack.stackSize == 0) {
                    slot.putStack(null);
                } else {
                    slot.onSlotChanged();
                }

                if (itemStack.stackSize == copyStack.stackSize) {
                    return null;
                }

                slot.onPickupFromSlot(player, itemStack);
            }
        }

        return copyStack;
    }
}
