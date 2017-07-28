package org.halvors.quantum.common.tile.machine;

import cofh.api.energy.EnergyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.QuantumFluids;
import org.halvors.quantum.common.QuantumItems;
import org.halvors.quantum.common.fluid.FluidTankInputOutput;
import org.halvors.quantum.common.network.packet.PacketTileEntity;
import org.halvors.quantum.common.utility.OreDictionaryUtility;

import javax.annotation.Nonnull;
import java.util.List;

public class TileNuclearBoiler extends TileProcess {
    public static final int tickTime = 20 * 15;
    public static final int energy = 21000;

    public final FluidTankInputOutput tank = new FluidTankInputOutput(new FluidTank(QuantumFluids.fluidStackWater.copy(), Fluid.BUCKET_VOLUME * 5), new FluidTank(QuantumFluids.fluidStackUraniumHexaflouride.copy(), Fluid.BUCKET_VOLUME * 5));

    // How many ticks has this item been extracting for?
    public int timer = 0; // Synced
    public float rotation = 0;

    public TileNuclearBoiler() {
        super(5);

        energyStorage = new EnergyStorage(energy * 2);

        outputSlot = 1;

        tankInputFillSlot = 2;
        tankInputDrainSlot = 3;
        tankOutputDrainSlot = 4;
    }

    @Override
    public void update() {
        super.update();

        rotation += 0.1;

        if (timer > 0) {
            rotation += 0.1;
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

                            waterTank.fill(liquid, true);
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

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        timer = tag.getInteger("timer");
        tank.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);

        tag.setInteger("timer", timer);
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

        timer = dataStream.readInt();
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        super.getPacketData(objects);

        objects.addAll(tank.getPacketData(objects));
        objects.add(timer);

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    @Override
    public FluidTank getInputTank() {
        return tank.getInputTank();
    }

    @Override
    public FluidTank getOutputTank() {
        return tank.getOutputTank();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        switch (slot) {
            case 1:
                return OreDictionaryUtility.isWaterCell(itemStack);

            case 3:
                return itemStack.getItem() == Quantum.itemYellowCake;
        }

        return false;
    }
    */

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return true;

        /*
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
        */
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return side == EnumFacing.DOWN ? new int[] { 2 } : new int[] { 1, 3 };
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, EnumFacing side) {
        return isItemValidForSlot(slot, itemStack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, EnumFacing side) {
        return slot == 2;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Check all conditions and see if we can start smelting
    public boolean canProcess() {
        if (tank.getInputTank().getFluid() != null) {
            if (tank.getInputTank().getFluid().amount >= Fluid.BUCKET_VOLUME) {
                if (getStackInSlot(1) != null) {
                    if (getStackInSlot(1).getItem() == QuantumItems.itemYellowCake || OreDictionaryUtility.isUraniumOre(getStackInSlot(1))) {
                        if (tank.getOutputTank().getFluid().amount < tank.getOutputTank().getCapacity()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    // Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack.
    public void doProcess() {
        if (canProcess()) {
            tank.getInputTank().drain(Fluid.BUCKET_VOLUME, true);
            FluidStack liquid = QuantumFluids.fluidStackUraniumHexaflouride.copy();
            liquid.amount = ConfigurationManager.General.uraniumHexaflourideRatio * 2;
            tank.getOutputTank().fill(liquid, true);
            decrStackSize(1, 1);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nonnull EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) tank;
        }

        return super.getCapability(capability, facing);
    }
}
