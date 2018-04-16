package org.halvors.nuclearphysics.common.tile.machine;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.nuclearphysics.common.capabilities.energy.EnergyStorage;
import org.halvors.nuclearphysics.common.capabilities.fluid.GasTank;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.init.ModItems;
import org.halvors.nuclearphysics.common.item.reactor.fission.ItemUranium.EnumUranium;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.tile.TileInventoryMachine;
import org.halvors.nuclearphysics.common.utility.EnergyUtility;

import java.util.List;

public class TileGasCentrifuge extends TileInventoryMachine {
    public static final int ticksRequired = 60 * 20;
    private static final int energyPerTick = 20000;

    public float rotation = 0;

    private final GasTank tank = new GasTank(FluidContainerRegistry.BUCKET_VOLUME * 5) {
        @Override
        public int fill(FluidStack resource, boolean doFill) {
            if (resource.isFluidEqual(ModFluids.fluidStackUraniumHexaflouride)) {
                return super.fill(resource, doFill);
            }

            return 0;
        }
    };

    public TileGasCentrifuge() {
        this(EnumMachine.GAS_CENTRIFUGE);
    }

    public TileGasCentrifuge(EnumMachine type) {
        super(type, 4);

        energyStorage = new EnergyStorage(energyPerTick * 2);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        tank.readFromNBT(tag.getCompoundTag("tank"));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (operatingTicks > 0) {
            rotation += 0.45;
        } else {
            rotation = 0;
        }

        if (!worldObj.isRemote) {
            EnergyUtility.discharge(0, this);

            if (canFunction() && canProcess() && energyStorage.extractEnergy(energyPerTick, true) >= energyPerTick) {
                if (operatingTicks < ticksRequired) {
                    operatingTicks++;
                } else {
                    process();
                    reset();
                }

                energyUsed = energyStorage.extractEnergy(energyPerTick, false);
            }

            if (!canProcess()) {
                reset();
            }

            if (worldObj.getWorldTime() % 10 == 0) {
                NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (worldObj.isRemote) {
            tank.handlePacketData(dataStream);
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        super.getPacketData(objects);

        tank.getPacketData(objects);

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public IFluidTank getTank() {
        return tank;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        switch (slot) {
            case 0: // Battery input slot.
                return EnergyUtility.canBeDischarged(itemStack);

            // TODO: Add uranium hexaflouride container here.
            //case 1: // Input tank drain slot.
            //    return OreDictionaryHelper.isEmptyCell(itemStack);

            case 2: // Item output slot.
                return itemStack.getItem() == ModItems.itemUranium && itemStack.getMetadata() == EnumUranium.URANIUM_235.ordinal();

            case 3: // Item output slot.
                return itemStack.getItem() == ModItems.itemUranium && itemStack.getMetadata() == EnumUranium.URANIUM_238.ordinal();
        }

        return false;
    }

    /*
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

        return fluidStack != null && fluidStack.amount >= General.uraniumHexaflourideRatio;
    }

    public void process() {
        if (canProcess()) {
            tank.drain(General.uraniumHexaflourideRatio, true);

            if (worldObj.rand.nextFloat() > 0.6) {
                incrStackSize(2, new ItemStack(ModItems.itemUranium, 1, EnumUranium.URANIUM_235.ordinal()));
            } else {
                incrStackSize(3, new ItemStack(ModItems.itemUranium, 1, EnumUranium.URANIUM_238.ordinal()));
            }
        }
    }
}
