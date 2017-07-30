package org.halvors.quantum.common.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import org.halvors.quantum.common.QuantumItems;

import javax.annotation.Nonnull;

public class ItemCell extends ItemMetadata {
    public ItemCell(String name) {
        super(name);

        if (!name.equalsIgnoreCase("empty_cell")) {
            setContainerItem(QuantumItems.itemCell);
        }
    }

    @Override
    @Nonnull
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new FluidHandlerItemStackSimple(stack, 200);
    }
}