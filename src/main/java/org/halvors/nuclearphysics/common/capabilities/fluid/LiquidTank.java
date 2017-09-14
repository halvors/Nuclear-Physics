package org.halvors.nuclearphysics.common.capabilities.fluid;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.halvors.nuclearphysics.common.network.PacketHandler;

import java.util.List;

public class LiquidTank extends FluidTank {
    public LiquidTank(int capacity) {
        super(capacity);
    }

    public void handlePacketData(ByteBuf dataStream) {
        if (dataStream.readBoolean()) {
            setFluid(FluidStack.loadFluidStackFromNBT(PacketHandler.readNBT(dataStream)));
        }
    }

    public List<Object> getPacketData(List<Object> objects) {
        if (fluid != null) {
            objects.add(true);

            NBTTagCompound compoundInputTank = new NBTTagCompound();
            fluid.writeToNBT(compoundInputTank);
            objects.add(compoundInputTank);
        } else {
            objects.add(false);
        }

        return objects;
    }
}