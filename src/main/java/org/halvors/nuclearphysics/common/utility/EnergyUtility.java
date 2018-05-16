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

                if (canBeDischarged(itemStack)) {
                    if (itemStack.hasCapability(CapabilityEnergy.ENERGY, null)) {
                        final IEnergyStorage itemEnergyStorage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);

                        if (energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()) {
                            final int needed = Math.round(Math.min(Integer.MAX_VALUE, (energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored())));

                            energyStorage.receiveEnergy(itemEnergyStorage.extractEnergy(needed, false), false);
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
        return itemStack != null && itemStack.hasCapability(CapabilityEnergy.ENERGY, null) && itemStack.getCapability(CapabilityEnergy.ENERGY, null).canExtract();
    }
}
