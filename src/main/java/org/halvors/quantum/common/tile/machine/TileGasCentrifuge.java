package org.halvors.quantum.common.tile.machine;

import cofh.api.energy.EnergyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.*;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.QuantumFluids;
import org.halvors.quantum.common.QuantumItems;
import org.halvors.quantum.common.network.PacketHandler;
import org.halvors.quantum.common.network.packet.PacketTileEntity;
import org.halvors.quantum.common.tile.ITileNetwork;
import org.halvors.quantum.common.utility.transform.vector.Vector3;
import org.halvors.quantum.common.utility.transform.vector.VectorHelper;

import java.util.List;

public class TileGasCentrifuge extends TileMachine implements ITickable, IFluidHandler {
    public static final int tickTime = 20 * 60;
    private static final int energy = 20000;

    public final FluidTank gasTank = new FluidTank(QuantumFluids.fluidStackUraniumHexaflouride.copy(), Fluid.BUCKET_VOLUME * 5); // Synced

    public int timer = 0; // Synced
    public float rotation = 0;

    public TileGasCentrifuge() {
        super(4);

        energyStorage = new EnergyStorage(energy * 2);
    }

    @Override
    public void update() {
        rotation += 0.45;

        if (timer > 0) {
            rotation += 0.45;
        }

        if (!world.isRemote) {
            if (world.getWorldTime() % 20 == 0) {
                for (int side = 0; side < 6; side++) {
                    EnumFacing direction = EnumFacing.getFront(side);
                    TileEntity tileEntity = VectorHelper.getTileEntityFromSide(world, new Vector3(this), direction);

                    if (tileEntity instanceof IFluidHandler && tileEntity.getClass() != getClass()) {
                        IFluidHandler fluidHandler = (IFluidHandler) tileEntity;

                        FluidStack requestFluid = QuantumFluids.fluidStackUraniumHexaflouride.copy();
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
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        timer = tagCompound.getInteger("smeltingTicks");

        NBTTagCompound compound = tagCompound.getCompoundTag("gas");
        gasTank.setFluid(FluidStack.loadFluidStackFromNBT(compound));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("smeltingTicks", timer);

        if (gasTank.getFluid() != null) {
            NBTTagCompound compound = new NBTTagCompound();
            gasTank.getFluid().writeToNBT(compound);
            tagCompound.setTag("gas", compound);
        }

        return tagCompound;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (world.isRemote) {
            timer = dataStream.readInt();

            if (dataStream.readBoolean()) {
                gasTank.setFluid(FluidStack.loadFluidStackFromNBT(PacketHandler.readNBT(dataStream)));
            }
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        super.getPacketData(objects);

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
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        switch (slot) {
            case 0:
                // TODO: Implement this.
                //return CompatibilityModule.isHandler(itemStack.getItem());
                return true;
            case 1:
                return true;
            case 2:
            case 3:
                return itemStack.getItem() == QuantumItems.itemUranium;
        }

        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        if (!world.isRemote) {
            Quantum.getPacketHandler().sendTo(new PacketTileEntity(this), (EntityPlayerMP) player);
        }
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return side == EnumFacing.UP ? new int[] { 0, 1 } : new int[] { 2, 3 };
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, EnumFacing side) {
        return slot == 1 && isItemValidForSlot(slot, itemStack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, EnumFacing side) {
        return slot == 2 || slot == 3;
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        if (resource.isFluidEqual(QuantumFluids.fluidStackUraniumHexaflouride)) {
            return gasTank.fill(resource, doFill);
        }

        return 0;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return fluid.equals(QuantumFluids.gasUraniumHexaflouride);
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return new FluidTankInfo[] { gasTank.getInfo() };
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
        if (gasTank.getFluid() != null) {
            if (gasTank.getFluid().amount >= ConfigurationManager.General.uraniumHexaflourideRatio) {
                return isItemValidForSlot(2, new ItemStack(QuantumItems.itemUranium)) && isItemValidForSlot(3, new ItemStack(QuantumItems.itemUranium, 1, 1));
            }
        }

        return false;
    }

    public void doProcess() {
        if (canProcess()) {
            gasTank.drain(ConfigurationManager.General.uraniumHexaflourideRatio, true);

            if (world.rand.nextFloat() > 0.6) {
                incrStackSize(2, new ItemStack(QuantumItems.itemUranium));
            } else {
                incrStackSize(3, new ItemStack(QuantumItems.itemUranium, 1, 1));
            }
        }
    }
}
