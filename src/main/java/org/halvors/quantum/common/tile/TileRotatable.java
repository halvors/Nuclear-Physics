package org.halvors.quantum.common.tile;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.network.packet.PacketTileEntity;

import java.util.List;

public class TileRotatable extends TileElectricInventory implements ITileNetwork, ITileRotatable {
    /** The direction this block is facing. */
    public EnumFacing facing = EnumFacing.NORTH;

    @SideOnly(Side.CLIENT)
    public EnumFacing clientFacing = EnumFacing.NORTH;

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

            if (clientFacing != facing) {
                clientFacing = facing;

                //world.notifyNeighborsOfStateChange(getPos(), world.getBlockState(getPos()).getBlock());
            }
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
        if (!(facing == clientFacing || world.isRemote)) {
            //Quantum.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            //markDirty();

            //clientFacing = facing;
        }

        this.facing = facing;

        Quantum.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
    }
}
