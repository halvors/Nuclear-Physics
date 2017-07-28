package org.halvors.quantum.common.tile.machine;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.QuantumFluids;
import org.halvors.quantum.common.QuantumItems;
import org.halvors.quantum.common.fluid.tank.FluidTankInputOutput;
import org.halvors.quantum.common.network.packet.PacketTileEntity;
import org.halvors.quantum.common.utility.OreDictionaryUtility;

import javax.annotation.Nonnull;
import java.util.List;

public class TileChemicalExtractor extends TileProcess {
    public static final int tickTime = 20 * 14;
    private static final int extractSpeed = 100;
    public static final int energy = 20000;

    public float rotation = 0;

    public final FluidTankInputOutput tank = new FluidTankInputOutput(new FluidTank(Fluid.BUCKET_VOLUME * 10), new FluidTank(Fluid.BUCKET_VOLUME * 10));
    private final ItemStackHandler itemStorage = new ItemStackHandler(7);

    public TileChemicalExtractor() {
        super(7);

        energyStorage = new EnergyStorage(energy * 2);

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

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        tank.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);

        tag = tank.writeToNBT(tag);

        return tag;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (world.isRemote) {
            tank.handlePacketData(dataStream);
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        super.getPacketData(objects);

        objects.addAll(tank.getPacketData(objects));

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
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
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[] { 1, 2, 3 };
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, EnumFacing side) {
        return isItemValidForSlot(slot, itemStack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, EnumFacing side) {
        return slot == 2;
    }

    @Override
    public FluidTank getInputTank() {
        return tank.getInputTank();
    }

    @Override
    public FluidTank getOutputTank() {
        return tank.getOutputTank();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean canProcess() {;
        FluidStack inputFluidStack = tank.getInputTank().getFluid();

        if (inputFluidStack != null) {
            if (inputFluidStack.amount >= Fluid.BUCKET_VOLUME && OreDictionaryUtility.isUraniumOre(getStackInSlot(inputSlot))) {
                if (isItemValidForSlot(outputSlot, new ItemStack(QuantumItems.itemYellowCake))) {
                    return true;
                }
            }

            FluidTank outputTank = tank.getOutputTank();

            if (outputTank.getFluidAmount() < outputTank.getCapacity()) {
                FluidStack outputFluidStack = outputTank.getFluid();

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
            if (OreDictionaryUtility.isUraniumOre(getStackInSlot(inputSlot))) {
                tank.getInputTank().drain(Fluid.BUCKET_VOLUME, true);
                incrStackSize(outputSlot, new ItemStack(QuantumItems.itemYellowCake, 3));
                decrStackSize(inputSlot, 1);

                return true;
            }
        }

        return false;
    }

    public boolean extractDeuterium() {
        if (canProcess()) {
            int waterUsage = ConfigurationManager.General.waterPerDeutermium;
            FluidStack drain = tank.getInputTank().drain(waterUsage * extractSpeed, false);

            if (drain != null && drain.amount >= 1 && drain.getFluid() == FluidRegistry.WATER) {
                if (tank.getOutputTank().fill(new FluidStack(QuantumFluids.gasDeuterium, extractSpeed), true) >= extractSpeed) {
                    tank.getInputTank().drain(waterUsage * extractSpeed, true);

                    return true;
                }
            }
        }

        return false;
    }

    public boolean extractTritium() {
        if (canProcess()) {
            int deutermiumUsage = ConfigurationManager.General.deutermiumPerTritium;
            FluidStack drain = tank.getInputTank().drain(deutermiumUsage * extractSpeed, false);

            if (drain != null && drain.amount >= 1 && drain.getFluid() == QuantumFluids.gasDeuterium) {
                if (tank.getOutputTank().fill(new FluidStack(QuantumFluids.gasTritium, extractSpeed), true) >= extractSpeed) {
                    tank.getInputTank().drain(deutermiumUsage * extractSpeed, true);

                    return true;
                }
            }
        }

        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nonnull EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemStorage;
        } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) tank;
        }

        return super.getCapability(capability, facing);
    }
}
