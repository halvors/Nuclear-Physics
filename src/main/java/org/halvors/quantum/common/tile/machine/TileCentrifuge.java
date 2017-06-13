package org.halvors.quantum.common.tile.machine;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.base.tile.ITileNetworkable;
import org.halvors.quantum.common.network.NetworkHandler;
import org.halvors.quantum.common.network.packet.PacketTileEntity;
import org.halvors.quantum.common.tile.TileElectricInventory;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.common.transform.vector.VectorHelper;
import org.halvors.quantum.lib.IRotatable;

import java.util.List;

public class TileCentrifuge extends TileElectricInventory implements ITileNetworkable, ISidedInventory, IFluidHandler, IRotatable, IEnergyReceiver { // IPacketReceiver IVoltageInput
    public static final int tickTime = 20 * 60;
    public static final long energy = 500000;
    public final FluidTank gasTank = new FluidTank(Quantum.fluidStackUraniumHexaflouride.copy(), FluidContainerRegistry.BUCKET_VOLUME * 5);
    public int timer = 0;
    public float rotation = 0;

    public TileCentrifuge() {
        energyStorage = new EnergyStorage((int) energy * 2);
        maxSlots = 4;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (timer > 0) {
            rotation += 0.45;
        }

        if (!worldObj.isRemote) {
            if (worldObj.getWorldTime() % 20 == 0) {
                for (int i = 0; i < 6; i++) {
                    ForgeDirection direction = ForgeDirection.getOrientation(i);
                    TileEntity tileEntity = VectorHelper.getTileEntityFromSide(worldObj, new Vector3(this), direction);

                    if (tileEntity instanceof IFluidHandler && tileEntity.getClass() != getClass()) {
                        IFluidHandler fluidHandler = (IFluidHandler) tileEntity;

                        FluidStack requestFluid = Quantum.fluidStackUraniumHexaflouride.copy();
                        requestFluid.amount = (gasTank.getCapacity() - gasTank.getFluid().amount);
                        FluidStack receiveFluid = fluidHandler.drain(direction.getOpposite(), requestFluid, true);

                        if (receiveFluid != null) {
                            if (receiveFluid.amount > 0) {
                                if (gasTank.fill(receiveFluid, false) > 0) {
                                    gasTank.fill(receiveFluid, true);
                                }
                            }
                        }
                    }
                }
            }

            if (canProcess()) {
                // TODO: Implement this.
                //discharge(getStackInSlot(0));

                if (energyStorage.extractEnergy((int) energy, true) >= energy) {
                    if (timer == 0) {
                        timer = 1200;
                    }

                    if (timer > 0) {
                        timer -= 1;

                        if (this.timer < 1) {
                            process();
                            timer = 0;
                        }
                    } else {
                        timer = 0;
                    }

                    energyStorage.extractEnergy((int) energy, false);
                }
            } else {
                timer = 0;
            }

            if (worldObj.getWorldTime() % 10 == 0) {
                NetworkHandler.sendToReceivers(new PacketTileEntity(this), this);
            }
        }
    }



    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        timer = nbt.getInteger("smeltingTicks");

        NBTTagCompound compound = nbt.getCompoundTag("gas");
        gasTank.setFluid(FluidStack.loadFluidStackFromNBT(compound));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        nbt.setInteger("smeltingTicks", timer);

        if (gasTank.getFluid() != null) {
            NBTTagCompound compound = new NBTTagCompound();
            gasTank.getFluid().writeToNBT(compound);
            nbt.setTag("gas", compound);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) throws Exception {
        if (worldObj.isRemote) {
            timer = dataStream.readInt();

            if (dataStream.readBoolean()) {
                gasTank.setFluid(FluidStack.loadFluidStackFromNBT(NetworkHandler.readNBTTag(dataStream)));
            }
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        objects.add(timer);

        if (gasTank.getFluid() != null) {
            objects.add(true);

            NBTTagCompound compoundGasTank = new NBTTagCompound();
            gasTank.getFluid().writeToNBT(compoundGasTank);
            objects.add(compoundGasTank);
        } else {
            objects.add(false);
        }

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
        switch (index) {
            case 0:
                // TODO: Implement this.
                //return CompatibilityModule.isHandler(itemStack.getItem());
            case 1:
                return true;
            case 2:
            case 3:
                return itemStack.getItem() == Quantum.itemUranium;
        }

        return false;
    }

    @Override
    public void openChest() {
        if (!worldObj.isRemote) {
            NetworkHandler.sendToReceivers(new PacketTileEntity(this), this);
        }
    }

    @Override
    public void closeChest() {

    }

    @Override
    public int[] getSlotsForFace(int slotIn) {
        return slotIn == 1 ? new int[] { 0, 1 } : new int[] { 2, 3 };
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
        return slot == 1 && isItemValidForSlot(slot, itemStack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
        return slot == 2 || slot == 3;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource.isFluidEqual(Quantum.fluidStackUraniumHexaflouride)) {
            return gasTank.fill(resource, doFill);
        }

        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return fluid.getID() == Quantum.fluidUraniumHexaflouride.getID();
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { gasTank.getInfo() };
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
        if (gasTank.getFluid() != null) {
            if (gasTank.getFluid().amount >= ConfigurationManager.General.uraniumHexaflourideRatio) {
                return isItemValidForSlot(2, new ItemStack(Quantum.itemUranium)) && isItemValidForSlot(3, new ItemStack(Quantum.itemUranium, 1, 1));
            }
        }

        return false;
    }

    public void process() {
        if (canProcess()) {
            gasTank.drain(ConfigurationManager.General.uraniumHexaflourideRatio, true);

            if (worldObj.rand.nextFloat() > 0.6) {
                incrStackSize(2, new ItemStack(Quantum.itemUranium));
            } else {
                incrStackSize(3, new ItemStack(Quantum.itemUranium, 1, 1));
            }
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
