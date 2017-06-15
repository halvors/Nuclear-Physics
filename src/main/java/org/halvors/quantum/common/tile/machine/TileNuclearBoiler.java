package org.halvors.quantum.common.tile.machine;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.base.tile.ITileNetworkable;
import org.halvors.quantum.common.network.NetworkHandler;
import org.halvors.quantum.common.network.packet.PacketTileEntity;
import org.halvors.quantum.common.tile.TileElectricInventory;
import org.halvors.quantum.lib.IRotatable;
import org.halvors.quantum.lib.utility.FluidUtility;
import org.halvors.quantum.lib.utility.OreDictionaryUtility;

import java.util.List;

public class TileNuclearBoiler extends TileElectricInventory implements ITileNetworkable, IRotatable, IEnergyReceiver, IFluidHandler, ISidedInventory {
    public static final int tickTime = 20 * 15;
    public static final int energy = 21000;

    public final FluidTank waterTank = new FluidTank(Quantum.fluidStackWater.copy(), FluidContainerRegistry.BUCKET_VOLUME * 5); // Synced
    public final FluidTank gasTank = new FluidTank(Quantum.fluidStackUraniumHexaflouride.copy(), FluidContainerRegistry.BUCKET_VOLUME * 5); // Synced

    // How many ticks has this item been extracting for?
    public int timer = 0; // Synced
    public float rotation = 0;

    public TileNuclearBoiler() {
        energyStorage = new EnergyStorage(energy * 2);
        maxSlots = 4;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (timer > 0) {
            rotation += 0.1;
        }

        if (!worldObj.isRemote) {
            // Put water as liquid
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
                            process();
                            timer = 0;
                        }
                    } else {
                        timer = 0;
                    }

                    energyStorage.extractEnergy(energy, false);
                }
            } else {
                timer = 0;
            }

            if (worldObj.getWorldTime() % 10 == 0) {
                if (!worldObj.isRemote) {
                    NetworkHandler.sendToReceivers(new PacketTileEntity(this), this);
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        timer = nbt.getInteger("timer");

        NBTTagCompound waterCompound = nbt.getCompoundTag("water");
        waterTank.setFluid(FluidStack.loadFluidStackFromNBT(waterCompound));

        NBTTagCompound gasCompound = nbt.getCompoundTag("gas");
        gasTank.setFluid(FluidStack.loadFluidStackFromNBT(gasCompound));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        nbt.setInteger("timer", timer);

        if (waterTank.getFluid() != null) {
            NBTTagCompound compound = new NBTTagCompound();
            waterTank.getFluid().writeToNBT(compound);
            nbt.setTag("water", compound);
        }

        if (gasTank.getFluid() != null) {
            NBTTagCompound compound = new NBTTagCompound();
            gasTank.getFluid().writeToNBT(compound);
            nbt.setTag("gas", compound);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) throws Exception {
        if (dataStream.readBoolean()) {
            waterTank.setFluid(FluidStack.loadFluidStackFromNBT(NetworkHandler.readNBTTag(dataStream)));
        }

        if (dataStream.readBoolean()) {
            gasTank.setFluid(FluidStack.loadFluidStackFromNBT(NetworkHandler.readNBTTag(dataStream)));
        }

        timer = dataStream.readInt();
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        if (waterTank.getFluid() != null) {
            objects.add(true);

            NBTTagCompound compoundWaterTank = new NBTTagCompound();
            waterTank.getFluid().writeToNBT(compoundWaterTank);
            objects.add(compoundWaterTank);
        } else {
            objects.add(false);
        }

        if (gasTank.getFluid() != null) {
            objects.add(true);

            NBTTagCompound compoundGasTank = new NBTTagCompound();
            gasTank.getFluid().writeToNBT(compoundGasTank);
            objects.add(compoundGasTank);
        } else {
            objects.add(false);
        }

        objects.add(timer);

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource.isFluidEqual(Quantum.fluidStackWater)) {
            return waterTank.fill(resource, doFill);
        }

        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (resource.isFluidEqual(Quantum.fluidStackUraniumHexaflouride)) {
            return gasTank.drain(resource.amount, doDrain);
        }

        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return gasTank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return fluid.getID() == Quantum.fluidStackWater.getFluid().getID();
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return fluid.getID() == Quantum.fluidUraniumHexaflouride.getID();
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { waterTank.getInfo(), gasTank.getInfo() };
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        switch (slot) {
            case 1:
                return itemStack.getItem() == Quantum.itemWaterCell;

            case 3:
                return itemStack.getItem() == Quantum.itemYellowCake;
        }

        return false;
    }

    @Override
    public int[] getSlotsForFace(int slot) {
        return slot == 0 ? new int[] { 2 } : new int[] { 1, 3 };
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
        return isItemValidForSlot(slot, itemStack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
        return slot == 2;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Check all conditions and see if we can start smelting
    public boolean canProcess() {
        if (waterTank.getFluid() != null) {
            if (waterTank.getFluid().amount >= FluidContainerRegistry.BUCKET_VOLUME) {
                if (getStackInSlot(3) != null) {
                    if (getStackInSlot(3).getItem() == Quantum.itemYellowCake || OreDictionaryUtility.isItemStackUraniumOre(getStackInSlot(3))) {
                        if (FluidUtility.getAmount(gasTank.getFluid()) < gasTank.getCapacity()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /** Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack. */
    public void process() {
        if (canProcess()) {
            waterTank.drain(FluidContainerRegistry.BUCKET_VOLUME, true);
            FluidStack liquid = Quantum.fluidStackUraniumHexaflouride.copy();
            liquid.amount = ConfigurationManager.General.uraniumHexaflourideRatio * 2;
            gasTank.fill(liquid, true);
            decrStackSize(3, 1);
        }
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
