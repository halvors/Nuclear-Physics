package org.halvors.nuclearphysics.common.tile.process;

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
import org.halvors.nuclearphysics.common.capabilities.fluid.GasTank;
import org.halvors.nuclearphysics.common.capabilities.fluid.LiquidTank;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.utility.EnergyUtility;
import org.halvors.nuclearphysics.common.utility.FluidUtility;
import org.halvors.nuclearphysics.common.utility.OreDictionaryHelper;

public class TileNuclearBoiler extends TileProcess {
    public static final int ticksRequired = 15 * 20;
    private static final int energyPerTick = 20000;

    public TileNuclearBoiler() {
        this(EnumMachine.NUCLEAR_BOILER);
    }

    public TileNuclearBoiler(EnumMachine type) {
        super(type, 5);

        energyStorage = new EnergyStorage(energyPerTick * 2);

        tankInput = new LiquidTank(FluidContainerRegistry.BUCKET_VOLUME * 5) {
            @Override
            public int fill(FluidStack resource, boolean doFill) {
                if (resource.isFluidEqual(ModFluids.fluidStackWater)) {
                    return super.fill(resource, doFill);
                }

                return 0;
            }
        };

        tankOutput = new GasTank(FluidContainerRegistry.BUCKET_VOLUME * 5);

        inputSlot = 1;

        tankInputFillSlot = 2;
        tankInputDrainSlot = 3;
        tankOutputDrainSlot = 4;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (operatingTicks > 0) {
            rotation += 0.1;
        } else {
            rotation = 0;
        }

        if (!worldObj.isRemote) {
            if (worldObj.getWorldTime() % 20 == 0) {
                FluidUtility.transferFluidToNeighbors(worldObj, xCoord, yCoord, zCoord, this);
            }

            EnergyUtility.discharge(0, this);

            if (canFunction() && canProcess() && energyStorage.extractEnergy(energyPerTick, true) >= energyPerTick) {
                if (operatingTicks < ticksRequired) {
                    operatingTicks++;
                } else {
                    process();
                    reset();
                }

                energyUsed = energyStorage.extractEnergy(energyPerTick, false);
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
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return fluid.getID() == ModFluids.fluidStackWater.getFluidID();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        switch (slot) {
            case 0: // Battery input slot.
                return EnergyUtility.canBeDischarged(itemStack);

            case 1: // Item input slot.
                return OreDictionaryHelper.isUraniumOre(itemStack) || OreDictionaryHelper.isYellowCake(itemStack);

            case 2: // Input tank fill slot.
            case 3: // Input tank drain slot.
                return FluidUtility.isEmptyContainer(itemStack) || FluidUtility.isFilledContainer(itemStack, FluidRegistry.WATER);

            // TODO: Add uranium hexaflouride container here.
            //case 4: // Output tank drain slot.
            //    return OreDictionaryHelper.isEmptyCell(itemStack);
        }

        return false;
    }

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

    // Check all conditions and see if we can start processing
    public boolean canProcess() {
        FluidStack inputFluidStack = tankInput.getFluid();

        if (inputFluidStack != null && inputFluidStack.amount >= FluidContainerRegistry.BUCKET_VOLUME) {
            ItemStack itemStack = getStackInSlot(inputSlot);

            if (itemStack != null && (OreDictionaryHelper.isUraniumOre(itemStack) || OreDictionaryHelper.isYellowCake(itemStack))) {
                return tankOutput.getFluidAmount() < tankOutput.getCapacity();
            }
        }

        return false;
    }

    // Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack.
    public void process() {
        if (canProcess()) {
            tankInput.drain(FluidContainerRegistry.BUCKET_VOLUME, true);

            FluidStack fluidStack = new FluidStack(ModFluids.uraniumHexaflouride, General.uraniumHexaflourideRatio * 2);
            tankOutput.fill(fluidStack, true);

            decrStackSize(inputSlot, 1);
        }
    }
}
