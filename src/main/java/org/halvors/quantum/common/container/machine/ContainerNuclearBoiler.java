package org.halvors.quantum.common.container.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.tile.machine.TileNuclearBoiler;
import org.halvors.quantum.lib.gui.ContainerBase;
import org.halvors.quantum.lib.gui.slot.SlotEnergyItem;
import org.halvors.quantum.lib.gui.slot.SlotSpecific;

public class ContainerNuclearBoiler extends ContainerBase {
    private static final int slotCount = 4;
    private TileNuclearBoiler tile;

    public ContainerNuclearBoiler(InventoryPlayer inventoryPlayer, TileNuclearBoiler tile) {
        super(tile);

        this.tile = tile;

        // Battery
        addSlotToContainer(new SlotEnergyItem(tile, 0, 56, 26));

        // Water Input
        addSlotToContainer(new Slot(tile, 1, 25, 50));

        // Gas Output
        addSlotToContainer(new Slot(tile, 2, 136, 50));

        // Yellowcake Input
        addSlotToContainer(new SlotSpecific(tile, 3, 81, 26, new ItemStack(Quantum.itemYellowCake), new ItemStack(Quantum.blockUraniumOre)));
        addPlayerInventory(inventoryPlayer.player);
        tile.openChest();
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
                if (this.getSlot(0).isItemValid(itemStack)) {
                    if (!mergeItemStack(itemStack, 0, 1, false)) {
                        return null;
                    }
                } else if (FluidContainerRegistry.getFluidForFilledItem(itemStack).isFluidEqual(Quantum.fluidStackWater)) {
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
