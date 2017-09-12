package org.halvors.nuclearphysics.common.tile;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class TileRotatable extends TileBase implements ITileNetwork, ITileRotatable {
    protected EnumFacing facing = EnumFacing.NORTH;

    public TileRotatable() {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        if (tag.hasKey("facing")) {
            facing = EnumFacing.getFront(tag.getInteger("facing"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (facing != null) {
            tag.setInteger("facing", facing.ordinal());
        }

        return tag;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        if (world.isRemote) {
            facing = EnumFacing.getFront(dataStream.readInt());
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        objects.add(facing.ordinal());

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public boolean canSetFacing(EnumFacing facing) {
        return Arrays.asList(EnumFacing.HORIZONTALS).contains(facing);
    }

    @Override
    public EnumFacing getFacing() {
        return facing;
    }

    @Override
    public void setFacing(EnumFacing facing) {
        this.facing = facing;

        NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
    }
}
