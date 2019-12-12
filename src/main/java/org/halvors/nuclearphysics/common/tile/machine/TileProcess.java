package org.halvors.nuclearphysics.common.tile.machine;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.halvors.nuclearphysics.common.block.states.BlockStateMachine.EnumMachine;
import org.halvors.nuclearphysics.common.capabilities.fluid.LiquidTank;
import org.halvors.nuclearphysics.common.tile.TileInventoryMachine;
import org.halvors.nuclearphysics.common.utility.FluidUtility;
import org.halvors.nuclearphysics.common.utility.InventoryUtility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/*
 * General class for all machines that do traditional ingredient processing.
 */
public abstract class TileProcess extends TileInventoryMachine implements IFluidHandler {
    private static final String NBT_TANK_INPUT = "tankInput";
    private static final String NBT_TANK_OUTPUT = "tankInOutput";

    protected LiquidTank tankInput;
    protected LiquidTank tankOutput;

    public float rotation = 0;

    protected int inputSlot;
    protected int outputSlot;

    protected int tankInputFillSlot;
    protected int tankInputDrainSlot;
    protected int tankOutputFillSlot;
    protected int tankOutputDrainSlot;

    public TileProcess(final EnumMachine type) {
        super(type);
    }

    @Override
    public void read(final CompoundNBT compound) {
        super.read(compound);

        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(tankInput, null, compound.getCompound(NBT_TANK_INPUT));
        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(tankOutput, null, compound.getCompound(NBT_TANK_OUTPUT));
    }

    @Override
    @Nonnull
    public CompoundNBT write(final CompoundNBT compound) {
        super.write(compound);

        tag.setTag(NBT_TANK_INPUT, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tankInput, null));
        tag.setTag(NBT_TANK_OUTPUT, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tankOutput, null));

        return compound;
    }

    @Override
    public boolean hasCapability(@Nonnull final Capability<?> capability, @Nullable final EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull final Capability<T> capability, @Nullable final EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) this;
        }

        return super.getCapability(capability, facing);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void update() {
        super.update();

        if (!world.isRemote) {
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
    public void handlePacketData(final ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (world.isRemote) {
            tankInput.handlePacketData(dataStream);
            tankOutput.handlePacketData(dataStream);
        }
    }

    @Override
    public List<Object> getPacketData(final List<Object> objects) {
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
    public int fill(final FluidStack resource, final boolean doFill) {
        return tankInput.fill(resource, doFill);
    }

    @Nullable
    @Override
    public FluidStack drain(final FluidStack resource, final boolean doDrain) {
        return tankOutput.drain(resource, doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(final int maxDrain, final boolean doDrain) {
        return tankOutput.drain(maxDrain, doDrain);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
     * Takes an fluid container item and try to fill the tank, dropping the remains in the output slot.
     */
    private void fillOrDrainTank(final int containerInput, final int containerOutput, final IFluidHandler tank) {
        final ItemStack itemStackInput = inventory.getStackInSlot(containerInput);

        if (!itemStackInput.isEmpty()) {
            final ItemStack itemStackOutput = inventory.getStackInSlot(containerOutput);
            final boolean isFilled = FluidUtility.isFilledContainer(itemStackInput);
            final FluidActionResult fluidActionResult;

            if (isFilled) {
                fluidActionResult = FluidUtil.tryEmptyContainer(itemStackInput, tank, Integer.MAX_VALUE, null, false);
            } else {
                fluidActionResult = FluidUtil.tryFillContainer(itemStackInput, tank, Integer.MAX_VALUE, null, false);
            }

            if (fluidActionResult.isSuccess()) {
                ItemStack itemStackResult = fluidActionResult.getResult();

                if (itemStackOutput.isEmpty() || (FluidUtility.isEmptyContainer(itemStackOutput) || FluidUtility.isFilledContainerEqual(itemStackResult, itemStackOutput)) && itemStackResult.isItemEqual(itemStackOutput) && itemStackOutput.isStackable() && itemStackOutput.getCount() < itemStackOutput.getMaxStackSize()) {
                    if (isFilled) {
                        FluidUtil.tryEmptyContainer(itemStackInput, tank, Integer.MAX_VALUE, null, true);
                    } else {
                        FluidUtil.tryFillContainer(itemStackInput, tank, Integer.MAX_VALUE, null, true);
                    }

                    InventoryUtility.decrStackSize(inventory, containerInput);
                    inventory.insertItem(containerOutput, itemStackResult, false);
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

    public FluidTank getInputTank() {
        return tankInput;
    }

    public FluidTank getOutputTank() {
        return tankOutput;
    }
}
