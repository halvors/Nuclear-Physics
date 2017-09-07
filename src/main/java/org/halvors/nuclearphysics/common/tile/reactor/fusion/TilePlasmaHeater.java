package org.halvors.nuclearphysics.common.tile.reactor.fusion;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
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
import org.halvors.nuclearphysics.api.tile.ITagRender;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.nuclearphysics.common.capabilities.fluid.LiquidTank;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.tile.machine.TileMachine;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.energy.UnitDisplay;
import org.halvors.nuclearphysics.common.utility.type.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class TilePlasmaHeater extends TileMachine implements ITickable, IFluidHandler, ITagRender {
    //public static long power = 10000000000L;
    public static int power = 128000; // TODO: Figure out what this should be.
    public static int plasmaHeatAmount = 100; // TODO: Configuration option for this?

    private final LiquidTank tankInputDeuterium = new LiquidTank(ModFluids.fluidStackDeuterium.copy(), Fluid.BUCKET_VOLUME * 10);
    private final LiquidTank tankInputTritium = new LiquidTank(ModFluids.fluidStackTritium.copy(), Fluid.BUCKET_VOLUME * 10);
    private final LiquidTank tankOutput = new LiquidTank(ModFluids.fluidStackPlasma.copy(), Fluid.BUCKET_VOLUME * 10);

    public float rotation = 0;

    public TilePlasmaHeater() {
        this(EnumMachine.PLASMA_HEATER);
    }

    public TilePlasmaHeater(EnumMachine type) {
        super(type);

        energyStorage = new EnergyStorage(power);
    }

    @Override
    public void update() {
        rotation = (rotation + energyStorage.getEnergyStored() / 10000F);

        if (!world.isRemote) {
            if (energyStorage.getEnergyStored() >= power / 20) {
                if (tankInputDeuterium.getFluidAmount() >= plasmaHeatAmount && tankInputTritium.getFluidAmount() >= TilePlasmaHeater.plasmaHeatAmount && tankOutput.getFluidAmount() < tankOutput.getCapacity()) {
                    tankInputDeuterium.drainInternal(TilePlasmaHeater.plasmaHeatAmount, true);
                    tankInputTritium.drainInternal(TilePlasmaHeater.plasmaHeatAmount, true);
                    tankOutput.fillInternal(new FluidStack(ModFluids.plasma, tankOutput.getCapacity()), true);

                    energyStorage.extractEnergy(Math.toIntExact(power / 20), false);
                }
            }

            if (world.getWorldTime() % 10 == 0) {
                NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }
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
        super.writeToNBT(tag);

        tag.setTag("tankInputDeuterium", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tankInputDeuterium, null));
        tag.setTag("tankInputTritium", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tankInputTritium, null));
        tag.setTag("tankOutput", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tankOutput, null));

        return tag;
    }

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
        return new IFluidTankProperties[] { new FluidTankPropertiesWrapper(tankInputDeuterium), new FluidTankPropertiesWrapper(tankInputTritium), new FluidTankPropertiesWrapper(tankOutput) };
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (resource.isFluidEqual(ModFluids.fluidStackDeuterium)) {
            tankInputDeuterium.fill(resource, doFill);
        } else if (resource.isFluidEqual(ModFluids.fluidStackTritium)) {
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
    public float addInformation(HashMap<String, Integer> map, EntityPlayer player) {
        if (energyStorage != null) {
            map.put(LanguageUtility.transelate("tooltip.energy") + ": " + UnitDisplay.getDisplay(energyStorage.getEnergyStored(), UnitDisplay.Unit.JOULES), Color.WHITE.getHex());
        }

        if (tankInputDeuterium.getFluidAmount() > 0) {
            map.put(LanguageUtility.transelate("fluid.deuterium") + ": " + tankInputDeuterium.getFluidAmount() + " L", Color.WHITE.getHex());
        }

        if (tankInputTritium.getFluidAmount() > 0) {
            map.put(LanguageUtility.transelate("fluid.tritium") + ": " + tankInputTritium.getFluidAmount() + " L", Color.WHITE.getHex());
        }

        if (tankOutput.getFluidAmount() > 0) {
            map.put(LanguageUtility.transelate("fluid.plasma") + ": " + tankOutput.getFluidAmount() + " L", Color.WHITE.getHex());
        }

        return 1.5F;
    }
}
