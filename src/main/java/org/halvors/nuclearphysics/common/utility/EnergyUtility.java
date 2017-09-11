package org.halvors.nuclearphysics.common.utility;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.halvors.nuclearphysics.common.tile.ITileRedstoneControl;

public class EnergyUtility {
    /**
     * Universally discharges an item, and updates the TileEntity's energy level.
     * @param slot - ID of the slot of which to charge
     * @param tile - TileEntity the item is being charged in.
     */
    public static void discharge(int slot, TileEntity tile) {
        if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, null)) {
            IEnergyStorage energyStorage = tile.getCapability(CapabilityEnergy.ENERGY, null);

            if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                ItemStack itemStack = inventory.getStackInSlot(slot);

                if (!itemStack.isEmpty() && energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()) {
                    if (itemStack.hasCapability(CapabilityEnergy.ENERGY, null)) {
                        IEnergyStorage itemEnergyStorage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);

                        if (itemEnergyStorage.canExtract()) {
                            int needed = Math.round(Math.min(Integer.MAX_VALUE, (energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored())));

                            energyStorage.receiveEnergy(itemEnergyStorage.extractEnergy(needed, false), false);
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
        if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, null)) {
            IEnergyStorage energyStorage = tile.getCapability(CapabilityEnergy.ENERGY, null);

            if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                ItemStack itemStack = inventory.getStackInSlot(slot);

                if (!itemStack.isEmpty() && energyStorage.getEnergyStored() > 0) {
                    if (itemStack.hasCapability(CapabilityEnergy.ENERGY, null)) {
                        IEnergyStorage itemEnergyStorage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);

                        if (itemEnergyStorage.canReceive()) {
                            int stored = Math.round(Math.min(Integer.MAX_VALUE, energyStorage.getEnergyStored()));

                            itemEnergyStorage.extractEnergy(energyStorage.receiveEnergy(stored, false), false);
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
        return itemStack.hasCapability(CapabilityEnergy.ENERGY, null) && itemStack.getCapability(CapabilityEnergy.ENERGY, null).canExtract();
    }

    /**
     * Whether or not a defined ItemStack can be charged with energy in some way.
     * @param itemStack - ItemStack to check
     * @return if the ItemStack can be discharged
     */
    public static boolean canBeCharged(ItemStack itemStack) {
        return itemStack.hasCapability(CapabilityEnergy.ENERGY, null) && itemStack.getCapability(CapabilityEnergy.ENERGY, null).canReceive();
    }

    /**
     * Whether or not a defined deemed-electrical ItemStack can be outputted out of a slot.
     * This puts into account whether or not that slot is used for charging or discharging.
     * @param itemStack - ItemStack to perform the check on
     * @param chargeSlot - whether or not the outputting slot is for charging or discharging
     * @return if the ItemStack can be outputted
     */
    public static boolean canBeOutputted(ItemStack itemStack, boolean chargeSlot) {
        if (itemStack.hasCapability(CapabilityEnergy.ENERGY, null)) {
            IEnergyStorage storage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);

            if (chargeSlot) {
                return !storage.canReceive();
            } else {
                return !storage.canExtract();
            }
        }

        return true;
    }
}
