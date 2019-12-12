package org.halvors.nuclearphysics.common.capabilities.fluid;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.List;

public class LiquidTank extends FluidTank {
    public LiquidTank(final int capacity) {
        super(capacity);
    }

    public void handlePacketData(final ByteBuf dataStream) {
        if (dataStream.readBoolean()) {
            setFluid(FluidStack.loadFluidStackFromNBT(ByteBufUtils.readTag(dataStream)));
        }
    }

    public List<Object> getPacketData(final List<Object> objects) {
        if (fluid != null) {
            objects.add(true);

            final CompoundNBT compoundInputTank = new CompoundNBT();
            fluid.writeToNBT(compoundInputTank);
            objects.add(compoundInputTank);
        } else {
            objects.add(false);
        }

        return objects;
    }
}