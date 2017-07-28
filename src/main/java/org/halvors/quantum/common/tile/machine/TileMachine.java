package org.halvors.quantum.common.tile.machine;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import org.halvors.quantum.common.tile.ITileNetwork;
import org.halvors.quantum.common.tile.TileConsumer;

import java.util.List;

public class TileMachine extends TileConsumer implements ITileNetwork {
    // How many ticks has this item been processed for?
    public int timer = 0; // Synced

    public TileMachine() {

    }

    public TileMachine(int maxSlots) {
        super(maxSlots);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        timer = tag.getInteger("timer");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);

        tag.setInteger("timer", timer);

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
}
