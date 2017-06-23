package org.halvors.quantum.common.tile.machine;

import cofh.api.energy.EnergyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.base.tile.ITileNetworkable;
import org.halvors.quantum.common.network.PacketHandler;
import org.halvors.quantum.common.network.packet.PacketTileEntity;
import org.halvors.quantum.lib.IRotatable;
import org.halvors.quantum.lib.utility.OreDictionaryUtility;

import java.util.List;

public class TileChemicalExtractor extends TileProcess implements ITileNetworkable, IFluidHandler, IRotatable {
    public static final int tickTime = 20 * 14;
    private static final int extractSpeed = 100;
    public static final int energy = 20000;

    public final FluidTank inputTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10); // Synced
    public final FluidTank outputTank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10); // Synced

    // How many ticks has this item been extracting for?
    public int time = 0; // Synced
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
    public void updateEntity() {
        super.updateEntity();

        if (time > 0) {
            rotation += 0.2;
        }

        if (!worldObj.isRemote) {
            if (canProcess()) {
                // TODO: Implement this.
                //discharge(getStackInSlot(0));

                if (energyStorage.extractEnergy(energy, true) >= energy) {
                    if (time == 0) {
                        time = tickTime;
                    }

                    if (time > 0) {
                        time--;

                        if (time < 1) {
                            if (!refineUranium()) {
                                if (!extractTritium()) {
                                    extractDeuterium();
                                }
                            }

                            time = 0;
                        }
                    } else {
                        time = 0;
                    }

                    energyStorage.extractEnergy(energy, false);
                } else {
                    time = 0;
                }
            } else {
                time = 0;
            }

            if (worldObj.getWorldTime() % 10 == 0) {
                if (!worldObj.isRemote) {
                    Quantum.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        NBTTagCompound inputTankCompound = nbt.getCompoundTag("inputTank");
        inputTank.setFluid(FluidStack.loadFluidStackFromNBT(inputTankCompound));

        NBTTagCompound outputTankCompound = nbt.getCompoundTag("outputTank");
        outputTank.setFluid(FluidStack.loadFluidStackFromNBT(outputTankCompound));

        time = nbt.getInteger("time");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        if (inputTank.getFluid() != null) {
            NBTTagCompound compound = new NBTTagCompound();
            inputTank.getFluid().writeToNBT(compound);
            nbt.setTag("inputTank", compound);
        }

        if (outputTank.getFluid() != null) {
            NBTTagCompound compound = new NBTTagCompound();
            outputTank.getFluid().writeToNBT(compound);
            nbt.setTag("outputTank", compound);
        }

        nbt.setInteger("time", time);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) throws Exception {
        if (worldObj.isRemote) {
            if (dataStream.readBoolean()) {
                inputTank.setFluid(FluidStack.loadFluidStackFromNBT(PacketHandler.readNBTTag(dataStream)));
            }

            if (dataStream.readBoolean()) {
                outputTank.setFluid(FluidStack.loadFluidStackFromNBT(PacketHandler.readNBTTag(dataStream)));
            }

            time = dataStream.readInt();
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
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

        objects.add(time);

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource != null && canFill(from, resource.getFluid())) {
            return inputTank.fill(resource, doFill);
        }

        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return outputTank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return fluid.getID() == FluidRegistry.WATER.getID() || fluid.getID() == Quantum.fluidDeuterium.getID();
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return outputTank.getFluid() != null && fluid.getID() == outputTank.getFluid().getFluidID();
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
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
    public int[] getSlotsForFace(int face) {
        return new int[] { 1, 2, 3 };
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
        return isItemValidForSlot(slot, itemStack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
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
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        if (canProcess()) {
            return super.receiveEnergy(from, maxReceive, simulate);
        }

        return 0;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        return 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean canProcess() {
        if (inputTank.getFluid() != null) {
            if (inputTank.getFluid().amount >= FluidContainerRegistry.BUCKET_VOLUME && OreDictionaryUtility.isUraniumOre(getStackInSlot(inputSlot))) {
                if (isItemValidForSlot(outputSlot, new ItemStack(Quantum.itemYellowCake))) {
                    return true;
                }
            }

            if (outputTank.getFluidAmount() < outputTank.getCapacity()) {
                if (inputTank.getFluid().getFluidID() == Quantum.fluidStackDeuterium.getFluidID() && inputTank.getFluid().amount >= ConfigurationManager.General.deutermiumPerTritium * extractSpeed) {
                    if (outputTank.getFluid() == null || outputTank.getFluid().getFluidID() == Quantum.fluidTritium.getID()) {
                        return true;
                    }
                }

                if (inputTank.getFluid().getFluidID() == Quantum.fluidStackWater.getFluidID() && inputTank.getFluid().amount >= ConfigurationManager.General.waterPerDeutermium * extractSpeed) {
                    if (outputTank.getFluid() == null || outputTank.getFluid().getFluidID() == Quantum.fluidDeuterium.getID()) {
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
                incrStackSize(outputSlot, new ItemStack(Quantum.itemYellowCake, 3));
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

            if (drain != null && drain.amount >= 1 && drain.getFluidID() == FluidRegistry.WATER.getID()) {
                if (outputTank.fill(new FluidStack(Quantum.fluidDeuterium, extractSpeed), true) >= extractSpeed) {
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

            if (drain != null && drain.amount >= 1 && drain.getFluidID() == Quantum.fluidDeuterium.getID()) {
                if (outputTank.fill(new FluidStack(Quantum.fluidTritium, extractSpeed), true) >= extractSpeed) {
                    inputTank.drain(deutermiumUsage * extractSpeed, true);

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public ForgeDirection getDirection() {
        return ForgeDirection.getOrientation(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
    }

    @Override
    public void setDirection(ForgeDirection direction) {
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, direction.ordinal(), 3);
    }
}
