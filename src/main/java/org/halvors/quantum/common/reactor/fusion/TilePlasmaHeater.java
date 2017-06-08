package org.halvors.quantum.common.reactor.fusion;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.utility.LanguageUtility;
import org.halvors.quantum.lib.prefab.tile.TileElectrical;
import universalelectricity.api.energy.EnergyStorageHandler;
import universalelectricity.api.energy.UnitDisplay;
import universalelectricity.api.energy.UnitDisplay.Unit;

import java.util.HashMap;

public class TilePlasmaHeater extends TileElectrical implements IFluidHandler { //IPacketReceiver, ITagRender,
    public static long power = 10000000000L;
    public static int plasmaHeatAmount = 100; //@Config

    public final FluidTank tankInputDeuterium = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
    public final FluidTank tankInputTritium = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);
    public final FluidTank tankOutput = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 10);

    public float rotation = 0;

    public TilePlasmaHeater() {
        energy = new EnergyStorageHandler(power, power / 20);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        rotation += energy.getEnergy() / 10000f;

        if (!worldObj.isRemote) {
            if (energy.checkExtract()) {
                if (tankInputDeuterium.getFluidAmount() >= plasmaHeatAmount && tankInputTritium.getFluidAmount() >= plasmaHeatAmount) {
                    tankInputDeuterium.drain(plasmaHeatAmount, true);
                    tankInputTritium.drain(plasmaHeatAmount, true);
                    tankOutput.fill(new FluidStack(Quantum.fluidPlasma, plasmaHeatAmount), true);
                    energy.extractEnergy();
                }
            }
        }

        if (ticks % 80 == 0) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

            // TODO: Update clients.
            //PacketHandler.sendPacketToClients(getDescriptionPacket(), worldObj, new Vector3(this), 25);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        NBTTagCompound deuterium = tagCompound.getCompoundTag("tankInputDeuterium");
        tankInputDeuterium.setFluid(FluidStack.loadFluidStackFromNBT(deuterium));

        NBTTagCompound tritium = tagCompound.getCompoundTag("tankInputTritium");
        tankInputTritium.setFluid(FluidStack.loadFluidStackFromNBT(tritium));

        NBTTagCompound output = tagCompound.getCompoundTag("tankOutput");
        tankOutput.setFluid(FluidStack.loadFluidStackFromNBT(output));
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        if (tankInputDeuterium.getFluid() != null) {
            NBTTagCompound compound = new NBTTagCompound();
            tankInputDeuterium.getFluid().writeToNBT(compound);
            tagCompound.setTag("tankInputDeuterium", compound);
        }

        if (tankInputTritium.getFluid() != null) {
            NBTTagCompound compound = new NBTTagCompound();
            tankInputTritium.getFluid().writeToNBT(compound);
            tagCompound.setTag("tankInputTritium", compound);
        }

        if (tankOutput.getFluid() != null) {
            NBTTagCompound compound = new NBTTagCompound();
            tankOutput.getFluid().writeToNBT(compound);
            tagCompound.setTag("tankOutput", compound);
        }
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
    */

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);

        //return ResonantInduction.PACKET_TILE.getPacket(this, nbt);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tagCompound);
    }

    /*
    @Override
    public float addInformation(HashMap<String, Integer> map, EntityPlayer player) {
        if (energy != null) {
            map.put(LanguageUtility.localize("tooltip.energy") + ": " + UnitDisplay.getDisplay(energy.getEnergy(), Unit.JOULES), 0xFFFFFF);
        }

        if (tankInputDeuterium.getFluidAmount() > 0) {
            map.put(LanguageUtility.localize("fluid.deuterium") + ": " + tankInputDeuterium.getFluidAmount() + " L", 0xFFFFFF);
        }

        if (tankInputTritium.getFluidAmount() > 0) {
            map.put(LanguageUtility.localize("fluid.tritium") + ": " + tankInputTritium.getFluidAmount() + " L", 0xFFFFFF);
        }

        if (tankOutput.getFluidAmount() > 0) {
            map.put(LanguageUtility.localize("fluid.plasma") + ": " + tankOutput.getFluidAmount() + " L", 0xFFFFFF);
        }

        return 1.5f;
    }
    */

    @Override
    public long onReceiveEnergy(ForgeDirection from, long receive, boolean doReceive) {
        if (tankInputDeuterium.getFluidAmount() > 0 && tankInputTritium.getFluidAmount() > 0) {
            return super.onReceiveEnergy(from, receive, doReceive);
        }

        return 0;
    }

    @Override
    public long onExtractEnergy(ForgeDirection from, long extract, boolean doExtract) {
        return 0;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource.isFluidEqual(Quantum.fluidStackDeuterium)) {
            return tankInputDeuterium.fill(resource, doFill);
        }

        if (resource.isFluidEqual(Quantum.fluidStackTritium)) {
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
        return fluid == Quantum.fluidDeuterium || fluid == Quantum.fluidTritium;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return fluid == Quantum.fluidPlasma;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { tankInputDeuterium.getInfo(), tankInputTritium.getInfo(), tankOutput.getInfo() };
    }

}
