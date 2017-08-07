package org.halvors.quantum.atomic.common.tile.machine;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.halvors.quantum.atomic.common.tile.ITileNetwork;
import org.halvors.quantum.atomic.common.tile.TileConsumer;

import javax.annotation.Nonnull;
import java.util.List;

public class TileMachine extends TileConsumer implements ITileNetwork {
    // How many ticks has this item been processed for?
    public int timer = 0; // Synced

    protected IItemHandlerModifiable inventory;

    public TileMachine() {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        timer = tag.getInteger("timer");

        if (tag.getTagId("Inventory") == Constants.NBT.TAG_LIST) {
            NBTTagList tagList = tag.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound slotTag = (NBTTagCompound) tagList.get(i);
                byte slot = slotTag.getByte("Slot");

                if (slot < inventory.getSlots()) {
                    inventory.setStackInSlot(slot, ItemStack.loadItemStackFromNBT(slotTag));
                }
            }

            return;
        }

        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inventory, null, tag.getTag("Slots"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);

        tag.setInteger("timer", timer);
        tag.setTag("Slots", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inventory, null));

        return tag;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (world.isRemote) {
            timer = dataStream.readInt();
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        super.getPacketData(objects);

        objects.add(timer);

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
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
