package org.halvors.nuclearphysics.common.tile.machine;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.nuclearphysics.common.capabilities.energy.EnergyStorage;
import org.halvors.nuclearphysics.common.capabilities.fluid.LiquidTank;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.init.ModItems;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.utility.EnergyUtility;
import org.halvors.nuclearphysics.common.utility.FluidUtility;
import org.halvors.nuclearphysics.common.utility.OreDictionaryHelper;

public class TileChemicalExtractor extends TileProcess {
    private static final int ENERGY_PER_TICK = 20000;
    private static final int EXTRACT_SPEED = 100;
    public static final int TICKS_REQUIRED = 14 * 20;

    public TileChemicalExtractor() {
        this(EnumMachine.CHEMICAL_EXTRACTOR);
    }

    public TileChemicalExtractor(final EnumMachine type) {
        super(type, 7);

        energyStorage = new EnergyStorage(ENERGY_PER_TICK * 2);

        tankInput = new LiquidTank(FluidContainerRegistry.BUCKET_VOLUME * 10) {
            @Override
            public int fill(final FluidStack resource, final boolean doFill) {
                if (resource.isFluidEqual(ModFluids.fluidStackWater) || resource.isFluidEqual(ModFluids.fluidStackDeuterium)) {
                    return super.fill(resource, doFill);
                }

                return 0;
            }
        };

        tankOutput = new LiquidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);

        inputSlot = 1;
        outputSlot = 2;

        tankInputFillSlot = 3;
        tankInputDrainSlot = 4;
        tankOutputFillSlot = 5;
        tankOutputDrainSlot = 6;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (operatingTicks > 0) {
            rotation += 0.2;
        } else {
            rotation = 0;
        }

        if (!worldObj.isRemote) {
            if (worldObj.getWorldTime() % 20 == 0) {
                FluidUtility.transferFluidToNeighbors(worldObj, pos, this);
            }

            EnergyUtility.discharge(0, this);

            if (canFunction() && canProcess() && energyStorage.extractEnergy(ENERGY_PER_TICK, true) >= ENERGY_PER_TICK) {
                if (operatingTicks < TICKS_REQUIRED) {
                    operatingTicks++;
                } else {
                    if (!refineUranium()) {
                        if (!extractTritium()) {
                            extractDeuterium();
                        }
                    }

                    reset();
                }

                energyUsed = energyStorage.extractEnergy(ENERGY_PER_TICK, false);
            }

            if (!canProcess()) {
                reset();
            }

            if (worldObj.getWorldTime() % 10 == 0) {
                NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean canFill(final ForgeDirection from, final Fluid fluid) {
        return fluid.getID() == FluidRegistry.WATER.getID() || fluid.getID() == ModFluids.deuterium.getID();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isItemValidForSlot(final int slot, final ItemStack itemStack) {
        switch (slot) {
            case 0: // Battery input slot.
                return EnergyUtility.canBeDischarged(itemStack);

            case 1: // Item input slot.
                return OreDictionaryHelper.isUraniumOre(itemStack);

            case 2: // Item output slot.
                return OreDictionaryHelper.isYellowCake(itemStack);

            case 3: // Input tank fill slot.
            case 4: // Input tank drain slot.
            	boolean a,b,c;
            	a = FluidUtility.isEmptyContainer(itemStack);
            	b = FluidUtility.isFilledContainer(itemStack, FluidRegistry.WATER);
            	c = FluidUtility.isFilledContainer(itemStack, ModFluids.deuterium);
            	NuclearPhysics.getLogger().warn("*** isItemValidForSlot() returns [" + a + "," + b + "," + c + "]");
                return FluidUtility.isEmptyContainer(itemStack) || FluidUtility.isFilledContainer(itemStack, FluidRegistry.WATER) || FluidUtility.isFilledContainer(itemStack, ModFluids.deuterium);

            case 5: // Output tank fill slot.
                return FluidUtility.isEmptyContainer(itemStack);

            case 6: // Output tank drain slot.
                return FluidUtility.isEmptyContainer(itemStack) || FluidUtility.isFilledContainer(itemStack, ModFluids.deuterium) || FluidUtility.isFilledContainer(itemStack, ModFluids.tritium);
        }

        return false;
    }

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
        final FluidStack inputFluidStack = tankInput.getFluid();

        if (inputFluidStack != null) {
            if (inputFluidStack.isFluidEqual(ModFluids.fluidStackWater) && inputFluidStack.amount >= FluidContainerRegistry.BUCKET_VOLUME && OreDictionaryHelper.isUraniumOre(getStackInSlot(inputSlot))) {
                return true;
            }

            if (tankOutput.getFluidAmount() < tankOutput.getCapacity()) {
                final FluidStack outputFluidStack = tankOutput.getFluid();

                if (inputFluidStack.isFluidEqual(ModFluids.fluidStackDeuterium) && inputFluidStack.amount >= General.deutermiumPerTritium * EXTRACT_SPEED) {
                    if (outputFluidStack == null || outputFluidStack.getFluid() == ModFluids.tritium) {
                        return true;
                    }
                }

                if (inputFluidStack.isFluidEqual(ModFluids.fluidStackWater) && inputFluidStack.amount >= General.waterPerDeutermium * EXTRACT_SPEED) {
                    return outputFluidStack == null || outputFluidStack.getFluid() == ModFluids.deuterium;
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
            if (OreDictionaryHelper.isUraniumOre(getStackInSlot(inputSlot))) {
                tankInput.drain(FluidContainerRegistry.BUCKET_VOLUME, true);
                incrStackSize(outputSlot, new ItemStack(ModItems.itemYellowCake, 3));
                decrStackSize(inputSlot, 1);

                return true;
            }
        }

        return false;
    }

    public boolean extractDeuterium() {
        if (canProcess()) {
            final int waterUsage = General.waterPerDeutermium;
            final FluidStack fluidStack = tankInput.drain(waterUsage * EXTRACT_SPEED, false);

            if (fluidStack != null && fluidStack.amount >= 1 && fluidStack.isFluidEqual(ModFluids.fluidStackWater)) {
                if (tankOutput.fill(new FluidStack(ModFluids.deuterium, EXTRACT_SPEED), true) >= EXTRACT_SPEED) {
                    tankInput.drain(waterUsage * EXTRACT_SPEED, true);

                    return true;
                }
            }
        }

        return false;
    }

    public boolean extractTritium() {
        if (canProcess()) {
            final int deutermiumUsage = General.deutermiumPerTritium;
            final FluidStack fluidStack = tankInput.drain(deutermiumUsage * EXTRACT_SPEED, false);

            if (fluidStack != null && fluidStack.amount >= 1 && fluidStack.isFluidEqual(ModFluids.fluidStackDeuterium)) {
                if (tankOutput.fill(new FluidStack(ModFluids.tritium, EXTRACT_SPEED), true) >= EXTRACT_SPEED) {
                    tankInput.drain(deutermiumUsage * EXTRACT_SPEED, true);

                    return true;
                }
            }
        }

        return false;
    }
}
