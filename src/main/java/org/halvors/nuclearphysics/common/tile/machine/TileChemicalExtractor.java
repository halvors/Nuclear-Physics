package org.halvors.nuclearphysics.common.tile.machine;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;
import org.halvors.nuclearphysics.common.ConfigurationManager;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.capability.fluid.FluidTankNuclearPhysics;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.init.ModItems;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.utility.EnergyUtility;
import org.halvors.nuclearphysics.common.utility.InventoryUtility;
import org.halvors.nuclearphysics.common.utility.OreDictionaryHelper;

public class TileChemicalExtractor extends TileProcess {
    public static final int tickTime = 20 * 14;
    private static final int extractSpeed = 100;
    public static final int energy = 20000;

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
                    case 0: // Battery input slot.
                        return EnergyUtility.canBeDischarged(itemStack);

                    case 1: // Item input slot.
                        return OreDictionaryHelper.isUraniumOre(itemStack);

                    case 2: // Item output slot.
                        return OreDictionaryHelper.isYellowCake(itemStack);

                    case 3: // Input tank fill slot.
                    case 4: // Input tank drain slot.
                        return OreDictionaryHelper.isEmptyCell(itemStack) || OreDictionaryHelper.isWaterCell(itemStack) || OreDictionaryHelper.isDeuteriumCell(itemStack);

                    case 5: // Output tank fill slot.
                        return OreDictionaryHelper.isEmptyCell(itemStack);

                    case 6: // Output tank drain slot.
                        return OreDictionaryHelper.isEmptyCell(itemStack) || OreDictionaryHelper.isDeuteriumCell(itemStack);
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

        tankInput = new FluidTankNuclearPhysics(Fluid.BUCKET_VOLUME * 10) {
            // TODO: Only allow internal draining?
            /*
            @Override
            public boolean canDrain() {
                return true;
            }
            */
        };

        tankOutput = new FluidTankNuclearPhysics(Fluid.BUCKET_VOLUME * 10) {
            // TODO: Only allow internal filling?
            /*
            @Override
            public boolean canFill() {
                return false;
            }
            */
        };

        inputSlot = 1;
        outputSlot = 2;

        tankInputFillSlot = 3;
        tankInputDrainSlot = 4;
        tankOutputFillSlot = 5;
        tankOutputDrainSlot = 6;
    }

    @Override
    public void update() {
        super.update();

        if (timer > 0) {
            rotation += 0.2;
        } else {
            rotation = 0;
        }

        if (!world.isRemote) {
            if (canProcess()) {
                EnergyUtility.discharge(0, this);

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
                    NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
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

    public boolean canProcess() {
        FluidStack inputFluidStack = tankInput.getFluid();

        if (inputFluidStack != null) {
            if (inputFluidStack.amount >= Fluid.BUCKET_VOLUME && OreDictionaryHelper.isUraniumOre(inventory.getStackInSlot(inputSlot))) {
                //if (isItemValidForSlot(outputSlot, new ItemStack(QuantumItems.itemYellowCake))) {;
                    return true;
                //}
            }

            if (tankOutput.getFluidAmount() < tankOutput.getCapacity()) {
                FluidStack outputFluidStack = tankOutput.getFluid();

                if (inputFluidStack.isFluidEqual(ModFluids.fluidStackDeuterium) && inputFluidStack.amount >= ConfigurationManager.General.deutermiumPerTritium * extractSpeed) {
                    if (outputFluidStack == null || outputFluidStack.getFluid() == ModFluids.gasTritium) {
                        return true;
                    }
                }

                if (inputFluidStack.isFluidEqual(ModFluids.fluidStackWater) && inputFluidStack.amount >= ConfigurationManager.General.waterPerDeutermium * extractSpeed) {
                    if (outputFluidStack == null || outputFluidStack.getFluid() == ModFluids.gasDeuterium) {
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
            if (OreDictionaryHelper.isUraniumOre(inventory.getStackInSlot(1))) { // inputSlot
                tankInput.drain(Fluid.BUCKET_VOLUME, true);
                inventory.insertItem(outputSlot, new ItemStack(ModItems.itemYellowCake, 3), false);
                InventoryUtility.decrStackSize(inventory, inputSlot);

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
                if (tankOutput.fillInternal(new FluidStack(ModFluids.gasDeuterium, extractSpeed), true) >= extractSpeed) {
                    tankInput.drainInternal(waterUsage * extractSpeed, true);

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

            if (drain != null && drain.amount >= 1 && drain.getFluid() == ModFluids.gasDeuterium) {
                if (tankOutput.fill(new FluidStack(ModFluids.gasTritium, extractSpeed), true) >= extractSpeed) {
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
