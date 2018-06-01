package org.halvors.nuclearphysics.common.utility;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.halvors.nuclearphysics.common.ConfigurationManager;
import org.halvors.nuclearphysics.common.Integration;

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

                if (canBeDischarged(itemStack) && energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()) {
                    final Item item = itemStack.getItem();

                    if (itemStack.hasCapability(CapabilityEnergy.ENERGY, null)) {
                        final IEnergyStorage itemEnergyStorage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
                        final int needed = Math.round(Math.min(Integer.MAX_VALUE, (energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored())));

                        energyStorage.receiveEnergy(itemEnergyStorage.extractEnergy(needed, false), false);
                    } else if (Integration.isIC2Loaded && item instanceof IElectricItem) {
                        final int needed = (int) ((energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored()) * ConfigurationManager.General.toIC2);

                        energyStorage.receiveEnergy((int) (ElectricItem.manager.discharge(itemStack, needed, 4, true, true, false) * ConfigurationManager.General.fromIC2), false);
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

            return itemStack.hasCapability(CapabilityEnergy.ENERGY, null) && itemStack.getCapability(CapabilityEnergy.ENERGY, null).canExtract() ||
                   item instanceof IElectricItem && ((IElectricItem) item).canProvideEnergy(itemStack);
        }

        return false;
    }
}
