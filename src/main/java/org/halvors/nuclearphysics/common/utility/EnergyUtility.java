package org.halvors.nuclearphysics.common.utility;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class EnergyUtility {
    /**
     * Universally discharges an item, and updates the TileEntity's energy level.
     * @param slot - ID of the slot of which to charge
     * @param tile - TileEntity the item is being charged in.
     */
    public static void discharge(final int slot, final TileEntity tile) {
        if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, null)) {
            final IEnergyStorage energyStorage = tile.getCapability(CapabilityEnergy.ENERGY, null);

            if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                final IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                final ItemStack itemStack = inventory.getStackInSlot(slot);

                if (!itemStack.isEmpty() && energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()) {
                    if (itemStack.hasCapability(CapabilityEnergy.ENERGY, null)) {
                        final IEnergyStorage itemEnergyStorage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);

                        if (itemEnergyStorage.canExtract()) {
                            final int needed = Math.round(Math.min(Integer.MAX_VALUE, (energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored())));

                            energyStorage.receiveEnergy(itemEnergyStorage.extractEnergy(needed, false), false);
                        }
                    }
                }
            }
        }
    }

    /**
<<<<<<< HEAD
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
=======
>>>>>>> 1.10.2
     * Whether or not a defined ItemStack can be discharged for energy in some way.
     * @param itemStack - ItemStack to check
     * @return if the ItemStack can be discharged
     */
    public static boolean canBeDischarged(final ItemStack itemStack) {
        return itemStack.hasCapability(CapabilityEnergy.ENERGY, null) && itemStack.getCapability(CapabilityEnergy.ENERGY, null).canExtract();
    }
}
