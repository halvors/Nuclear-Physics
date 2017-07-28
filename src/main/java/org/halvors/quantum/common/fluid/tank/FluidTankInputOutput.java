package org.halvors.quantum.common.fluid.tank;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import org.halvors.quantum.common.network.PacketHandler;

import javax.annotation.Nullable;
import java.util.List;

public class FluidTankInputOutput implements IFluidHandler {
    protected FluidTank inputTank; // Synced
    protected FluidTank outputTank; // Synced
    private IFluidTankProperties[] tankProperties;

    public FluidTankInputOutput(FluidTank inputTank, FluidTank outputTank) {
        this.inputTank = inputTank;
        this.outputTank = outputTank;
    }

    public void readFromNBT(NBTTagCompound tag) {
        NBTTagCompound inputTankTag = tag.getCompoundTag("inputTank");
        inputTank.setFluid(FluidStack.loadFluidStackFromNBT(inputTankTag));

        NBTTagCompound outputTankTag = tag.getCompoundTag("outputTank");
        outputTank.setFluid(FluidStack.loadFluidStackFromNBT(outputTankTag));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if (inputTank.getFluid() != null) {
            NBTTagCompound inputTankCompound = new NBTTagCompound();
            inputTank.getFluid().writeToNBT(inputTankCompound);
            tag.setTag("inputTank", inputTankCompound);
        }

        if (outputTank.getFluid() != null) {
            NBTTagCompound outputTankTag = new NBTTagCompound();
            outputTank.getFluid().writeToNBT(outputTankTag);
            tag.setTag("outputTank", outputTankTag);
        }

        return tag;
    }

    public void handlePacketData(ByteBuf dataStream) {
        if (dataStream.readBoolean()) {
            inputTank.setFluid(FluidStack.loadFluidStackFromNBT(PacketHandler.readNBT(dataStream)));
        }

        if (dataStream.readBoolean()) {
            outputTank.setFluid(FluidStack.loadFluidStackFromNBT(PacketHandler.readNBT(dataStream)));
        }
    }

    public List<Object> getPacketData(List<Object> objects) {
        if (inputTank.getFluid() != null) {
            objects.add(true);

            NBTTagCompound compoundInputTank = new NBTTagCompound();
            inputTank.getFluid().writeToNBT(compoundInputTank);
            objects.add(compoundInputTank);
        } else {
            objects.add(false);
        }

        if (outputTank.getFluid() != null) {
            objects.add(true);

            NBTTagCompound compoundOutputTank = new NBTTagCompound();
            outputTank.getFluid().writeToNBT(compoundOutputTank);
            objects.add(compoundOutputTank);
        } else {
            objects.add(false);
        }

        return objects;
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        if (tankProperties == null) {
            tankProperties = new IFluidTankProperties[] { new FluidTankPropertiesWrapper(inputTank), new FluidTankPropertiesWrapper(outputTank) };
        }

        return tankProperties;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return inputTank.fill(resource, doFill);
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return drain(resource.amount, doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return outputTank.drain(maxDrain, doDrain);
    }

    public FluidTank getInputTank() {
        return inputTank;
    }

    public FluidTank getOutputTank() {
        return outputTank;
    }
}
