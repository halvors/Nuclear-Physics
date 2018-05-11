package org.halvors.nuclearphysics.common.tile;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.type.EnumRedstoneControl;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.RedstoneUtility;

import java.util.List;

public class TileMachine extends TileConsumer implements ITileRedstoneControl {
    private static final String NBT_OPERATING_TICKS = "operatingTicks";
    private static final String NBT_REDSTONE = "redstone";
    private static final String NBT_REDSTONE_CONTROL = "redstoneControl";

    protected EnumMachine type;

    protected int energyUsed = 0; // Synced
    protected int operatingTicks = 0; // Synced

    protected EnumRedstoneControl redstoneControl = EnumRedstoneControl.DISABLED;
    protected boolean redstone = false;
    protected boolean redstoneLastTick = false;

    public TileMachine() {

    }

    public TileMachine(final EnumMachine type) {
        this.type = type;
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);

        operatingTicks = tag.getInteger(NBT_OPERATING_TICKS);
        redstone = tag.getBoolean(NBT_REDSTONE);
        redstoneControl = EnumRedstoneControl.values()[tag.getInteger(NBT_REDSTONE_CONTROL)];
    }

    @Override
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setInteger(NBT_OPERATING_TICKS, operatingTicks);
        tag.setBoolean(NBT_REDSTONE, redstone);
        tag.setInteger(NBT_REDSTONE_CONTROL, redstoneControl.ordinal());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            redstoneLastTick = redstone;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(final ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (worldObj.isRemote) {
            energyUsed = dataStream.readInt();
            operatingTicks = dataStream.readInt();
            redstone = dataStream.readBoolean();
            redstoneControl = EnumRedstoneControl.values()[dataStream.readInt()];
        }
    }

    @Override
    public List<Object> getPacketData(final List<Object> objects) {
        super.getPacketData(objects);

        objects.add(energyUsed);
        objects.add(operatingTicks);
        objects.add(redstone);
        objects.add(redstoneControl.ordinal());

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public EnumRedstoneControl getRedstoneControl() {
        return redstoneControl;
    }

    @Override
    public void setRedstoneControl(final EnumRedstoneControl redstoneControl) {
        this.redstoneControl = redstoneControl;
    }

    @Override
    public boolean isPowered() {
        return redstone;
    }

    @Override
    public boolean wasPowered() {
        return redstoneLastTick;
    }

    @Override
    public boolean canPulse() {
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void updatePower() {
        if (!worldObj.isRemote) {
            boolean power = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);

            if (redstone != power) {
                redstone = power;

                NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public EnumMachine getType() {
        return type;
    }

    public int getEnergyUsed() {
        return energyUsed;
    }

    public int getOperatingTicks() {
        return operatingTicks;
    }

    public String getName() {
        return LanguageUtility.transelate(getBlockType().getUnlocalizedName() + "." + type.ordinal() + ".name");
    }

    protected boolean canFunction() {
        return RedstoneUtility.canFunction(this);
    }

    protected void reset() {
        operatingTicks = 0;
        energyUsed = 0;
    }
}
