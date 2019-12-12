package org.halvors.nuclearphysics.common.tile;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.nbt.CompoundNBT;
import org.halvors.nuclearphysics.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.states.BlockStateMachine.EnumMachine;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.type.EnumRedstoneControl;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.RedstoneUtility;

import java.util.List;

public class TileMachine extends TileConsumer implements ITickable, ITileRedstoneControl {
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
    public void read(CompoundNBT compound) {
        super.read(compound);

        operatingTicks = compound.getInt(NBT_OPERATING_TICKS);
        redstone = compound.getBoolean(NBT_REDSTONE);
        redstoneControl = EnumRedstoneControl.values()[compound.getInt(NBT_REDSTONE_CONTROL)];
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt(NBT_OPERATING_TICKS, operatingTicks);
        compound.putBoolean(NBT_REDSTONE, redstone);
        compound.putInt(NBT_REDSTONE_CONTROL, redstoneControl.ordinal());

        return super.write(compound);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void update() {
        if (!world.isRemote) {
            redstoneLastTick = redstone;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(final ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (world.isRemote) {
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
        if (!world.isRemote) {
            boolean power = world.getRedstonePowerFromNeighbors(pos) > 0;

            if (redstone != power) {
                redstone = power;

                NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public EnumMachine getMachineType() {
        return type;
    }

    public int getEnergyUsed() {
        return energyUsed;
    }

    public int getOperatingTicks() {
        return operatingTicks;
    }

    public String getName() {
        return LanguageUtility.transelate(getBlockType().getTranslationKey() + "." + type.ordinal() + ".name");
    }

    protected boolean canFunction() {
        return RedstoneUtility.canFunction(this);
    }

    protected void reset() {
        operatingTicks = 0;
        energyUsed = 0;
    }
}
