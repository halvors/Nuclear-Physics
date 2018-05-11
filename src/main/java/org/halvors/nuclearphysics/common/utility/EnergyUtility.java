package org.halvors.nuclearphysics.common.utility;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class EnergyUtility {
    /**
     * Universally discharges an item, and updates the TileEntity's energy level.
     * @param slot - ID of the slot of which to charge
     * @param tile - TileEntity the item is being charged in.
     */
    public static void discharge(final int slot, final TileEntity tile) {
        if (tile instanceof IEnergyReceiver) {
            final IEnergyReceiver energyReceiver = (IEnergyReceiver) tile;

            if (tile instanceof IInventory) {
                final IInventory inventory = (IInventory) tile;
                final ItemStack itemStack = inventory.getStackInSlot(slot);

                if (canBeDischarged(itemStack)) {
                    final Item item = itemStack.getItem();

                    if (item instanceof IEnergyContainerItem) {
                        final IEnergyContainerItem itemEnergyContainer = (IEnergyContainerItem) item;
                        final int needed = Math.round(Math.min(Integer.MAX_VALUE, (energyReceiver.getMaxEnergyStored(null) - energyReceiver.getEnergyStored(null))));

                        if (energyReceiver.receiveEnergy(null, itemEnergyContainer.extractEnergy(itemStack, needed, true), true) > 0) {
                            energyReceiver.receiveEnergy(null, itemEnergyContainer.extractEnergy(itemStack, needed, false), false);
                        }
                    }
                }
            }
        }
    }

    /**
     * Whether or not a defined ItemStack can be discharged for energy in some way.
     * @param itemStack - ItemStack to check
     * @return if the ItemStack can be discharged
     */
    public static boolean canBeDischarged(final ItemStack itemStack) {
        if (itemStack != null) {
            final Item item = itemStack.getItem();

            return item instanceof IEnergyContainerItem && ((IEnergyContainerItem) item).extractEnergy(itemStack, 1, true) > 0;
        }

        return false;
    }
}