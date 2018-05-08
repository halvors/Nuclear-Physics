package org.halvors.nuclearphysics.common.tile.reactor.fusion;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
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
import org.halvors.nuclearphysics.common.block.states.BlockStateMachine.EnumMachine;
import org.halvors.nuclearphysics.common.capabilities.energy.EnergyStorage;
import org.halvors.nuclearphysics.common.capabilities.fluid.GasTank;
import org.halvors.nuclearphysics.common.capabilities.fluid.LiquidTank;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.science.unit.UnitDisplay;
import org.halvors.nuclearphysics.common.tile.TileMachine;
import org.halvors.nuclearphysics.common.type.EnumColor;
import org.halvors.nuclearphysics.common.type.EnumRedstoneControl;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class TilePlasmaHeater extends TileMachine implements IFluidHandler, ITagRender {
    private static final String NBT_TANK_DEUTERIUM = "tankInputDeuterium";
    private static final String NBT_TANK_TRITIUM = "tankInputTritium";
    private static final String NBT_TANK_OUTPUT = "tankOutput";
    private static final int TICKS_REQUIRED = 20 * 20;
    private static final int ENERGY_PER_TICK = 25000;
    private static final int PLASMA_HEAT_AMOUNT = 100;

    public final LiquidTank tankInputDeuterium = new LiquidTank(Fluid.BUCKET_VOLUME * 10) {
        @Override
        public int fill(final FluidStack resource, final boolean doFill) {
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
        public int fill(final FluidStack resource, final boolean doFill) {
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

    public TilePlasmaHeater(final EnumMachine type) {
        super(type);

        redstoneControl = EnumRedstoneControl.LOW;
        energyStorage = new EnergyStorage(ENERGY_PER_TICK * 20);
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);

        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(tankInputDeuterium, null, tag.getTag(NBT_TANK_DEUTERIUM));
        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(tankInputTritium, null, tag.getTag(NBT_TANK_TRITIUM));
        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(tankOutput, null, tag.getTag(NBT_TANK_OUTPUT));
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setTag(NBT_TANK_DEUTERIUM, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tankInputDeuterium, null));
        tag.setTag(NBT_TANK_TRITIUM, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tankInputTritium, null));
        tag.setTag(NBT_TANK_OUTPUT, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tankOutput, null));

        return tag;
    }

    @Override
    public boolean hasCapability(@Nonnull final Capability<?> capability, @Nullable final EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull final Capability<T> capability, @Nullable final EnumFacing facing) {
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
            if (canFunction() && canProcess() && energyStorage.extractEnergy(ENERGY_PER_TICK, true) >= ENERGY_PER_TICK) {
                if (operatingTicks < TICKS_REQUIRED) {
                    operatingTicks++;
                } else {
                    process();
                    reset();
                }

                energyUsed = energyStorage.extractEnergy(ENERGY_PER_TICK, false);
            }

            if (!canProcess()) {
                reset();
            }

            if (world.getWorldTime() % 10 == 0) {
                NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(final ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (world.isRemote) {
            tankInputDeuterium.handlePacketData(dataStream);
            tankInputTritium.handlePacketData(dataStream);
            tankOutput.handlePacketData(dataStream);
        }
    }

    @Override
    public List<Object> getPacketData(final List<Object> objects) {
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
    public int fill(final FluidStack resource, final boolean doFill) {
        if (resource.isFluidEqual(ModFluids.fluidStackDeuterium)) {
            return tankInputDeuterium.fill(resource, doFill);
        } else if (resource.isFluidEqual(ModFluids.fluidStackTritium)) {
            return tankInputTritium.fill(resource, doFill);
        }

        return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(final FluidStack resource, final boolean doDrain) {
        return drain(resource.amount, doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(final int maxDrain, final boolean doDrain) {
        return tankOutput.drain(maxDrain, doDrain);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    @SideOnly(Side.CLIENT)
    public float addInformation(final HashMap<String, Integer> map, final EntityPlayer player) {
        if (energyStorage.getEnergyStored() > 0) {
            map.put(LanguageUtility.transelate("tooltip.energy") + ": " + UnitDisplay.getEnergyDisplay(energyStorage.getEnergyStored()), EnumColor.WHITE.getHex());
        }

        if (tankInputDeuterium.getFluidAmount() > 0) {
            map.put(LanguageUtility.transelate("fluid.deuterium") + ": " + tankInputDeuterium.getFluidAmount() + " L", EnumColor.WHITE.getHex());
        }

        if (tankInputTritium.getFluidAmount() > 0) {
            map.put(LanguageUtility.transelate("fluid.tritium") + ": " + tankInputTritium.getFluidAmount() + " L", EnumColor.WHITE.getHex());
        }

        if (tankOutput.getFluidAmount() > 0) {
            map.put(LanguageUtility.transelate("fluid.plasma") + ": " + tankOutput.getFluidAmount() + " L", EnumColor.WHITE.getHex());
        }

        return 1.5F;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean canProcess() {
        return tankOutput.getFluidAmount() < tankOutput.getCapacity() && tankInputDeuterium.getFluidAmount() >= PLASMA_HEAT_AMOUNT && tankInputTritium.getFluidAmount() >= PLASMA_HEAT_AMOUNT;
    }

    public void process() {
        if (canProcess()) {
            tankInputDeuterium.drainInternal(PLASMA_HEAT_AMOUNT, true);
            tankInputTritium.drainInternal(PLASMA_HEAT_AMOUNT, true);
            tankOutput.fillInternal(new FluidStack(ModFluids.plasma, PLASMA_HEAT_AMOUNT), true);
        }
    }
}
