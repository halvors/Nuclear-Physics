package org.halvors.quantum.common.tile.machine;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.QuantumFluids;
import org.halvors.quantum.common.QuantumItems;
import org.halvors.quantum.common.fluid.tank.FluidTankQuantum;
import org.halvors.quantum.common.item.reactor.fission.ItemUranium.EnumUranium;
import org.halvors.quantum.common.network.packet.PacketTileEntity;

import javax.annotation.Nonnull;
import java.util.List;

public class TileGasCentrifuge extends TileMachine implements ITickable {
    public static final int tickTime = 20 * 60;
    private static final int energy = 20000;

    private final FluidTankQuantum tank = new FluidTankQuantum(QuantumFluids.fluidStackUraniumHexaflouride.copy(), Fluid.BUCKET_VOLUME * 5) {
        @Override
        public int fill(FluidStack resource, boolean doFill) {
            if (resource.isFluidEqual(QuantumFluids.fluidStackUraniumHexaflouride)) {
                return super.fill(resource, doFill);
            }

            return 0;
        }

        @Override
        public boolean canDrain() {
            return false;
        }
    };

    public float rotation = 0;

    public TileGasCentrifuge() {
        energyStorage = new EnergyStorage(energy * 2);
        inventory = new ItemStackHandler(4) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                markDirty();
            }

            private boolean isItemValidForSlot(int slot, ItemStack itemStack) {
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
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if (!isItemValidForSlot(slot, stack)) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    @Override
    public void update() {
        if (timer > 0) {
            rotation += 0.45;
        }

        if (!world.isRemote) {
            // TODO: Fix this?
            /*
            if (world.getWorldTime() % 20 == 0) {
                for (int side = 0; side < 6; side++) {
                    EnumFacing direction = EnumFacing.getFront(side);
                    TileEntity tileEntity = VectorHelper.getTileEntityFromSide(world, new Vector3(this), direction);

                    if (tileEntity instanceof IFluidHandler && tileEntity.getClass() != getClass()) {
                        IFluidHandler fluidHandler = (IFluidHandler) tileEntity;

                        FluidStack requestFluid = QuantumFluids.fluidStackUraniumHexaflouride.copy();
                        requestFluid.amount = (tank.getCapacity() - tank.getFluid().amount);
                        FluidStack receiveFluid = fluidHandler.drain(direction.getOpposite(), requestFluid, true);

                        if (receiveFluid != null) {
                            if (receiveFluid.amount > 0) {
                                if (tank.fill(receiveFluid, false) > 0) {
                                    tank.fill(receiveFluid, true);
                                }
                            }
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

        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(tank, null, tag.getTag("tank"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);

        tag.setTag("tank", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tank, null));

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

    public IFluidTank getTank() {
        return tank;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
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
    */

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean canProcess() {
        FluidStack fluidStack = tank.getFluid();

        if (fluidStack != null) {
            if (fluidStack.amount >= ConfigurationManager.General.uraniumHexaflourideRatio) {
                //return inventory.isItemValidForSlot(2, new ItemStack(QuantumItems.itemUranium)) && isItemValidForSlot(3, new ItemStack(QuantumItems.itemUranium, 1, 1));
                return true;
            }
        }

        return false;
    }

    public void doProcess() {
        if (canProcess()) {
            tank.drainInternal(ConfigurationManager.General.uraniumHexaflourideRatio, true);

            if (world.rand.nextFloat() > 0.6) {
                // TODO: setCount() for 1.12
                //inventory.incrStackSize(2, new ItemStack(QuantumItems.itemUranium));
                inventory.setStackInSlot(2, new ItemStack(QuantumItems.itemUranium));
            } else {
                //inventory.incrStackSize(3, new ItemStack(QuantumItems.itemUranium, 1, EnumUranium.URANIUM_238.ordinal()));
                inventory.setStackInSlot(3, new ItemStack(QuantumItems.itemUranium, 1, EnumUranium.URANIUM_238.ordinal()));
            }
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
