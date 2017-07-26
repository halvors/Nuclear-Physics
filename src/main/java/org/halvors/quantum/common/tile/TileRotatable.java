package org.halvors.quantum.common.tile;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.network.packet.PacketTileEntity;

import java.util.List;

public class TileRotatable extends TileElectricInventory implements ITileNetwork, ITileRotatable {
    /** The direction this block is facing. */
    public EnumFacing facing = EnumFacing.NORTH;

    public TileRotatable() {

    }

    public TileRotatable(int maxSlots) {
        super(maxSlots);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        if (tagCompound.hasKey("facing")) {
            facing = EnumFacing.getFront(tagCompound.getInteger("facing"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        if (facing != null) {
            tagCompound.setInteger("facing", facing.ordinal());
        }

        return tagCompound;
    }

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

    @Override
    public EnumFacing getFacing() {
        return facing;
    }

    @Override
    public void setFacing(EnumFacing facing) {
        this.facing = facing;

        Quantum.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
    }
}
