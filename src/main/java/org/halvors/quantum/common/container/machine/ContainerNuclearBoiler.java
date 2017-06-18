package org.halvors.quantum.common.container.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.tile.machine.TileNuclearBoiler;
import org.halvors.quantum.lib.container.ContainerBase;
import org.halvors.quantum.lib.container.slot.SlotEnergyItem;
import org.halvors.quantum.lib.container.slot.SlotSpecific;

public class ContainerNuclearBoiler extends ContainerBase {
    private static final int slotCount = 4;
    private TileNuclearBoiler tile;

    public ContainerNuclearBoiler(InventoryPlayer inventoryPlayer, TileNuclearBoiler tile) {
        super(tile);

        this.tile = tile;

        // Battery
        addSlotToContainer(new SlotEnergyItem(tile, 0, 56, 26));

        // Yellowcake Input
        addSlotToContainer(new SlotSpecific(tile, 1, 81, 26, new ItemStack(Quantum.itemYellowCake), new ItemStack(Quantum.blockUraniumOre)));

        // Fluid input fill
        addSlotToContainer(new Slot(tile, 2, 25, 19));

        // Fluid input drain
        addSlotToContainer(new Slot(tile, 3, 25, 50));

        // Fluid output drain
        addSlotToContainer(new Slot(tile, 4, 135, 50));

        addPlayerInventory(inventoryPlayer.player);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tile.isUseableByPlayer(player);
    }

    /** Called to transfer a stack from one inventory to the other eg. when shift clicking. */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        ItemStack copyStack = null;
        Slot slot = (Slot) inventorySlots.get(slotId);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack = slot.getStack();
            copyStack = itemStack.copy();

            if (slotId >= slotCount) {
                if (getSlot(0).isItemValid(itemStack)) {
                    if (!mergeItemStack(itemStack, 0, 1, false)) {
                        return null;
                    }
                } else if (Quantum.fluidStackWater.isFluidEqual(FluidContainerRegistry.getFluidForFilledItem(itemStack))) {
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

        return copyStack;
    }
}
