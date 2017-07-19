package org.halvors.quantum.common.tile.machine;

import cofh.api.energy.EnergyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.QuantumFluids;
import org.halvors.quantum.common.QuantumItems;
import org.halvors.quantum.common.network.PacketHandler;
import org.halvors.quantum.common.network.packet.PacketTileEntity;
import org.halvors.quantum.common.tile.ITileNetwork;
import org.halvors.quantum.common.utility.OreDictionaryUtility;

import java.util.List;

public class TileChemicalExtractor extends TileProcess implements ITileNetwork, IFluidHandler {
    public static final int tickTime = 20 * 14;
    private static final int extractSpeed = 100;
    public static final int energy = 20000;

    public final FluidTank inputTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10); // Synced
    public final FluidTank outputTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10); // Synced

    // How many ticks has this item been extracting for?
    public int timer = 0; // Synced
    public float rotation = 0;

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

        rotation += 0.2;

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
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        timer = nbt.getInteger("timer");

        NBTTagCompound inputTankCompound = nbt.getCompoundTag("inputTank");
        inputTank.setFluid(FluidStack.loadFluidStackFromNBT(inputTankCompound));

        NBTTagCompound outputTankCompound = nbt.getCompoundTag("outputTank");
        outputTank.setFluid(FluidStack.loadFluidStackFromNBT(outputTankCompound));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("timer", timer);

        if (inputTank.getFluid() != null) {
            NBTTagCompound inputTankCompound = new NBTTagCompound();
            inputTank.getFluid().writeToNBT(inputTankCompound);
            tagCompound.setTag("inputTank", inputTankCompound);
        }

        if (outputTank.getFluid() != null) {
            NBTTagCompound outputTankCompound = new NBTTagCompound();
            outputTank.getFluid().writeToNBT(outputTankCompound);
            tagCompound.setTag("outputTank", outputTankCompound);
        }

        return tagCompound;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) throws Exception {
        if (world.isRemote) {
            timer = dataStream.readInt();

            if (dataStream.readBoolean()) {
                inputTank.setFluid(FluidStack.loadFluidStackFromNBT(PacketHandler.readNBT(dataStream)));
            }

            if (dataStream.readBoolean()) {
                outputTank.setFluid(FluidStack.loadFluidStackFromNBT(PacketHandler.readNBT(dataStream)));
            }
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        objects.add(timer);

        if (inputTank.getFluid() != null) {
            objects.add(true);

            NBTTagCompound compoundInputTank = new NBTTagCompound();
            inputTank.getFluid().writeToNBT(compoundInputTank);
            objects.add(compoundInputTank);
        } else {
            objects.add(false);
        }

        if (outputTank.getFluid() != null) {
            objects.add(true);

            NBTTagCompound compoundOutputTank = new NBTTagCompound();
            outputTank.getFluid().writeToNBT(compoundOutputTank);
            objects.add(compoundOutputTank);
        } else {
            objects.add(false);
        }

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        if (resource != null && canFill(from, resource.getFluid())) {
            return inputTank.fill(resource, doFill);
        }

        return 0;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return outputTank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return fluid.equals(FluidRegistry.WATER) || fluid.equals(QuantumFluids.gasDeuterium);
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return outputTank.getFluid() != null && fluid.equals(outputTank.getFluid());
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return new FluidTankInfo[] { inputTank.getInfo(), outputTank.getInfo() };
    }

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
        return inputTank;
    }

    @Override
    public FluidTank getOutputTank() {
        return outputTank;
    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        if (canProcess()) {
            return super.receiveEnergy(from, maxReceive, simulate);
        }

        return 0;
    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        return 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean canProcess() {
        if (inputTank.getFluid() != null) {
            if (inputTank.getFluid().amount >= FluidContainerRegistry.BUCKET_VOLUME && OreDictionaryUtility.isUraniumOre(getStackInSlot(inputSlot))) {
                if (isItemValidForSlot(outputSlot, new ItemStack(QuantumItems.itemYellowCake))) {
                    return true;
                }
            }

            if (outputTank.getFluidAmount() < outputTank.getCapacity()) {
                if (inputTank.getFluid().equals(QuantumFluids.fluidStackDeuterium) && inputTank.getFluid().amount >= ConfigurationManager.General.deutermiumPerTritium * extractSpeed) {
                    if (outputTank.getFluid() == null || outputTank.getFluid().equals(QuantumFluids.gasTritium)) {
                        return true;
                    }
                }

                if (inputTank.getFluid().equals(QuantumFluids.fluidStackWater) && inputTank.getFluid().amount >= ConfigurationManager.General.waterPerDeutermium * extractSpeed) {
                    if (outputTank.getFluid() == null || outputTank.getFluid().equals(QuantumFluids.gasDeuterium)) {
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
                inputTank.drain(FluidContainerRegistry.BUCKET_VOLUME, true);
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
            FluidStack drain = inputTank.drain(waterUsage * extractSpeed, false);

            if (drain != null && drain.amount >= 1 && drain.equals(FluidRegistry.WATER)) {
                if (outputTank.fill(new FluidStack(QuantumFluids.gasDeuterium, extractSpeed), true) >= extractSpeed) {
                    inputTank.drain(waterUsage * extractSpeed, true);

                    return true;
                }
            }
        }

        return false;
    }

    public boolean extractTritium() {
        if (canProcess()) {
            int deutermiumUsage = ConfigurationManager.General.deutermiumPerTritium;
            FluidStack drain = inputTank.drain(deutermiumUsage * extractSpeed, false);

            if (drain != null && drain.amount >= 1 && drain.equals(QuantumFluids.gasDeuterium)) {
                if (outputTank.fill(new FluidStack(QuantumFluids.gasTritium, extractSpeed), true) >= extractSpeed) {
                    inputTank.drain(deutermiumUsage * extractSpeed, true);

                    return true;
                }
            }
        }

        return false;
    }
}
