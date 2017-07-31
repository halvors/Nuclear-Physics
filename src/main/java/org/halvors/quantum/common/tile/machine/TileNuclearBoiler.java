package org.halvors.quantum.common.tile.machine;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.QuantumFluids;
import org.halvors.quantum.common.QuantumItems;
import org.halvors.quantum.common.fluid.tank.FluidTankQuantum;
import org.halvors.quantum.common.network.packet.PacketTileEntity;
import org.halvors.quantum.common.utility.InventoryUtility;
import org.halvors.quantum.common.utility.OreDictionaryHelper;

public class TileNuclearBoiler extends TileProcess {
    public static final int tickTime = 20 * 15;
    public static final int energy = 21000;

    public float rotation = 0;

    public TileNuclearBoiler() {
        energyStorage = new EnergyStorage(energy * 2);
        inventory = new ItemStackHandler(5) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                markDirty();
            }

            private boolean isItemValidForSlot(int slot, ItemStack itemStack) {
                switch (slot) {
                    case 0: // Battery input slot.
                        return itemStack.hasCapability(CapabilityEnergy.ENERGY, null);

                    case 1: // Item input slot.
                        return OreDictionaryHelper.isUraniumOre(itemStack) || OreDictionaryHelper.isYellowCake(itemStack);

                    case 2: // Input tank fill slot.
                    case 3: // Input tank drain slot.
                        return OreDictionaryHelper.isEmptyCell(itemStack) || OreDictionaryHelper.isWaterCell(itemStack);

                    case 4: // Output tank drain slot.
                        return OreDictionaryHelper.isEmptyCell(itemStack); // TODO: Add uranium hexaflouride container here.
                }

                return false;
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if (!isItemValidForSlot(slot, stack)) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };

        tankInput = new FluidTankQuantum(QuantumFluids.fluidStackWater.copy(),Fluid.BUCKET_VOLUME * 5) {
            @Override
            public int fill(FluidStack resource, boolean doFill) {
                if (resource.isFluidEqual(QuantumFluids.fluidStackWater)) {
                    return super.fill(resource, doFill);
                }

                return 0;
            }

            @Override
            public boolean canDrain() {
                return false;
            }
        };

        tankOutput = new FluidTankQuantum(QuantumFluids.fluidStackUraniumHexaflouride.copy(), Fluid.BUCKET_VOLUME * 5) {
            @Override
            public boolean canFill() {
                return false;
            }

            @Override
            public FluidStack drain(FluidStack resource, boolean doDrain) {
                if (resource.isFluidEqual(QuantumFluids.fluidStackUraniumHexaflouride)) {
                    return drain(resource.amount, doDrain);
                }

                return null;
            }
        };

        inputSlot = 1;

        tankInputFillSlot = 2;
        tankInputDrainSlot = 3;
        tankOutputDrainSlot = 4;
    }

    @Override
    public void update() {
        super.update();

        if (timer > 0) {
            rotation += 0.1;
        } else {
            rotation = 0;
        }

        if (!world.isRemote) {
            // Put water as liquid
            /*
            if (getStackInSlot(1) != null) {
                if (FluidContainerRegistry.isFilledContainer(getStackInSlot(1))) {
                    FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(getStackInSlot(1));

                    if (liquid.isFluidEqual(Quantum.fluidStackWater)) {
                        if (fill(ForgeDirection.UNKNOWN, liquid, false) > 0) {
                            ItemStack resultingContainer = getStackInSlot(1).getItem().getContainerItem(getStackInSlot(1));

                            if (resultingContainer == null && getStackInSlot(1).stackSize > 1) {
                                getStackInSlot(1).stackSize--;
                            } else {
                                setInventorySlotContents(1, resultingContainer);
                            }

                            tankInput.fillInternal(liquid, true);
                        }
                    }
                }
            }
            */

            if (canProcess()) {
                // TODO: Implement this.
                //discharge(getStackInSlot(0));

                if (energyStorage.extractEnergy(energy, true) >= energy) {
                    if (timer == 0) {
                        timer = tickTime;
                    }

                    if (timer > 0) {
                        timer--;

                        if (timer < 1) {
                            doProcess();
                            timer = 0;
                        }
                    } else {
                        timer = 0;
                    }

                    energyStorage.extractEnergy(energy, false);
                } else {
                    timer = 0;
                }
            } else {
                timer = 0;
            }

            if (world.getWorldTime() % 10 == 0) {
                if (!world.isRemote) {
                    Quantum.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return side == EnumFacing.DOWN ? new int[] { 2 } : new int[] { 1, 3 };
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, EnumFacing side) {
        return slot == 2;
    }
    */

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Check all conditions and see if we can start smelting
    public boolean canProcess() {
        FluidStack inputFluidStack = tankInput.getFluid();

        if (inputFluidStack != null && inputFluidStack.amount >= Fluid.BUCKET_VOLUME) {
            ItemStack itemStack = inventory.getStackInSlot(inputSlot);

            if (itemStack != null) {
                if (OreDictionaryHelper.isUraniumOre(itemStack) || OreDictionaryHelper.isYellowCake(itemStack)) {
                    FluidStack outputFluidStack = tankOutput.getFluid();

                    if (outputFluidStack != null && outputFluidStack.amount < tankOutput.getCapacity()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack.
    public void doProcess() {
        if (canProcess()) {
            tankInput.drainInternal(Fluid.BUCKET_VOLUME, true);
            FluidStack liquid = QuantumFluids.fluidStackUraniumHexaflouride.copy();
            liquid.amount = ConfigurationManager.General.uraniumHexaflourideRatio * 2;
            tankOutput.fillInternal(liquid, true);
            InventoryUtility.decrStackSize(inventory, inputSlot);
        }
    }
}
