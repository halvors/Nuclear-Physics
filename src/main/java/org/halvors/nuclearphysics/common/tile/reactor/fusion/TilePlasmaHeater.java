package org.halvors.nuclearphysics.common.tile.reactor.fusion;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.api.tile.ITagRender;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.nuclearphysics.common.capabilities.fluid.GasTank;
import org.halvors.nuclearphysics.common.capabilities.fluid.LiquidTank;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.tile.TileMachine;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.energy.UnitDisplay;
import org.halvors.nuclearphysics.common.utility.type.Color;
import org.halvors.nuclearphysics.common.utility.type.RedstoneControl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class TilePlasmaHeater extends TileMachine implements IFluidHandler, ITagRender {
    private static int energyPerTick = 25000;
    private static int plasmaHeatAmount = 100;

    public final LiquidTank tankInputDeuterium = new LiquidTank(Fluid.BUCKET_VOLUME * 10) {
        @Override
        public int fill(FluidStack resource, boolean doFill) {
            if (resource.isFluidEqual(ModFluids.fluidStackDeuterium)) {
                return super.fill(resource, doFill);
            }

            return 0;
        }

        @Override
        public boolean canDrain() {
            return false;
        }
    };

    public final LiquidTank tankInputTritium = new LiquidTank(Fluid.BUCKET_VOLUME * 10) {
        @Override
        public int fill(FluidStack resource, boolean doFill) {
            if (resource.isFluidEqual(ModFluids.fluidStackTritium)) {
                return super.fill(resource, doFill);
            }

            return 0;
        }

        @Override
        public boolean canDrain() {
            return false;
        }
    };

    public final GasTank tankOutput = new GasTank(Fluid.BUCKET_VOLUME * 10) {
        @Override
        public boolean canFill() {
            return false;
        }
    };

    public float rotation = 0;

    public TilePlasmaHeater() {
        this(EnumMachine.PLASMA_HEATER);
    }

    public TilePlasmaHeater(EnumMachine type) {
        super(type);

        ticksRequired = 20 * 20;
        redstoneControl = RedstoneControl.LOW;
        energyStorage = new EnergyStorage(energyPerTick * 20);
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
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) this;
        }

        return super.getCapability(capability, facing);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void update() {
        super.update();

        if (operatingTicks > 0) {
            rotation += 0.5;
        } else {
            rotation = 0;
        }

        if (!world.isRemote) {
            if (canFunction() && canProcess() && energyStorage.extractEnergy(energyPerTick, true) >= energyPerTick) {
                if (operatingTicks < ticksRequired) {
                    operatingTicks++;
                } else {
                    process();

                    operatingTicks = 0;
                }

                energyUsed = energyStorage.extractEnergy(energyPerTick, false);
            } else {
                operatingTicks = 0;
                energyUsed = 0;
            }

            if (world.getWorldTime() % 10 == 0) {
                NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }
        }
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
            return tankInputDeuterium.fill(resource, doFill);
        } else if (resource.isFluidEqual(ModFluids.fluidStackTritium)) {
            return tankInputTritium.fill(resource, doFill);
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
    @SideOnly(Side.CLIENT)
    public float addInformation(HashMap<String, Integer> map, EntityPlayer player) {
        if (energyStorage != null) {
            // TODO: Fix so that this is only done client side. (With proxy?)
            map.put(LanguageUtility.transelate("tooltip.energy") + ": " + UnitDisplay.getEnergyDisplay(energyStorage.getEnergyStored()), Color.WHITE.getHex());
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean canProcess() {
        return tankOutput.getFluidAmount() < tankOutput.getCapacity() && tankInputDeuterium.getFluidAmount() >= plasmaHeatAmount && tankInputTritium.getFluidAmount() >= plasmaHeatAmount;
    }

    public void process() {
        if (canProcess()) {
            tankInputDeuterium.drainInternal(plasmaHeatAmount, true);
            tankInputTritium.drainInternal(plasmaHeatAmount, true);
            tankOutput.fillInternal(new FluidStack(ModFluids.plasma, plasmaHeatAmount), true);
        }
    }
}
