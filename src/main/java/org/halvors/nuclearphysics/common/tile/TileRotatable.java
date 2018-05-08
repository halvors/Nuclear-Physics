package org.halvors.nuclearphysics.common.tile;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;

import java.util.Arrays;
import java.util.List;

public class TileRotatable extends TileBase implements ITileNetwork, ITileRotatable {
    private static final String NBT_FACING = "facing";

    protected ForgeDirection facing = ForgeDirection.NORTH;

    public TileRotatable() {

    }

    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);

        if (tag.hasKey(NBT_FACING)) {
            facing = ForgeDirection.getOrientation(tag.getInteger(NBT_FACING));
        }
    }

    @Override
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (facing != null) {
            tag.setInteger(NBT_FACING, facing.ordinal());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(final ByteBuf dataStream) {
        if (worldObj.isRemote) {
            facing = ForgeDirection.getOrientation(dataStream.readInt());
        }
    }

    @Override
    public List<Object> getPacketData(final List<Object> objects) {
        objects.add(facing.ordinal());

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public boolean canSetFacing(final ForgeDirection facing) {
        return Arrays.asList(ForgeDirection.ROTATION_MATRIX).contains(facing);
    }

    @Override
    public ForgeDirection getFacing() {
        return facing;
    }

    @Override
    public void setFacing(final ForgeDirection facing) {
        this.facing = facing;

        NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
    }
}
