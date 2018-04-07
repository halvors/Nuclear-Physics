package org.halvors.nuclearphysics.common.tile.reactor.fusion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import org.halvors.nuclearphysics.api.tile.ITagRender;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.nuclearphysics.common.capabilities.energy.EnergyStorage;
import org.halvors.nuclearphysics.common.capabilities.fluid.GasTank;
import org.halvors.nuclearphysics.common.capabilities.fluid.LiquidTank;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.tile.TileMachine;
import org.halvors.nuclearphysics.common.type.Color;
import org.halvors.nuclearphysics.common.type.RedstoneControl;
import org.halvors.nuclearphysics.common.unit.UnitDisplay;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.HashMap;
import java.util.List;

public class TilePlasmaHeater extends TileMachine implements IFluidHandler, ITagRender {
    private static int ticksRequired = 20 * 20;
    private static int energyPerTick = 25000;
    private static int plasmaHeatAmount = 100;

    public final LiquidTank tankInputDeuterium = new LiquidTank(FluidContainerRegistry.BUCKET_VOLUME * 10) {
        @Override
        public int fill(FluidStack resource, boolean doFill) {
            if (resource.isFluidEqual(ModFluids.fluidStackDeuterium)) {
                return super.fill(resource, doFill);
            }

            return 0;
        }
    };

    public final LiquidTank tankInputTritium = new LiquidTank(FluidContainerRegistry.BUCKET_VOLUME * 10) {
        @Override
        public int fill(FluidStack resource, boolean doFill) {
            if (resource.isFluidEqual(ModFluids.fluidStackTritium)) {
                return super.fill(resource, doFill);
            }

            return 0;
        }
    };

    public final GasTank tankOutput = new GasTank(FluidContainerRegistry.BUCKET_VOLUME * 10);

    public float rotation = 0;

    public TilePlasmaHeater() {
        this(EnumMachine.PLASMA_HEATER);
    }

    public TilePlasmaHeater(EnumMachine type) {
        super(type);

        redstoneControl = RedstoneControl.LOW;
        energyStorage = new EnergyStorage(energyPerTick * 20);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        tankInputDeuterium.readFromNBT(tag.getCompoundTag("tankInputDeuterium"));
        tankInputTritium.readFromNBT(tag.getCompoundTag("tankInputTritium"));
        tankOutput.readFromNBT(tag.getCompoundTag("tankOutput"));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tankInputDeuterium.writeToNBT(tag.getCompoundTag("tankInputDeuterium"));
        tankInputTritium.writeToNBT(tag.getCompoundTag("tankInputTritium"));
        tankOutput.writeToNBT(tag.getCompoundTag("tankOutput"));
    }

    /*
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
    */

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (operatingTicks > 0) {
            rotation += 0.5;
        } else {
            rotation = 0;
        }

        if (!worldObj.isRemote) {
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
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource.isFluidEqual(ModFluids.fluidStackDeuterium)) {
            return tankInputDeuterium.fill(resource, doFill);
        } else if (resource.isFluidEqual(ModFluids.fluidStackTritium)) {
            return tankInputTritium.fill(resource, doFill);
        }

        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return tankOutput.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { tankInputDeuterium.getInfo(), tankInputTritium.getInfo(), tankOutput.getInfo() };
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    @SideOnly(Side.CLIENT)
    public float addInformation(HashMap<String, Integer> map, EntityPlayer player) {
        if (energyStorage.getEnergyStored() > 0) {
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
            tankInputDeuterium.drain(plasmaHeatAmount, true);
            tankInputTritium.drain(plasmaHeatAmount, true);
            tankOutput.fill(new FluidStack(ModFluids.plasma, plasmaHeatAmount), true);
        }
    }
}
