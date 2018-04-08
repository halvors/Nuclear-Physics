package org.halvors.nuclearphysics.common.utility;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyStorage;
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
    public static void discharge(int slot, TileEntity tile) {
        if (tile instanceof IEnergyStorage) {
            final IEnergyStorage energyStorage = (IEnergyStorage) tile;

            if (tile instanceof IInventory) {
                final IInventory inventory = (IInventory) tile;
                final ItemStack itemStack = inventory.getStackInSlot(slot);

                if (itemStack != null && energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()) {
                    final Item item = itemStack.getItem();

                    if (item instanceof IEnergyContainerItem) {
                        final IEnergyContainerItem itemEnergyContainer = (IEnergyContainerItem) item;

                        if (canBeDischarged(itemStack)) {
                            int needed = Math.round(Math.min(Integer.MAX_VALUE, (energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored())));

                            energyStorage.receiveEnergy(itemEnergyContainer.extractEnergy(itemStack, needed, false), false);
                        }
                    }
                }
            }
        }
    }

    /**
     * Universally charges an item, and updates the TileEntity's energy level.
     * @param slot - ID of the slot of which to discharge
     * @param tile - TileEntity the item is being discharged in
     */
    public static void charge(int slot, TileEntity tile) {
        if (tile instanceof IEnergyStorage) {
            final IEnergyStorage energyStorage = (IEnergyStorage) tile;

            if (tile instanceof IInventory) {
                final IInventory inventory = (IInventory) tile;
                final ItemStack itemStack = inventory.getStackInSlot(slot);

                if (itemStack != null && energyStorage.getEnergyStored() > 0) {
                    final Item item = itemStack.getItem();

                    if (item instanceof IEnergyContainerItem) {
                        final IEnergyContainerItem itemEnergyContainer = (IEnergyContainerItem) item;

                        if (canBeCharged(itemStack)) {
                            int stored = Math.round(Math.min(Integer.MAX_VALUE, energyStorage.getEnergyStored()));

                            itemEnergyContainer.extractEnergy(itemStack, energyStorage.receiveEnergy(stored, false), false);
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
    public static boolean canBeDischarged(ItemStack itemStack) {
        final Item item = itemStack.getItem();

        return item instanceof IEnergyContainerItem && ((IEnergyContainerItem) item).extractEnergy(itemStack, 1, true) > 0;
    }

    /**
     * Whether or not a defined ItemStack can be charged with energy in some way.
     * @param itemStack - ItemStack to check
     * @return if the ItemStack can be discharged
     */
    public static boolean canBeCharged(ItemStack itemStack) {
        final Item item = itemStack.getItem();

        return item instanceof IEnergyContainerItem && ((IEnergyContainerItem) item).receiveEnergy(itemStack, 1, true) > 0;
    }

    /**
     * Whether or not a defined deemed-electrical ItemStack can be outputted out of a slot.
     * This puts into account whether or not that slot is used for charging or discharging.
     * @param itemStack - ItemStack to perform the check on
     * @param chargeSlot - whether or not the outputting slot is for charging or discharging
     * @return if the ItemStack can be outputted
     */
    public static boolean canBeOutputted(ItemStack itemStack, boolean chargeSlot) {
        final Item item = itemStack.getItem();

        if (item instanceof IEnergyContainerItem) {
            IEnergyContainerItem energyContainer = (IEnergyContainerItem) item;

            if (chargeSlot) {
                return energyContainer.receiveEnergy(itemStack, 1, true) > 0;
            } else {
                return energyContainer.extractEnergy(itemStack, 1, true) > 0;
            }
        }

        return true;
    }
}
