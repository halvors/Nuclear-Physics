package org.halvors.atomicscience.old.process.fission;

import atomicscience.AtomicScience;
import atomicscience.Settings;
import calclavia.lib.network.IPacketReceiver;
import calclavia.lib.network.PacketTile;
import calclavia.lib.network.Synced;
import calclavia.lib.prefab.tile.IRotatable;
import calclavia.lib.prefab.tile.TileElectricalInventory;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import universalelectricity.api.electricity.IVoltageInput;
import universalelectricity.api.energy.EnergyStorageHandler;

public class TileNuclearBoiler
        extends TileElectricalInventory
        implements ISidedInventory, IPacketReceiver, IFluidHandler, IRotatable, IVoltageInput
{
    public static final long DIAN = 50000L;
    public final int SHI_JIAN = 300;
    @Synced
    public final FluidTank waterTank = new FluidTank(AtomicScience.FLUIDSTACK_WATER.copy(), 5000);
    @Synced
    public final FluidTank gasTank = new FluidTank(AtomicScience.FLUIDSTACK_URANIUM_HEXAFLOURIDE.copy(), 5000);
    @Synced
    public int timer = 0;
    public float rotation = 0.0F;

    public TileNuclearBoiler()
    {
        this.energy = new EnergyStorageHandler(100000L);
        this.maxSlots = 4;
    }

    public long onReceiveEnergy(ForgeDirection from, long receive, boolean doReceive)
    {
        if (nengYong()) {
            return super.onReceiveEnergy(from, receive, doReceive);
        }
        return 0L;
    }

    public void func_70316_g()
    {
        super.func_70316_g();
        if (this.timer > 0) {
            this.rotation += 0.1F;
        }
        if (!this.field_70331_k.field_72995_K)
        {
            if (func_70301_a(1) != null) {
                if (FluidContainerRegistry.isFilledContainer(func_70301_a(1)))
                {
                    FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(func_70301_a(1));
                    if (liquid.isFluidEqual(AtomicScience.FLUIDSTACK_WATER)) {
                        if (fill(ForgeDirection.UNKNOWN, liquid, false) > 0)
                        {
                            ItemStack resultingContainer = func_70301_a(1).func_77973_b().getContainerItemStack(func_70301_a(1));
                            if ((resultingContainer == null) && (func_70301_a(1).field_77994_a > 1)) {
                                func_70301_a(1).field_77994_a -= 1;
                            } else {
                                func_70299_a(1, resultingContainer);
                            }
                            this.waterTank.fill(liquid, true);
                        }
                    }
                }
            }
            if (nengYong())
            {
                discharge(func_70301_a(0));
                if (this.energy.extractEnergy(50000L, false) >= 50000L)
                {
                    if (this.timer == 0) {
                        this.timer = 300;
                    }
                    if (this.timer > 0)
                    {
                        this.timer -= 1;
                        if (this.timer < 1)
                        {
                            yong();
                            this.timer = 0;
                        }
                    }
                    else
                    {
                        this.timer = 0;
                    }
                    this.energy.extractEnergy(50000L, true);
                }
            }
            else
            {
                this.timer = 0;
            }
            if (this.ticks % 10L == 0L) {
                sendDescPack();
            }
        }
    }

    public void onReceivePacket(ByteArrayDataInput data, EntityPlayer player, Object... extra)
    {
        try
        {
            this.timer = data.readInt();
            this.waterTank.setFluid(new FluidStack(AtomicScience.FLUIDSTACK_WATER.fluidID, data.readInt()));
            this.gasTank.setFluid(new FluidStack(AtomicScience.FLUIDSTACK_URANIUM_HEXAFLOURIDE.fluidID, data.readInt()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Packet func_70319_e()
    {
        return AtomicScience.PACKET_TILE.getPacket(this, new Object[] { Integer.valueOf(this.timer), Integer.valueOf(AtomicScience.getFluidAmount(this.waterTank.getFluid())), Integer.valueOf(AtomicScience.getFluidAmount(this.gasTank.getFluid())) });
    }

    public void sendDescPack()
    {
        if (!this.field_70331_k.field_72995_K) {
            for (EntityPlayer player : getPlayersUsing()) {
                PacketDispatcher.sendPacketToPlayer(func_70319_e(), (Player)player);
            }
        }
    }

    public boolean nengYong()
    {
        if (this.waterTank.getFluid() != null) {
            if (this.waterTank.getFluid().amount >= 1000) {
                if (func_70301_a(3) != null) {
                    if ((AtomicScience.itemYellowCake.field_77779_bT == func_70301_a(3).field_77993_c) || (AtomicScience.isItemStackUraniumOre(func_70301_a(3)))) {
                        if (AtomicScience.getFluidAmount(this.gasTank.getFluid()) < this.gasTank.getCapacity()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void yong()
    {
        if (nengYong())
        {
            this.waterTank.drain(1000, true);
            FluidStack liquid = AtomicScience.FLUIDSTACK_URANIUM_HEXAFLOURIDE.copy();
            liquid.amount = (Settings.uraniumHexaflourideRatio * 2);
            this.gasTank.fill(liquid, true);
            func_70298_a(3, 1);
        }
    }

    public void func_70307_a(NBTTagCompound nbt)
    {
        super.func_70307_a(nbt);
        this.timer = nbt.func_74762_e("shiJian");

        NBTTagCompound waterCompound = nbt.func_74775_l("water");
        this.waterTank.setFluid(FluidStack.loadFluidStackFromNBT(waterCompound));

        NBTTagCompound gasCompound = nbt.func_74775_l("gas");
        this.gasTank.setFluid(FluidStack.loadFluidStackFromNBT(gasCompound));
    }

    public void func_70310_b(NBTTagCompound nbt)
    {
        super.func_70310_b(nbt);
        nbt.func_74768_a("shiJian", this.timer);
        if (this.waterTank.getFluid() != null)
        {
            NBTTagCompound compound = new NBTTagCompound();
            this.waterTank.getFluid().writeToNBT(compound);
            nbt.func_74782_a("water", compound);
        }
        if (this.gasTank.getFluid() != null)
        {
            NBTTagCompound compound = new NBTTagCompound();
            this.gasTank.getFluid().writeToNBT(compound);
            nbt.func_74782_a("gas", compound);
        }
    }

    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        if (AtomicScience.FLUIDSTACK_WATER.isFluidEqual(resource)) {
            return this.waterTank.fill(resource, doFill);
        }
        return 0;
    }

    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if (AtomicScience.FLUIDSTACK_URANIUM_HEXAFLOURIDE.isFluidEqual(resource)) {
            return this.gasTank.drain(resource.amount, doDrain);
        }
        return null;
    }

    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return this.gasTank.drain(maxDrain, doDrain);
    }

    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return AtomicScience.FLUIDSTACK_WATER.fluidID == fluid.getID();
    }

    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return AtomicScience.FLUIDSTACK_URANIUM_HEXAFLOURIDE.fluidID == fluid.getID();
    }

    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[] { this.waterTank.getInfo(), this.gasTank.getInfo() };
    }

    public boolean func_94041_b(int slotID, ItemStack itemStack)
    {
        if (slotID == 1) {
            return AtomicScience.isItemStackWaterCell(itemStack);
        }
        if (slotID == 3) {
            return itemStack.field_77993_c == AtomicScience.itemYellowCake.field_77779_bT;
        }
        return false;
    }

    public int[] func_94128_d(int side)
    {
        return new int[] { 1, side == 0 ? new int[] { 2 } : 3 };
    }

    public boolean func_102007_a(int slotID, ItemStack itemStack, int side)
    {
        return func_94041_b(slotID, itemStack);
    }

    public boolean func_102008_b(int slotID, ItemStack itemstack, int j)
    {
        return slotID == 2;
    }

    public long onExtractEnergy(ForgeDirection from, long extract, boolean doExtract)
    {
        return 0L;
    }

    public long getVoltageInput(ForgeDirection from)
    {
        return 1000L;
    }

    public void onWrongVoltage(ForgeDirection direction, long voltage) {}
}
