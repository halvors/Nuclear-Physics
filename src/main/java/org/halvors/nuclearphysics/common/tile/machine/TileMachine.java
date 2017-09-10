package org.halvors.nuclearphysics.common.tile.machine;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.tile.ITileNetwork;
import org.halvors.nuclearphysics.common.tile.ITileRedstoneControl;
import org.halvors.nuclearphysics.common.tile.TileConsumer;
import org.halvors.nuclearphysics.common.utility.EnergyUtility;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.type.RedstoneControl;

import java.util.List;

public class TileMachine extends TileConsumer implements ITickable, ITileNetwork, ITileRedstoneControl {
    protected EnumMachine type;

    public int energyUsed = 0;
    public int operatingTicks = 0; // Synced
    public int ticksRequired = 0;

    protected RedstoneControl redstoneControl = RedstoneControl.DISABLED;
    protected boolean redstone = false;
    protected boolean redstoneLastTick = false;

    public TileMachine() {

    }

    public TileMachine(EnumMachine type) {
        this.type = type;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        operatingTicks = tag.getInteger("operatingTicks");
        redstone = tag.getBoolean("redstone");
        redstoneControl = RedstoneControl.values()[tag.getInteger("redstoneControl")];
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setInteger("operatingTicks", operatingTicks);
        tag.setBoolean("redstone", redstone);
        tag.setInteger("redstoneControl", redstoneControl.ordinal());

        return tag;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void update() {
        redstoneLastTick = redstone;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (world.isRemote) {
            energyUsed = dataStream.readInt();
            operatingTicks = dataStream.readInt();
            redstone = dataStream.readBoolean();
            redstoneControl = RedstoneControl.values()[dataStream.readInt()];
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        super.getPacketData(objects);

        objects.add(energyUsed);
        objects.add(operatingTicks);
        objects.add(redstone);
        objects.add(redstoneControl.ordinal());

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public RedstoneControl getRedstoneControl() {
        return redstoneControl;
    }

    @Override
    public void setRedstoneControl(RedstoneControl redstoneControl) {
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
        if (!world.isRemote) {
            boolean power = world.isBlockIndirectlyGettingPowered(pos) > 0;

            if (redstone != power) {
                redstone = power;

                NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getName() {
        return LanguageUtility.transelate(getBlockType().getUnlocalizedName() + "." + type.ordinal() + ".name");
    }

    public EnumMachine getType() {
        return type;
    }

    protected boolean canFunction() {
        return EnergyUtility.canFunction(this);
    }
}
