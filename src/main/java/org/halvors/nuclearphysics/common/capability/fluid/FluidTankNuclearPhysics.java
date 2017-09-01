package org.halvors.nuclearphysics.common.capability.fluid;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.halvors.nuclearphysics.common.network.PacketHandler;

import javax.annotation.Nullable;
import java.util.List;

public class FluidTankNuclearPhysics extends FluidTank {
    public FluidTankNuclearPhysics(int capacity) {
        super(capacity);
    }

    public FluidTankNuclearPhysics(@Nullable FluidStack fluidStack, int capacity) {
        super(fluidStack, capacity);
    }

    public FluidTankNuclearPhysics(Fluid fluid, int amount, int capacity) {
        super(fluid, amount, capacity);
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
