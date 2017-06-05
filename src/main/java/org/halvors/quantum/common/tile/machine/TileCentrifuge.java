package org.halvors.quantum.common.tile.machine;

import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import ibxm.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.network.NetworkHandler;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.common.transform.vector.VectorHelper;
import org.halvors.quantum.lib.IRotatable;
import org.halvors.quantum.lib.prefab.tile.TileElectricalInventory;
import universalelectricity.api.electricity.IVoltageInput;
import universalelectricity.api.energy.EnergyStorageHandler;

public class TileCentrifuge extends TileElectricalInventory implements ISidedInventory, IFluidHandler, IRotatable, IVoltageInput { //, IPacketReceiver
    public static final int SHI_JIAN = 1200;
    public static final long DIAN = 500000L;
    public final FluidTank gasTank = new FluidTank(Quantum.fluidStackUraniumHexaflouride.copy(), 5000);
    public int timer = 0;
    public float rotation = 0.0F;

    public TileCentrifuge() {
        this.energy = new EnergyStorageHandler(1000000L);
        this.maxSlots = 4;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (timer > 0) {
            rotation += 0.45F;
        }

        if (!worldObj.isRemote) {
            if (this.ticks % 20L == 0L) {
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

            if (nengYong()) {
                discharge(getStackInSlot(0));

                if (energy.extractEnergy(500000L, false) >= 500000L) {
                    if (timer == 0) {
                        timer = 1200;
                    }

                    if (timer > 0) {
                        timer -= 1;

                        if (this.timer < 1) {
                            yong();
                            timer = 0;
                        }
                    } else {
                        timer = 0;
                    }

                    energy.extractEnergy(500000L, true);
                }
            } else {
                timer = 0;
            }

            if (ticks % 10L == 0L) {
                for (EntityPlayer player : getPlayersUsing()) {
                    NetworkHandler.sendTo((IMessage) getDescriptionPacket(), (EntityPlayerMP) player);
                }
            }
        }
    }

    public void yong() {
        if (nengYong()) {
            gasTank.drain(ConfigurationManager.General.uraniumHexaflourideRatio, true);

            if (worldObj.rand.nextFloat() > 0.6D) {
                incrStackSize(2, new ItemStack(Quantum.itemUranium));
            } else {
                incrStackSize(3, new ItemStack(Quantum.itemUranium, 1, 1));
            }
        }
    }

    public boolean nengYong() {
        if (gasTank.getFluid() != null) {
            if (gasTank.getFluid().amount >= ConfigurationManager.General.uraniumHexaflourideRatio) {
                return isItemValidForSlot(2, new ItemStack(Quantum.itemUranium)) && isItemValidForSlot(3, new ItemStack(Quantum.itemUranium, 1, 1));
            }
        }

        return false;
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

        nbt.setInteger("smeltingTicks", this.timer);

        if (gasTank.getFluid() != null) {
            NBTTagCompound compound = new NBTTagCompound();
            gasTank.getFluid().writeToNBT(compound);
            nbt.setTag("gas", compound);
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        //TODO: Fix this.
        //return ResonantInduction.PACKET_TILE.getPacket(this, new Object[] { timer, Atomic.getFluidAmount(this.gasTank.getFluid()) });
        return null;
    }

    /* TODO: Fix this.
    public void onReceivePacket(ByteArrayDataInput data, EntityPlayer player, Object... extra) {
        try {
            this.timer = data.readInt();
            this.gasTank.setFluid(new FluidStack(Atomic.FLUIDSTACK_URANIUM_HEXAFLOURIDE.fluidID, data.readInt()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

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
            for (EntityPlayer player : getPlayersUsing()) {
                NetworkHandler.sendTo((IMessage) getDescriptionPacket(), (EntityPlayerMP) player);
            }
        }
    }

    @Override
    public void closeChest() {

    }

    @Override
    public int[] getSlotsForFace(int slotIn) {
        // TODO: Fix this.
        //return new int[] { 2, (slotIn == 1 ? new int[] { 0, 1 } : 3) };
        return new int[] {};
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemStack, int side) {
        return (slotID == 1) && (isItemValidForSlot(slotID, itemStack));
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, int j) {
        return (slotID == 2) || (slotID == 3);
    }


    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (Quantum.fluidStackUraniumHexaflouride.isFluidEqual(resource)) {
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
        return Quantum.fluidUraniumHexaflouride == fluid;
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
    public long getVoltageInput(ForgeDirection direction) {
        return 1000L;
    }

    @Override
    public void onWrongVoltage(ForgeDirection direction, long paramLong) {

    }

    @Override
    public long onReceiveEnergy(ForgeDirection from, long receive, boolean doReceive) {
        if (nengYong()) {
            return super.onReceiveEnergy(from, receive, doReceive);
        }

        return 0L;
    }

    @Override
    public long onExtractEnergy(ForgeDirection from, long extract, boolean doExtract) {
        return 0L;
    }
}
