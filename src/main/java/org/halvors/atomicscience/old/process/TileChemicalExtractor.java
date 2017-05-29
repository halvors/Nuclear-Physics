package org.halvors.atomicscience.old.process;

import atomicscience.AtomicScience;
import atomicscience.Settings;
import calclavia.lib.network.PacketAnnotation;
import calclavia.lib.network.Synced;
import calclavia.lib.prefab.tile.IRotatable;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.electricity.IVoltageInput;
import universalelectricity.api.energy.EnergyStorageHandler;

public class TileChemicalExtractor
        extends TileProcess
        implements ISidedInventory, IFluidHandler, IRotatable, IVoltageInput
{
    public static final int TICK_TIME = 280;
    public static final int EXTRACT_SPEED = 100;
    public static final long ENERGY = 5000L;
    @Synced
    public final FluidTank inputTank = new FluidTank(10000);
    @Synced
    public final FluidTank outputTank = new FluidTank(10000);
    @Synced
    public int time = 0;
    public float rotation = 0.0F;

    public TileChemicalExtractor()
    {
        this.energy = new EnergyStorageHandler(10000L);
        this.maxSlots = 7;
        this.inputSlot = 1;
        this.outputSlot = 2;
        this.tankInputFillSlot = 3;
        this.tankInputDrainSlot = 4;
        this.tankOutputFillSlot = 5;
        this.tankOutputDrainSlot = 6;
    }

    public void func_70316_g()
    {
        super.func_70316_g();
        if (this.time > 0) {
            this.rotation += 0.2F;
        }
        if (!this.field_70331_k.field_72995_K)
        {
            if (canUse())
            {
                discharge(func_70301_a(0));
                if (this.energy.checkExtract(5000L))
                {
                    if (this.time == 0) {
                        this.time = 280;
                    }
                    if (this.time > 0)
                    {
                        this.time -= 1;
                        if (this.time < 1)
                        {
                            if (!refineUranium()) {
                                if (!extractTritium()) {
                                    extractDeuterium();
                                }
                            }
                            this.time = 0;
                        }
                    }
                    else
                    {
                        this.time = 0;
                    }
                }
                this.energy.extractEnergy(5000L, true);
            }
            else
            {
                this.time = 0;
            }
            if (this.ticks % 10L == 0L) {
                for (EntityPlayer player : getPlayersUsing()) {
                    PacketDispatcher.sendPacketToPlayer(func_70319_e(), (Player)player);
                }
            }
        }
    }

    public Packet func_70319_e()
    {
        return AtomicScience.PACKET_ANNOTATION.getPacket(this);
    }

    public boolean canUse()
    {
        if (this.inputTank.getFluid() != null)
        {
            if ((this.inputTank.getFluid().amount >= 1000) && (AtomicScience.isItemStackUraniumOre(func_70301_a(this.inputSlot)))) {
                if (func_94041_b(this.outputSlot, new ItemStack(AtomicScience.itemYellowCake))) {
                    return true;
                }
            }
            if (this.outputTank.getFluidAmount() < this.outputTank.getCapacity())
            {
                if ((this.inputTank.getFluid().getFluid().getID() == AtomicScience.FLUID_DEUTERIUM.getID()) && (this.inputTank.getFluid().amount >= Settings.deutermiumPerTritium * 100)) {
                    if ((this.outputTank.getFluid() == null) || (AtomicScience.FLUIDSTACK_TRITIUM.equals(this.outputTank.getFluid()))) {
                        return true;
                    }
                }
                if ((this.inputTank.getFluid().getFluid().getID() == FluidRegistry.WATER.getID()) && (this.inputTank.getFluid().amount >= Settings.waterPerDeutermium * 100)) {
                    if ((this.outputTank.getFluid() == null) || (AtomicScience.FLUIDSTACK_DEUTERIUM.equals(this.outputTank.getFluid()))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean refineUranium()
    {
        if (canUse()) {
            if (AtomicScience.isItemStackUraniumOre(func_70301_a(this.inputSlot)))
            {
                this.inputTank.drain(1000, true);
                incrStackSize(this.outputSlot, new ItemStack(AtomicScience.itemYellowCake, 3));
                func_70298_a(this.inputSlot, 1);
                return true;
            }
        }
        return false;
    }

    public boolean extractDeuterium()
    {
        if (canUse())
        {
            FluidStack drain = this.inputTank.drain(Settings.waterPerDeutermium * 100, false);
            if ((drain != null) && (drain.amount >= 1) && (drain.getFluid().getID() == FluidRegistry.WATER.getID())) {
                if (this.outputTank.fill(new FluidStack(AtomicScience.FLUIDSTACK_DEUTERIUM, 100), true) >= 100)
                {
                    this.inputTank.drain(Settings.waterPerDeutermium * 100, true);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean extractTritium()
    {
        if (canUse())
        {
            int waterUsage = Settings.deutermiumPerTritium;

            FluidStack drain = this.inputTank.drain(Settings.deutermiumPerTritium * 100, false);
            if ((drain != null) && (drain.amount >= 1) && (drain.getFluid().getID() == AtomicScience.FLUID_DEUTERIUM.getID())) {
                if (this.outputTank.fill(new FluidStack(AtomicScience.FLUIDSTACK_TRITIUM, 100), true) >= 100)
                {
                    this.inputTank.drain(Settings.deutermiumPerTritium * 100, true);
                    return true;
                }
            }
        }
        return false;
    }

    public void func_70307_a(NBTTagCompound nbt)
    {
        super.func_70307_a(nbt);

        this.time = nbt.func_74762_e("time");
        NBTTagCompound water = nbt.func_74775_l("inputTank");
        this.inputTank.setFluid(FluidStack.loadFluidStackFromNBT(water));
        NBTTagCompound deuterium = nbt.func_74775_l("outputTank");
        this.outputTank.setFluid(FluidStack.loadFluidStackFromNBT(deuterium));
    }

    public void func_70310_b(NBTTagCompound nbt)
    {
        super.func_70310_b(nbt);
        nbt.func_74768_a("time", this.time);
        if (this.inputTank.getFluid() != null)
        {
            NBTTagCompound compound = new NBTTagCompound();
            this.inputTank.getFluid().writeToNBT(compound);
            nbt.func_74782_a("inputTank", compound);
        }
        if (this.outputTank.getFluid() != null)
        {
            NBTTagCompound compound = new NBTTagCompound();
            this.outputTank.getFluid().writeToNBT(compound);
            nbt.func_74782_a("outputTank", compound);
        }
    }

    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        if ((resource != null) && (canFill(from, resource.getFluid()))) {
            return this.inputTank.fill(resource, doFill);
        }
        return 0;
    }

    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        return drain(from, resource.amount, doDrain);
    }

    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return this.outputTank.drain(maxDrain, doDrain);
    }

    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return (FluidRegistry.WATER.getID() == fluid.getID()) || (AtomicScience.FLUID_DEUTERIUM.getID() == fluid.getID());
    }

    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return (this.outputTank.getFluid() != null) && (this.outputTank.getFluid().getFluid().getID() == fluid.getID());
    }

    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[] { this.inputTank.getInfo(), this.outputTank.getInfo() };
    }

    public boolean func_94041_b(int slotID, ItemStack itemStack)
    {
        if (slotID == 0) {
            return CompatibilityModule.isHandler(itemStack.func_77973_b());
        }
        if (slotID == 1) {
            return AtomicScience.isItemStackWaterCell(itemStack);
        }
        if (slotID == 2) {
            return (AtomicScience.isItemStackDeuteriumCell(itemStack)) || (AtomicScience.isItemStackTritiumCell(itemStack));
        }
        if (slotID == 3) {
            return (AtomicScience.isItemStackEmptyCell(itemStack)) || (AtomicScience.isItemStackUraniumOre(itemStack)) || (AtomicScience.isItemStackDeuteriumCell(itemStack));
        }
        return false;
    }

    public int[] func_94128_d(int side)
    {
        return new int[] { 1, 2, 3 };
    }

    public boolean func_102007_a(int slotID, ItemStack itemStack, int side)
    {
        return func_94041_b(slotID, itemStack);
    }

    public boolean func_102008_b(int slotID, ItemStack itemstack, int side)
    {
        return slotID == 2;
    }

    public long onExtractEnergy(ForgeDirection from, long extract, boolean doExtract)
    {
        return 0L;
    }

    public long onReceiveEnergy(ForgeDirection from, long receive, boolean doReceive)
    {
        if (canUse()) {
            return super.onReceiveEnergy(from, receive, doReceive);
        }
        return 0L;
    }

    public long getVoltageInput(ForgeDirection from)
    {
        return 1000L;
    }

    public void onWrongVoltage(ForgeDirection direction, long voltage) {}

    public FluidTank getInputTank()
    {
        return this.inputTank;
    }

    public FluidTank getOutputTank()
    {
        return this.outputTank;
    }
}
