package org.halvors.nuclearphysics.common.tile.machine;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.nuclearphysics.common.capabilities.fluid.LiquidTank;
import org.halvors.nuclearphysics.common.tile.TileInventoryMachine;
import org.halvors.nuclearphysics.common.utility.FluidUtility;

import java.util.List;

/*
 * General class for all machines that do traditional recipe processing.
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

    public TileProcess(final EnumMachine type, final int maxSlots) {
        super(type, maxSlots);
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);

        tankInput.readFromNBT(tag.getCompoundTag(NBT_TANK_INPUT));
        tankOutput.readFromNBT(tag.getCompoundTag(NBT_TANK_OUTPUT));
    }

    @Override
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setTag(NBT_TANK_INPUT, tankInput.writeToNBT(new NBTTagCompound()));
        tag.setTag(NBT_TANK_OUTPUT, tankOutput.writeToNBT(new NBTTagCompound()));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (!worldObj.isRemote) {
            if (getInputTank() != null) {	// if tank presents
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

        if (worldObj.isRemote) {
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
    public int fill(final ForgeDirection from, final FluidStack resource, final boolean doFill) {
        if (resource != null && canFill(from, resource.getFluid())) {
            return tankInput.fill(resource, doFill);
        }

        return 0;
    }

    @Override
    public FluidStack drain(final ForgeDirection from, final FluidStack resource, final boolean doDrain) {
        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(final ForgeDirection from, final int maxDrain, final boolean doDrain) {
        return tankOutput.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canDrain(final ForgeDirection from, final Fluid fluid) {
        return tankOutput.getFluid() != null && fluid.getID() == tankOutput.getFluid().getFluidID();
    }

    @Override
    public FluidTankInfo[] getTankInfo(final ForgeDirection from) {
        return new FluidTankInfo[] { tankInput.getInfo(), tankOutput.getInfo() };
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
     * Takes an fluid container item and try to fill the tank, dropping the remains in the output slot.
     */
    public void fillOrDrainTank(final int containerInput, final int containerOutput, final FluidTank tank) {
        final ItemStack itemStackInput = getStackInSlot(containerInput);
        final ItemStack itemStackOutput = getStackInSlot(containerOutput);
        final IFluidContainerItem fluidContainerItem;

        if (itemStackInput != null) {
            if (FluidUtility.isFilledContainer(itemStackInput)) {
                FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStackInput);

                //*** Mekanism tanks begin 
                if(fluidStack == null) {
                	fluidContainerItem = (IFluidContainerItem)itemStackInput.getItem();
                	fluidStack = fluidContainerItem.getFluid(itemStackInput);
                	ItemStack fluidCont = itemStackInput.getItem().getContainerItem(itemStackInput);
                	if(fluidCont == null) {
                		// 
                		int amount = Math.min(fluidStack.amount, tank.fill(fluidStack, false));
                		tank.fill(fluidContainerItem.drain(itemStackInput, amount, true), true);
                		FluidStack fs_tmp = fluidContainerItem.getFluid(itemStackInput);
                		if(fs_tmp == null) {
                			ItemStack empty = itemStackInput.copy();
                			decrStackSize(containerInput, 1);
                			incrStackSize(containerOutput, empty); // move empty tank to lower slot
                		}
                	}
                }
                //*** Mekanism tanks end
                else {
                	final ItemStack result = itemStackInput.getItem().getContainerItem(itemStackInput);

                	if (result != null && tank.fill(fluidStack, false) >= fluidStack.amount && (itemStackOutput == null || result.isItemEqual(itemStackOutput))) {
                		tank.fill(fluidStack, true);
                		decrStackSize(containerInput, 1);
                		incrStackSize(containerOutput, result);
                	}
                }
            } else if (FluidUtility.isEmptyContainer(itemStackInput)) {
                final FluidStack avaliable = tank.getFluid();

                if (avaliable != null) {
                    final ItemStack result = FluidContainerRegistry.fillFluidContainer(avaliable, itemStackInput);
                    final FluidStack filled = FluidContainerRegistry.getFluidForFilledItem(result);

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
