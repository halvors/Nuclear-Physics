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
import org.halvors.nuclearphysics.common.science.unit.UnitDisplay;
import org.halvors.nuclearphysics.common.tile.TileMachine;
import org.halvors.nuclearphysics.common.type.EnumColor;
import org.halvors.nuclearphysics.common.type.EnumRedstoneControl;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.HashMap;
import java.util.List;

public class TilePlasmaHeater extends TileMachine implements IFluidHandler, ITagRender {
    private static final String NBT_TANK_DEUTERIUM = "tankInputDeuterium";
    private static final String NBT_TANK_TRITIUM = "tankInputTritium";
    private static final String NBT_TANK_OUTPUT = "tankOutput";
    private static final int TICKS_REQUIRED = 20 * 20;
    private static final int ENERGY_PER_TICK = 25000;
    private static final int PLASMA_HEAT_AMOUNT = 100;

    public final LiquidTank tankInputDeuterium = new LiquidTank(FluidContainerRegistry.BUCKET_VOLUME * 10) {
        @Override
        public int fill(final FluidStack resource, final boolean doFill) {
            if (resource.isFluidEqual(ModFluids.fluidStackDeuterium)) {
                return super.fill(resource, doFill);
            }

            return 0;
        }
    };

    public final LiquidTank tankInputTritium = new LiquidTank(FluidContainerRegistry.BUCKET_VOLUME * 10) {
        @Override
        public int fill(final FluidStack resource, final boolean doFill) {
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

    public TilePlasmaHeater(final EnumMachine type) {
        super(type);

        redstoneControl = EnumRedstoneControl.LOW;
        energyStorage = new EnergyStorage(ENERGY_PER_TICK * 20);
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);

        tankInputDeuterium.readFromNBT(tag.getCompoundTag(NBT_TANK_DEUTERIUM));
        tankInputTritium.readFromNBT(tag.getCompoundTag(NBT_TANK_TRITIUM));
        tankOutput.readFromNBT(tag.getCompoundTag(NBT_TANK_OUTPUT));
    }

    @Override
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setTag(NBT_TANK_DEUTERIUM, tankInputDeuterium.writeToNBT(new NBTTagCompound()));
        tag.setTag(NBT_TANK_TRITIUM, tankInputTritium.writeToNBT(new NBTTagCompound()));
        tag.setTag(NBT_TANK_OUTPUT, tankOutput.writeToNBT(new NBTTagCompound()));
    }

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

            if (worldObj.getWorldTime() % 10 == 0) {
                NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(final ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (worldObj.isRemote) {
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
    public int fill(final ForgeDirection from, final FluidStack resource, final boolean doFill) {
        if (resource.isFluidEqual(ModFluids.fluidStackDeuterium)) {
            return tankInputDeuterium.fill(resource, doFill);
        } else if (resource.isFluidEqual(ModFluids.fluidStackTritium)) {
            return tankInputTritium.fill(resource, doFill);
        }

        return 0;
    }

    @Override
    public FluidStack drain(final ForgeDirection from, final FluidStack resource, final boolean doDrain) {
        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(final ForgeDirection from, final int maxDrain, final boolean doDrain) {
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
            tankInputDeuterium.drain(PLASMA_HEAT_AMOUNT, true);
            tankInputTritium.drain(PLASMA_HEAT_AMOUNT, true);
            tankOutput.fill(new FluidStack(ModFluids.plasma, PLASMA_HEAT_AMOUNT), true);
        }
    }
}
