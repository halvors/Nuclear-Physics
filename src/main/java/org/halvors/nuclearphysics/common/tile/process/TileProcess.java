package org.halvors.nuclearphysics.common.tile.process;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.nuclearphysics.common.capabilities.fluid.LiquidTank;
import org.halvors.nuclearphysics.common.tile.TileInventoryMachine;
import org.halvors.nuclearphysics.common.utility.FluidUtility;

import java.util.List;

/*
 * General class for all machines that do traditional recipe processing.
 */
public abstract class TileProcess extends TileInventoryMachine implements IFluidHandler {
    protected LiquidTank tankInput;
    protected LiquidTank tankOutput;

    public float rotation = 0;

    protected int inputSlot;
    protected int outputSlot;

    protected int tankInputFillSlot;
    protected int tankInputDrainSlot;
    protected int tankOutputFillSlot;
    protected int tankOutputDrainSlot;

    public TileProcess(EnumMachine type, int maxSlots) {
        super(type, maxSlots);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        tankInput.readFromNBT(tag.getCompoundTag("tankInput"));
        tankOutput.readFromNBT(tag.getCompoundTag("tankOutput"));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setTag("tankInput", tankInput.writeToNBT(new NBTTagCompound()));
        tag.setTag("tankOutput", tankOutput.writeToNBT(new NBTTagCompound()));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (!worldObj.isRemote) {
            if (getInputTank() != null) {
                fillOrDrainTank(tankInputFillSlot, tankInputDrainSlot, getInputTank());
            }

            if (getOutputTank() != null) {
                fillOrDrainTank(tankOutputFillSlot, tankOutputDrainSlot, getOutputTank());
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (worldObj.isRemote) {
            tankInput.handlePacketData(dataStream);
            tankOutput.handlePacketData(dataStream);
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        super.getPacketData(objects);

        tankInput.getPacketData(objects);
        tankOutput.getPacketData(objects);

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource != null && canFill(from, resource.getFluid())) {
            return tankInput.fill(resource, doFill);
        }

        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return tankOutput.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return tankOutput.getFluid() != null && fluid.getID() == tankOutput.getFluid().getFluidID();
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { tankInput.getInfo(), tankOutput.getInfo() };
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
     * Takes an fluid container item and try to fill the tank, dropping the remains in the output slot.
     */
    public void fillOrDrainTank(int containerInput, int containerOutput, FluidTank tank) {
        ItemStack itemStackInput = getStackInSlot(containerInput);
        ItemStack itemStackOutput = getStackInSlot(containerOutput);

        if (itemStackInput != null) {
            if (FluidUtility.isFilledContainer(itemStackInput)) {
                FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStackInput);
                ItemStack result = itemStackInput.getItem().getContainerItem(itemStackInput);

                if (result != null && tank.fill(fluidStack, false) >= fluidStack.amount && (itemStackOutput == null || result.isItemEqual(itemStackOutput))) {
                    tank.fill(fluidStack, true);
                    decrStackSize(containerInput, 1);
                    incrStackSize(containerOutput, result);
                }
            } else if (FluidUtility.isEmptyContainer(itemStackInput)) {
                FluidStack avaliable = tank.getFluid();

                if (avaliable != null) {
                    ItemStack result = FluidContainerRegistry.fillFluidContainer(avaliable, itemStackInput);
                    FluidStack filled = FluidContainerRegistry.getFluidForFilledItem(result);

                    if (result != null && filled != null && (itemStackOutput == null || result.isItemEqual(itemStackOutput))) {
                        decrStackSize(containerInput, 1);
                        incrStackSize(containerOutput, result);
                        tank.drain(filled.amount, true);
                    }
                }
            }
        }
    }

    public FluidTank getInputTank() {
        return tankInput;
    }

    public FluidTank getOutputTank() {
        return tankOutput;
    }
}
