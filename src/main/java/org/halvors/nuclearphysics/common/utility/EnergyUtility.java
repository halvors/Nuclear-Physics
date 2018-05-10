package org.halvors.nuclearphysics.common.utility;

import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.tile.TileProducer;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyReceiver;
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
    public static void discharge(final int slot, final TileEntity tile) {
    	NuclearPhysics.getLogger().warn("EnergyUtility::discharge()");
        if (tile instanceof IEnergyStorage) {
            final IEnergyStorage energyStorage = (IEnergyStorage) tile;	// no, IEnergyReceiver

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
        else if(tile instanceof TileProducer){
        	final IEnergyStorage energyStorage = ((TileProducer)tile).getEnergyStorage();	// tile's capacitor
        	if(tile instanceof IInventory) {
                final IInventory inventory = (IInventory) tile;
                final ItemStack itemStack = inventory.getStackInSlot(slot);
                if (itemStack != null && energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()) {
                    final Item item = itemStack.getItem();

                    if (item instanceof IEnergyContainerItem) {
                        final IEnergyContainerItem itemEnergyContainer = (IEnergyContainerItem) item; // battery

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
     * Whether or not a defined ItemStack can be discharged for energy in some way.
     * @param itemStack - ItemStack to check
     * @return if the ItemStack can be discharged
     */
    public static boolean canBeDischarged(final ItemStack itemStack) {
        final Item item = itemStack.getItem();

        return item instanceof IEnergyContainerItem && ((IEnergyContainerItem) item).extractEnergy(itemStack, 1, true) > 0;
    }
}
