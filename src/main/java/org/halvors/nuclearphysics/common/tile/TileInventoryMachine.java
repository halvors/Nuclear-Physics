package org.halvors.nuclearphysics.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.halvors.nuclearphysics.common.block.states.BlockStateMachine.EnumMachine;
import org.halvors.nuclearphysics.common.utility.InventoryUtility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileInventoryMachine extends TileMachine {
    private static final String NBT_SLOTS = "slots";

    protected IItemHandlerModifiable inventory;

    public TileInventoryMachine() {

    }

    public TileInventoryMachine(final EnumMachine type) {
        super(type);
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);

        InventoryUtility.readFromNBT(tag, inventory);

        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inventory, null, tag.getTag(NBT_SLOTS));
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setTag(NBT_SLOTS, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inventory, null));

        return tag;
    }

    @Override
    public boolean hasCapability(@Nonnull final Capability<?> capability, @Nullable final EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull final Capability<T> capability, @Nullable final EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) inventory;
        }

        return super.getCapability(capability, facing);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public IItemHandlerModifiable getInventory() {
        return inventory;
    }
}
