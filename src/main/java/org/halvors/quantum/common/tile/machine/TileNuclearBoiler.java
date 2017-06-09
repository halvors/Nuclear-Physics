package org.halvors.quantum.common.tile.machine;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.lib.IRotatable;
import org.halvors.quantum.lib.prefab.tile.TileElectricalInventory;

public class TileNuclearBoiler extends TileElectricalInventory implements ISidedInventory, IFluidHandler, IRotatable, IEnergyReceiver { // IPacketReceiver IVoltageInput
    public final static long DIAN = 50000;
    public final int SHI_JIAN = 20 * 15;
    //@Synced
    public final FluidTank waterTank = new FluidTank(Quantum.fluidStackWater.copy(), FluidContainerRegistry.BUCKET_VOLUME * 5);
    //@Synced
    public final FluidTank gasTank = new FluidTank(Quantum.fluidStackUraniumHexaflouride.copy(), FluidContainerRegistry.BUCKET_VOLUME * 5);
    // How many ticks has this item been extracting for?
    //@Synced
    public int timer = 0;
    public float rotation = 0;

    public TileNuclearBoiler() {
        energyStorage = new EnergyStorage((int) DIAN * 2);
        maxSlots = 4;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (timer > 0) {
            rotation += 0.1f;
        }

        if (!this.worldObj.isRemote) {
            // Put water as liquid
            if (getStackInSlot(1) != null) {
                if (FluidContainerRegistry.isFilledContainer(getStackInSlot(1))) {
                    FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(getStackInSlot(1));

                    if (liquid.isFluidEqual(Quantum.fluidStackWater)) {
                        if (this.fill(ForgeDirection.UNKNOWN, liquid, false) > 0) {
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

            if (nengYong()) {
                discharge(getStackInSlot(0));

                if (energyStorage.extractEnergy((int) DIAN, false) >= DIAN) {
                    if (timer == 0) {
                        timer = SHI_JIAN;
                    }

                    if (timer > 0) {
                        timer--;

                        if (timer < 1) {
                            yong();
                            timer = 0;
                        }
                    } else {
                        timer = 0;
                    }

                    energyStorage.extractEnergy((int) DIAN, true);
                }
            } else {
                timer = 0;
            }

            if (ticks % 10 == 0) {
                sendDescPack();
            }
        }
    }

    /*
    @Override
    public void onReceivePacket(ByteArrayDataInput data, EntityPlayer player, Object... extra) {
        try {
            this.timer = data.readInt();
            this.waterTank.setFluid(new FluidStack(Quantum.fluidStackWater, data.readInt()));
            this.gasTank.setFluid(new FluidStack((Quantum.fluidStackUraniumHexaflouride, data.readInt()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

    @Override
    public Packet getDescriptionPacket() {
        // TODO: Fix this.
        //return ResonantInduction.PACKET_TILE.getPacket(this, this.timer, Atomic.getFluidAmount(this.waterTank.getFluid()), Atomic.getFluidAmount(this.gasTank.getFluid()));
        return null;
    }

    public void sendDescPack() {
        if (!this.worldObj.isRemote) {
            for (EntityPlayer player : this.getPlayersUsing()) {
                // TODO: Fix this.
                //PacketDispatcher.sendPacketToPlayer(getDescriptionPacket(), player);
            }
        }
    }

    // Check all conditions and see if we can start smelting
    public boolean nengYong() {
        if (this.waterTank.getFluid() != null) {
            if (this.waterTank.getFluid().amount >= FluidContainerRegistry.BUCKET_VOLUME) {
                if (getStackInSlot(3) != null) {
                    if (Quantum.itemYellowCake == getStackInSlot(3).getItem() || new ItemBlock(Quantum.blockUraniumOre) == getStackInSlot(3).getItem()) {
                        // TODO: Fix this.
                        //if (Atomic.getFluidAmount(gasTank.getFluid()) < gasTank.getCapacity()) {
                        //    return true;
                        //}
                    }
                }
            }
        }

        return false;
    }

    /** Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack. */
    public void yong() {
        if (this.nengYong()) {
            this.waterTank.drain(FluidContainerRegistry.BUCKET_VOLUME, true);
            FluidStack liquid = Quantum.fluidStackUraniumHexaflouride.copy();
            liquid.amount = ConfigurationManager.General.uraniumHexaflourideRatio * 2;
            this.gasTank.fill(liquid, true);
            this.decrStackSize(3, 1);
        }
    }

    /** Reads a tile entity from NBT. */
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        timer = nbt.getInteger("shiJian");

        NBTTagCompound waterCompound = nbt.getCompoundTag("water");
        waterTank.setFluid(FluidStack.loadFluidStackFromNBT(waterCompound));

        NBTTagCompound gasCompound = nbt.getCompoundTag("gas");
        gasTank.setFluid(FluidStack.loadFluidStackFromNBT(gasCompound));
    }

    /** Writes a tile entity to NBT. */
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        nbt.setInteger("shiJian", this.timer);

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

    /** Tank Methods */
    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (Quantum.fluidStackWater == resource) {
            return waterTank.fill(resource, doFill);
        }

        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (Quantum.fluidStackUraniumHexaflouride == resource) {
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
        return Quantum.fluidStackWater.getFluid() == fluid;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return Quantum.fluidUraniumHexaflouride == fluid;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { this.waterTank.getInfo(), this.gasTank.getInfo() };
    }

    /** Inventory */
    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemStack) {
        if (slotID == 1) {
            return Quantum.itemWaterCell == itemStack.getItem();
        } else if (slotID == 3) {
            return Quantum.itemYellowCake == itemStack.getItem();
        }

        return false;
    }

    @Override
    public int[] getSlotsForFace(int slotIn) {
        return slotIn == 0 ? new int[] { 2 } : new int[] { 1, 3 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemStack, int side) {
        return isItemValidForSlot(slotID, itemStack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, int j) {
        return slot == 2;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        if (nengYong()) {
            return super.receiveEnergy(from, maxReceive, simulate);
        }

        return 0;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        return 0;
    }
}
