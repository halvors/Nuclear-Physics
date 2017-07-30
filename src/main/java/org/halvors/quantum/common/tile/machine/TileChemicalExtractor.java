package org.halvors.quantum.common.tile.machine;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.QuantumFluids;
import org.halvors.quantum.common.fluid.tank.FluidTankQuantum;
import org.halvors.quantum.common.network.packet.PacketTileEntity;
import org.halvors.quantum.common.utility.OreDictionaryUtility;

public class TileChemicalExtractor extends TileProcess {
    public static final int tickTime = 20 * 14;
    private static final int extractSpeed = 100;
    public static final int energy = 20000;

    public float rotation = 0;

    private IItemHandler top = new RangedWrapper(inventory, 2, 3);
    private IItemHandler sides = new RangedWrapper(inventory, 0, 2);

    public TileChemicalExtractor() {
        energyStorage = new EnergyStorage(energy * 2);
        inventory = new ItemStackHandler(7) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                markDirty();
            }

            private boolean isItemValidForSlot(int slot, ItemStack itemStack) {
                switch (slot) {
                    case 0: // Water input for machine.
                        // TODO: Fix this.
                        //return CompatibilityModule.isHandler(itemStack.getItem());
                        return true;

                    case 1:
                        return OreDictionaryUtility.isWaterCell(itemStack);

                    case 2: // Empty cell to be filled with deuterium or tritium.
                        return OreDictionaryUtility.isDeuteriumCell(itemStack) || OreDictionaryUtility.isTritiumCell(itemStack);

                    case 3: // Uranium to be extracted into yellowcake.
                        return OreDictionaryUtility.isEmptyCell(itemStack) || OreDictionaryUtility.isUraniumOre(itemStack) || OreDictionaryUtility.isDeuteriumCell(itemStack);
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

        tankInput = new FluidTankQuantum(Fluid.BUCKET_VOLUME * 10);
        tankOutput = new FluidTankQuantum(Fluid.BUCKET_VOLUME * 10);

        inputSlot = 1;
        outputSlot = 2;

        tankInputFillSlot = 3;
        tankInputDrainSlot = 4;
        tankOutputFillSlot = 5;
        tankOutputDrainSlot = 6;
    }

    @Override
    public void update() {
        //super.update();

        if (timer > 0) {
            rotation += 0.2;
        }

        if (!world.isRemote) {
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
                            if (!refineUranium()) {
                                if (!extractTritium()) {
                                    extractDeuterium();
                                }
                            }

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
        return new int[] { 1, 2, 3 };
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, EnumFacing side) {
        return slot == 2;
    }
    */

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean canProcess() {;
        FluidStack inputFluidStack = tankInput.getFluid();

        if (inputFluidStack != null) {
            if (inputFluidStack.amount >= Fluid.BUCKET_VOLUME && OreDictionaryUtility.isUraniumOre(inventory.getStackInSlot(1))) { // inputSlot
                //if (isItemValidForSlot(outputSlot, new ItemStack(QuantumItems.itemYellowCake))) {
                    return true;
                //}
            }

            if (tankInput.getFluidAmount() < tankInput.getCapacity()) {
                FluidStack outputFluidStack = tankInput.getFluid();

                if (inputFluidStack.isFluidEqual(QuantumFluids.fluidStackDeuterium) && inputFluidStack.amount >= ConfigurationManager.General.deutermiumPerTritium * extractSpeed) {
                    if (outputFluidStack == null || outputFluidStack.getFluid() == QuantumFluids.gasTritium) {
                        return true;
                    }
                }

                if (inputFluidStack.isFluidEqual(QuantumFluids.fluidStackWater) && inputFluidStack.amount >= ConfigurationManager.General.waterPerDeutermium * extractSpeed) {
                    if (outputFluidStack == null || outputFluidStack.getFluid() == QuantumFluids.gasDeuterium) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /*
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack.
     */
    public boolean refineUranium() {
        if (canProcess()) {
            if (OreDictionaryUtility.isUraniumOre(inventory.getStackInSlot(1))) { // inputSlot
                tankInput.drain(Fluid.BUCKET_VOLUME, true);
                //itemStorage.incrStackSize(outputSlot, new ItemStack(QuantumItems.itemYellowCake, 3));
                //itemStorage.decrStackSize(inputSlot, 1);

                return true;
            }
        }

        return false;
    }

    public boolean extractDeuterium() {
        if (canProcess()) {
            int waterUsage = ConfigurationManager.General.waterPerDeutermium;
            FluidStack drain = tankInput.drain(waterUsage * extractSpeed, false);

            if (drain != null && drain.amount >= 1 && drain.getFluid() == FluidRegistry.WATER) {
                if (tankOutput.fill(new FluidStack(QuantumFluids.gasDeuterium, extractSpeed), true) >= extractSpeed) {
                    tankInput.drain(waterUsage * extractSpeed, true);

                    return true;
                }
            }
        }

        return false;
    }

    public boolean extractTritium() {
        if (canProcess()) {
            int deutermiumUsage = ConfigurationManager.General.deutermiumPerTritium;
            FluidStack drain = tankInput.drain(deutermiumUsage * extractSpeed, false);

            if (drain != null && drain.amount >= 1 && drain.getFluid() == QuantumFluids.gasDeuterium) {
                if (tankOutput.fill(new FluidStack(QuantumFluids.gasTritium, extractSpeed), true) >= extractSpeed) {
                    tankInput.drain(deutermiumUsage * extractSpeed, true);

                    return true;
                }
            }
        }

        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nonnull EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == null) {
                return (T) inventory;
            }

            switch (facing) {
                case UP:
                    return (T) top;

                case DOWN:
                    return null;

                default:
                    return (T) sides;
            }

        }

        return super.getCapability(capability, facing);
    }
    */
}
