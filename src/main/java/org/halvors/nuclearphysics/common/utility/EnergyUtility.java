package org.halvors.nuclearphysics.common.utility;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyReceiver;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.Integration;

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

                    if (Integration.isRedstoneFluxLoaded && item instanceof IEnergyContainerItem) {
                        final IEnergyContainerItem itemEnergyContainer = (IEnergyContainerItem) item;
                        final int needed = Math.round(Math.min(Integer.MAX_VALUE, (energyReceiver.getMaxEnergyStored(null) - energyReceiver.getEnergyStored(null))));

                        if (energyReceiver.receiveEnergy(null, itemEnergyContainer.extractEnergy(itemStack, needed, true), true) > 0) {
                            energyReceiver.receiveEnergy(null, itemEnergyContainer.extractEnergy(itemStack, needed, false), false);
                        }
                    } else if (Integration.isIC2Loaded && item instanceof IElectricItem) {
                        final IElectricItem electricItem = (IElectricItem) item;

                        if (electricItem.canProvideEnergy(itemStack)) {
                            final int needed = (int) ((energyReceiver.getMaxEnergyStored(null) - energyReceiver.getEnergyStored(null)) * General.toIC2);

                            if (energyReceiver.receiveEnergy(null, (int) (ElectricItem.manager.discharge(itemStack, needed, 4, true, true, true) * General.fromIC2), true) > 0) {
                                energyReceiver.receiveEnergy(null, (int) (ElectricItem.manager.discharge(itemStack, needed, 4, true, true, false) * General.fromIC2), false);
                            }
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

            return item instanceof IEnergyContainerItem && ((IEnergyContainerItem) item).extractEnergy(itemStack, 1, true) > 0 ||
                   item instanceof IElectricItem && ((IElectricItem) item).canProvideEnergy(itemStack);
        }

        return false;
    }
}