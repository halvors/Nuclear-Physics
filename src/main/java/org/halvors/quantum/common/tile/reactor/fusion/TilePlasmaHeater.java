package org.halvors.quantum.common.tile.reactor.fusion;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.init.QuantumFluids;
import org.halvors.quantum.common.fluid.tank.GasTank;
import org.halvors.quantum.common.network.packet.PacketTileEntity;
import org.halvors.quantum.common.tile.machine.TileMachine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TilePlasmaHeater extends TileMachine implements ITickable, IFluidHandler {
    public static long power = 10000000000L;
    public static int plasmaHeatAmount = 100; //@Config

    private final GasTank tankInputDeuterium = new GasTank(QuantumFluids.fluidStackDeuterium.copy(), Fluid.BUCKET_VOLUME * 10);
    private final GasTank tankInputTritium = new GasTank(QuantumFluids.fluidStackTritium.copy(), Fluid.BUCKET_VOLUME * 10);
    private final GasTank tankOutput = new GasTank(QuantumFluids.plasmaStack.copy(), Fluid.BUCKET_VOLUME * 10);

    public float rotation = 0;
    private final int maxTransfer = (int) power / 20;

    public TilePlasmaHeater() {
        energyStorage = new EnergyStorage((int) power, maxTransfer);
    }

    @Override
    public void update() {
        rotation += energyStorage.getEnergyStored() / 10000F;

        if (!world.isRemote) {
            if (tankInputDeuterium.getFluidAmount() > 0 && tankInputTritium.getFluidAmount() > 0) {
                if (energyStorage.extractEnergy(maxTransfer, true) >= maxTransfer) {
                    if (tankInputDeuterium.getFluidAmount() >= plasmaHeatAmount && tankInputTritium.getFluidAmount() >= plasmaHeatAmount) {
                        tankInputDeuterium.drainInternal(plasmaHeatAmount, true);
                        tankInputTritium.drainInternal(plasmaHeatAmount, true);
                        tankOutput.fillInternal(new FluidStack(QuantumFluids.plasma, plasmaHeatAmount), true);

                        energyStorage.extractEnergy(maxTransfer, false);
                    }
                }
            }
        }

        if (world.getWorldTime() % 80 == 0) {
            // TODO: Is this still needed?
            //world.markBlockForUpdate(xCoord, yCoord, zCoord);

            Quantum.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(tankInputDeuterium, null, tag.getTag("tankInputDeuterium"));
        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(tankInputTritium, null, tag.getTag("tankInputTritium"));
        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(tankOutput, null, tag.getTag("tankOutput"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);

        tag.setTag("tankInputDeuterium", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tankInputDeuterium, null));
        tag.setTag("tankInputTritium", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tankInputTritium, null));
        tag.setTag("tankOutput", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tankOutput, null));

        return tag;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (world.isRemote) {
            tankInputDeuterium.handlePacketData(dataStream);
            tankInputTritium.handlePacketData(dataStream);
            tankOutput.handlePacketData(dataStream);
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        super.getPacketData(objects);

        tankInputDeuterium.getPacketData(objects);
        tankInputTritium.getPacketData(objects);
        tankOutput.getPacketData(objects);

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new IFluidTankProperties[] { new FluidTankPropertiesWrapper(tankInputDeuterium), new FluidTankPropertiesWrapper(tankInputDeuterium), new FluidTankPropertiesWrapper(tankOutput),};
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (resource.isFluidEqual(QuantumFluids.fluidStackDeuterium)) {
            tankInputDeuterium.fill(resource, doFill);
        } else if (resource.isFluidEqual(QuantumFluids.fluidStackTritium)) {
            tankInputTritium.fill(resource, doFill);
        }

        return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return drain(resource.amount, doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return tankOutput.drain(maxDrain, doDrain);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) this;
        }

        return super.getCapability(capability, facing);
    }

    /*
    @Override
    public void read(ByteBuf data, EntityPlayer player, PacketType) {
        try {
            readFromNBT(PacketHandler.readNBTTagCompound(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);

        //return ResonantInduction.PACKET_TILE.getPacket(this, nbt);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tagCompound);
    }

    @Override
    public float addInformation(HashMap<String, Integer> map, EntityPlayer player) {
        if (energy != null) {
            map.put(LanguageUtility.transelate("tooltip.energy") + ": " + UnitDisplay.getDisplay(energy.getEnergy(), Unit.JOULES), 0xFFFFFF);
        }

        if (tankInputDeuterium.getFluidAmount() > 0) {
            map.put(LanguageUtility.transelate("fluid.deuterium") + ": " + tankInputDeuterium.getFluidAmount() + " L", 0xFFFFFF);
        }

        if (tankInputTritium.getFluidAmount() > 0) {
            map.put(LanguageUtility.transelate("fluid.tritium") + ": " + tankInputTritium.getFluidAmount() + " L", 0xFFFFFF);
        }

        if (tankOutput.getFluidAmount() > 0) {
            map.put(LanguageUtility.transelate("fluid.plasma") + ": " + tankOutput.getFluidAmount() + " L", 0xFFFFFF);
        }

        return 1.5f;
    }
    */

    /*
    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        if (resource.isFluidEqual(QuantumFluids.fluidStackDeuterium)) {
            return tankInputDeuterium.fill(resource, doFill);
        }

        if (resource.isFluidEqual(QuantumFluids.fluidStackTritium)) {
            return tankInputTritium.fill(resource, doFill);
        }

        return 0;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return tankOutput.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return fluid.equals(QuantumFluids.gasDeuterium) || fluid.equals(QuantumFluids.gasTritium);
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return fluid.equals(QuantumFluids.plasma);
    }
    */
}
