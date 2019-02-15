package org.halvors.nuclearphysics.common.tile.machine;

import io.netty.buffer.ByteBuf;
import net.minecraft.init.Items;
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
            if (getInputTank() != null) {
                fillTank(tankInputFillSlot, tankInputDrainSlot, getInputTank());
            }

            if (getOutputTank() != null) {
                drainTank(tankOutputFillSlot, tankOutputDrainSlot, getOutputTank());
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
    public void fillTank(final int containerInput, final int containerOutput, final FluidTank tank) {
        final ItemStack itemStackInput = getStackInSlot(containerInput);
        final ItemStack itemStackOutput = getStackInSlot(containerOutput);
        IFluidContainerItem itemTank;
        

        if (itemStackInput != null) {
            if (FluidContainerRegistry.isFilledContainer(itemStackInput)) {			// simple mode container

                final FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(itemStackInput);	// fluid & amount
                // get an empty cell/bucket/bottle
                final ItemStack result = FluidContainerRegistry.drainFluidContainer(itemStackInput);
                // bottle must have volume of 250 mb
                if((result != null) && (result.getItem().equals(Items.glass_bottle))) fluidStack.amount = 250;

                if (result != null && tank.fill(fluidStack, false) >= fluidStack.amount && (canMergeStack(result, itemStackOutput))) {
                    tank.fill(fluidStack, true);
                    decrStackSize(containerInput, 1);
                    incrStackSize(containerOutput, result);
                }
            } else if(itemStackInput.getItem() instanceof IFluidContainerItem) {	// advanced mode container
            	itemTank = (IFluidContainerItem)itemStackInput.getItem();
            	
            	if(itemStackInput.getMaxStackSize() == 1) {							// unstackable tank, like Mekanism tank, may have a big volume
            		int rez = tank.fill(itemTank.drain(itemStackInput, itemTank.getCapacity(itemStackInput), false), false);
            		if(rez > 0){
            			rez = tank.fill(itemTank.drain(itemStackInput, rez, true), true);
            			System.out.println("*** Tank filled for " + rez + " mB");
            		}
            		if(itemTank.getFluid(itemStackInput) == null && (itemStackOutput == null)) {
            			incrStackSize(containerOutput, decrStackSize(containerInput, 1));
            		}
            	}
            	else {																// stackable tank, we can't drain part of them
            		ItemStack oneCell = itemStackInput.copy();
            		oneCell.stackSize=1;
                	itemTank = (IFluidContainerItem)oneCell.getItem();			// give one of them
                	FluidStack portion = itemTank.drain(oneCell, itemTank.getCapacity(oneCell), true);

                	if(tank.fill(portion, false) == portion.amount && canMergeStack(oneCell, itemStackOutput)) {
            			int rez = tank.fill(portion, true);
            			System.out.println("*** Tank filled for " + rez + " mB");
            			decrStackSize(containerInput, 1);
            			incrStackSize(containerOutput, oneCell);					// oneCell is empty now!
            		}
            	}
            } else System.out.println("*** fillOrDrainTank(): no any container detected!");
        }
    }

    /**
     * Ouptut tank can drain only
     * @param containerInput - slot index for empty items
     * @param containerOutput - slot index for filled items
     * @param tank - internal tank to drain
     */
    public void drainTank(final int containerInput, final int containerOutput, final FluidTank tank) {
        final ItemStack itemStackInput = getStackInSlot(containerInput);
        final ItemStack itemStackOutput = getStackInSlot(containerOutput);

        if (itemStackInput != null) {
            if (FluidContainerRegistry.isEmptyContainer(itemStackInput)){   // simple container
            	/**
            	 * Currently, any container of this type it isn't defined for deuteruim/tritium
            	 */
            	if(tank.getFluidAmount() >= FluidContainerRegistry.getContainerCapacity(itemStackInput)){
            		ItemStack filled = FluidContainerRegistry.fillFluidContainer(tank.getFluid(), itemStackInput);
            		if(filled != null && canMergeStack(filled, itemStackOutput)) {
            			tank.drain(FluidContainerRegistry.getContainerCapacity(itemStackInput), true);
            			decrStackSize(containerInput, 1);
            			incrStackSize(containerOutput, filled);
            		}
            	}
            } else if(itemStackInput.getItem() instanceof IFluidContainerItem) {	// advanced container
            	IFluidContainerItem itemTank = (IFluidContainerItem)itemStackInput.getItem();
            	if(itemStackInput.getMaxStackSize() == 1) {							// unstackable, like Mekanism tank 
            		if(itemTank.fill(itemStackInput, tank.drain(tank.getCapacity(), false), false) > 0) {
            			itemTank.fill(itemStackInput, tank.drain(tank.getCapacity(), true), true);
            		}
            		// it's time to check item for filled state
        			FluidStack fs = itemTank.getFluid(itemStackInput);
        			if(itemTank.getCapacity(itemStackInput) == fs.amount) {			// tank is full
        				if(itemStackOutput == null) {								// lower slot may be clear
        					incrStackSize(containerOutput, decrStackSize(containerInput, 1));
        				}
        			}
            	} else {															// stackable, like our cells
            		// тут все еще не плюсуются ячейки!
            		ItemStack oneCell = itemStackInput.copy();
            		oneCell.stackSize = 1;
            		FluidStack test = tank.drain(tank.getCapacity(), false);
            		itemTank.fill(oneCell, test, true);								// we need filled sample for comparission 

            		if((test != null) && (test.amount >= itemTank.getCapacity(oneCell))) {
            			if(canMergeStack(oneCell, itemStackOutput)) {
            				tank.drain(itemTank.getCapacity(oneCell), true);
            				decrStackSize(containerInput, 1);
System.out.println("*** increment " + itemStackOutput + " for " + oneCell);
            				incrStackSize(containerOutput, oneCell);
            			}
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
    
    /**
     * @halvors, you can move this to utilities class!
     * @param source
     * @param destination
     * @return
     */
    public static boolean canMergeStack(ItemStack source, ItemStack destination) {
    	if(destination == null || source == null) return true;
    	if(source.isItemEqual(destination) && ItemStack.areItemStackTagsEqual(source, destination))
    		if(source.stackSize + destination.stackSize <= destination.getMaxStackSize()) return true;
    	return false;
    }
}
