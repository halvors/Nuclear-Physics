package org.halvors.quantum.common.tile.machine;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import org.halvors.quantum.common.fluid.tank.FluidTankQuantum;
import org.halvors.quantum.common.utility.InventoryUtility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/*
 * General class for all machines that do traditional recipe processing.
 */
public abstract class TileProcess extends TileMachine implements ITickable, IFluidHandler {
    protected FluidTankQuantum tankInput;
    protected FluidTankQuantum tankOutput;

    protected int inputSlot;
    protected int outputSlot;

    protected int tankInputFillSlot;
    protected int tankInputDrainSlot;
    protected int tankOutputFillSlot;
    protected int tankOutputDrainSlot;

    @Override
    public void update() {
        if (getInputTank() != null) {
            fillOrDrainTank(tankInputFillSlot, tankInputDrainSlot, getInputTank());
        }

        if (getOutputTank() != null) {
            fillOrDrainTank(tankOutputFillSlot, tankOutputDrainSlot, getOutputTank());
        }
    }

    /*
     * Takes an fluid container item and try to fill the tank, dropping the remains in the output slot.
     */
    public void fillOrDrainTank(int containerInput, int containerOutput, IFluidTank tank) {
        ItemStack inputStack = inventory.getStackInSlot(containerInput);
        ItemStack outputStack = inventory.getStackInSlot(containerOutput);

        if (inputStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
            IFluidHandler handler = inputStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);

            // TODO...
        }


        if (FluidContainerRegistry.isFilledContainer(inputStack)) {
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(inputStack);
            ItemStack result = inputStack.getItem().getContainerItem(inputStack);

            if (result != null && tank.fill(fluidStack, false) >= fluidStack.amount && (outputStack == null || result.isItemEqual(outputStack))) {
                tank.fill(fluidStack, true);

                InventoryUtility.decrStackSize(inventory, containerInput);
                inventory.insertItem(containerOutput, result, false);
            }
        } else if (FluidContainerRegistry.isEmptyContainer(inputStack)) {
            FluidStack avaliable = tank.getFluid();

            if (avaliable != null) {
                ItemStack result = FluidContainerRegistry.fillFluidContainer(avaliable, inputStack);
                FluidStack filled = FluidContainerRegistry.getFluidForFilledItem(result);

                if (result != null && filled != null && (outputStack == null || result.isItemEqual(outputStack))) {
                    InventoryUtility.decrStackSize(inventory, containerInput);
                    inventory.insertItem(containerOutput, result, false);
                    tank.drain(filled.amount, true);
                }
            }
        }
    }

    /*
     * Gets the current result of the input set up.
     */
    /*
    public RecipeResource[] getResults() {
        ItemStack inputStack = getStackInSlot(inputSlot);
        RecipeResource[] mixedResult = MachineRecipes.INSTANCE.getOutput(machineName, inputStack, getInputTank().getFluid());

        if (mixedResult.length > 0) {
            return mixedResult;
        }

        return MachineRecipes.INSTANCE.getOutput(machineName, inputStack);

        return null;
    }

    public boolean hasResult() {
        return getResults().length > 0;
    }
    */

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(tankInput, null, tag.getTag("tankInput"));
        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(tankOutput, null, tag.getTag("tankOutput"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);

        tag.setTag("tankInput", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tankInput, null));
        tag.setTag("tankOutput", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tankOutput, null));

        return tag;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (world.isRemote) {
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
    public IFluidTankProperties[] getTankProperties() {
        return new IFluidTankProperties[] { new FluidTankPropertiesWrapper(tankInput), new FluidTankPropertiesWrapper(tankOutput) };
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return tankInput.fill(resource, doFill);
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return tankOutput.drain(resource, doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return tankOutput.drain(maxDrain, doDrain);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public FluidTank getInputTank() {
        return tankInput;
    }

    public FluidTank getOutputTank() {
        return tankOutput;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) this;
        }

        return super.getCapability(capability, facing);
    }
}
