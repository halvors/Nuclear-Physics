package org.halvors.nuclearphysics.common.tile.machine;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.nuclearphysics.common.tile.ITileNetwork;
import org.halvors.nuclearphysics.common.tile.TileConsumer;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.List;

public abstract class TileMachine extends TileConsumer implements ITileNetwork {
    protected EnumMachine type;
    
    public int operatingTicks = 0; // Synced
    public int ticksRequired = 0;

    public TileMachine() {

    }

    public TileMachine(EnumMachine type) {
        this.type = type;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        operatingTicks = tag.getInteger("operatingTicks");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setInteger("operatingTicks", operatingTicks);

        return tag;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (world.isRemote) {
            operatingTicks = dataStream.readInt();
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        super.getPacketData(objects);

        objects.add(operatingTicks);

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getName() {
        return LanguageUtility.transelate(getBlockType().getUnlocalizedName() + "." + type.ordinal() + ".name");
    }

    public EnumMachine getType() {
        return type;
    }
}
