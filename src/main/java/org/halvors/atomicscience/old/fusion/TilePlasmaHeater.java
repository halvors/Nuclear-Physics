package org.halvors.atomicscience.old.fusion;

import atomicscience.AtomicScience;
import calclavia.lib.config.Config;
import calclavia.lib.network.IPacketReceiver;
import calclavia.lib.network.PacketHandler;
import calclavia.lib.network.PacketTile;
import calclavia.lib.prefab.tile.TileElectrical;
import calclavia.lib.render.ITagRender;
import calclavia.lib.utility.LanguageUtility;
import com.google.common.io.ByteArrayDataInput;
import java.util.HashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import universalelectricity.api.energy.EnergyStorageHandler;
import universalelectricity.api.energy.UnitDisplay;
import universalelectricity.api.energy.UnitDisplay.Unit;
import universalelectricity.api.vector.Vector3;

public class TilePlasmaHeater
        extends TileElectrical
        implements IPacketReceiver, ITagRender, IFluidHandler
{
    public static final long DIAN = 10000000000L;
    @Config
    public static final int plasmaHeatAmount = 100;
    public final FluidTank tankInputDeuterium = new FluidTank(10000);
    public final FluidTank tankInputTritium = new FluidTank(10000);
    public final FluidTank tankOutput = new FluidTank(10000);
    public float rotation = 0.0F;

    public TilePlasmaHeater()
    {
        this.energy = new EnergyStorageHandler(10000000000L, 500000000L);
    }

    public long onReceiveEnergy(ForgeDirection from, long receive, boolean doReceive)
    {
        if ((this.tankInputDeuterium.getFluidAmount() > 0) && (this.tankInputTritium.getFluidAmount() > 0)) {
            return super.onReceiveEnergy(from, receive, doReceive);
        }
        return 0L;
    }

    public void func_70316_g()
    {
        super.func_70316_g();

        this.rotation += (float)this.energy.getEnergy() / 10000.0F;
        if (!this.field_70331_k.field_72995_K) {
            if (this.energy.checkExtract()) {
                if ((this.tankInputDeuterium.getFluidAmount() >= 100) && (this.tankInputTritium.getFluidAmount() >= 100))
                {
                    this.tankInputDeuterium.drain(100, true);
                    this.tankInputTritium.drain(100, true);
                    this.tankOutput.fill(new FluidStack(AtomicScience.FLUID_PLASMA, 100), true);
                    this.energy.extractEnergy();
                }
            }
        }
        if (this.ticks % 80L == 0L) {
            PacketHandler.sendPacketToClients(func_70319_e(), this.field_70331_k, new Vector3(this), 25.0D);
        }
    }

    public Packet func_70319_e()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        func_70310_b(nbt);
        return AtomicScience.PACKET_TILE.getPacket(this, new Object[] { nbt });
    }

    public void onReceivePacket(ByteArrayDataInput data, EntityPlayer player, Object... extra)
    {
        try
        {
            func_70307_a(PacketHandler.readNBTTagCompound(data));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void func_70307_a(NBTTagCompound nbt)
    {
        super.func_70307_a(nbt);
        NBTTagCompound deuterium = nbt.func_74775_l("tankInputDeuterium");
        this.tankInputDeuterium.setFluid(FluidStack.loadFluidStackFromNBT(deuterium));
        NBTTagCompound tritium = nbt.func_74775_l("tankInputTritium");
        this.tankInputTritium.setFluid(FluidStack.loadFluidStackFromNBT(tritium));
        NBTTagCompound output = nbt.func_74775_l("tankOutput");
        this.tankOutput.setFluid(FluidStack.loadFluidStackFromNBT(output));
    }

    public void func_70310_b(NBTTagCompound nbt)
    {
        super.func_70310_b(nbt);
        if (this.tankInputDeuterium.getFluid() != null)
        {
            NBTTagCompound compound = new NBTTagCompound();
            this.tankInputDeuterium.getFluid().writeToNBT(compound);
            nbt.func_74782_a("tankInputDeuterium", compound);
        }
        if (this.tankInputTritium.getFluid() != null)
        {
            NBTTagCompound compound = new NBTTagCompound();
            this.tankInputTritium.getFluid().writeToNBT(compound);
            nbt.func_74782_a("tankInputTritium", compound);
        }
        if (this.tankOutput.getFluid() != null)
        {
            NBTTagCompound compound = new NBTTagCompound();
            this.tankOutput.getFluid().writeToNBT(compound);
            nbt.func_74782_a("tankOutput", compound);
        }
    }

    public float addInformation(HashMap<String, Integer> map, EntityPlayer player)
    {
        if (this.energy != null) {
            map.put(LanguageUtility.getLocal("tooltip.energy") + ": " + UnitDisplay.getDisplay(this.energy.getEnergy(), UnitDisplay.Unit.JOULES), Integer.valueOf(16777215));
        }
        if (this.tankInputDeuterium.getFluidAmount() > 0) {
            map.put(LanguageUtility.getLocal("fluid.deuterium") + ": " + this.tankInputDeuterium.getFluidAmount() + " L", Integer.valueOf(16777215));
        }
        if (this.tankInputTritium.getFluidAmount() > 0) {
            map.put(LanguageUtility.getLocal("fluid.tritium") + ": " + this.tankInputTritium.getFluidAmount() + " L", Integer.valueOf(16777215));
        }
        if (this.tankOutput.getFluidAmount() > 0) {
            map.put(LanguageUtility.getLocal("fluid.plasma") + ": " + this.tankOutput.getFluidAmount() + " L", Integer.valueOf(16777215));
        }
        return 1.5F;
    }

    public long onExtractEnergy(ForgeDirection from, long extract, boolean doExtract)
    {
        return 0L;
    }

    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        if (resource.isFluidEqual(AtomicScience.FLUIDSTACK_DEUTERIUM)) {
            return this.tankInputDeuterium.fill(resource, doFill);
        }
        if (resource.isFluidEqual(AtomicScience.FLUIDSTACK_TRITIUM)) {
            return this.tankInputTritium.fill(resource, doFill);
        }
        return 0;
    }

    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        return drain(from, resource.amount, doDrain);
    }

    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return this.tankOutput.drain(maxDrain, doDrain);
    }

    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return (fluid.getID() == AtomicScience.FLUID_DEUTERIUM.getID()) || (fluid.getID() == AtomicScience.FLUID_TRITIUM.getID());
    }

    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return fluid == AtomicScience.FLUID_PLASMA;
    }

    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[] { this.tankInputDeuterium.getInfo(), this.tankInputTritium.getInfo(), this.tankOutput.getInfo() };
    }
}
