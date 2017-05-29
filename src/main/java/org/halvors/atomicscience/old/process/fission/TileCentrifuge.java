package org.halvors.atomicscience.old.process.fission;

import atomicscience.AtomicScience;
import atomicscience.Settings;
import calclavia.lib.network.IPacketReceiver;
import calclavia.lib.network.PacketTile;
import calclavia.lib.prefab.tile.IRotatable;
import calclavia.lib.prefab.tile.TileElectricalInventory;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import universalelectricity.api.CompatibilityModule;
import universalelectricity.api.electricity.IVoltageInput;
import universalelectricity.api.energy.EnergyStorageHandler;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorHelper;

public class TileCentrifuge
        extends TileElectricalInventory
        implements ISidedInventory, IPacketReceiver, IFluidHandler, IRotatable, IVoltageInput
{
    public static final int SHI_JIAN = 1200;
    public static final long DIAN = 500000L;
    public final FluidTank gasTank = new FluidTank(AtomicScience.FLUIDSTACK_URANIUM_HEXAFLOURIDE.copy(), 5000);
    public int timer = 0;
    public float rotation = 0.0F;

    public TileCentrifuge()
    {
        this.energy = new EnergyStorageHandler(1000000L);
        this.maxSlots = 4;
    }

    public void func_70316_g()
    {
        super.func_70316_g();
        if (this.timer > 0) {
            this.rotation += 0.45F;
        }
        if (!this.field_70331_k.field_72995_K)
        {
            if (this.ticks % 20L == 0L) {
                for (int i = 0; i < 6; i++)
                {
                    ForgeDirection direction = ForgeDirection.getOrientation(i);
                    TileEntity tileEntity = VectorHelper.getTileEntityFromSide(this.field_70331_k, new Vector3(this), direction);
                    if (((tileEntity instanceof IFluidHandler)) && (tileEntity.getClass() != getClass()))
                    {
                        IFluidHandler fluidHandler = (IFluidHandler)tileEntity;
                        if (fluidHandler != null)
                        {
                            FluidStack requestFluid = AtomicScience.FLUIDSTACK_URANIUM_HEXAFLOURIDE.copy();
                            requestFluid.amount = (this.gasTank.getCapacity() - AtomicScience.getFluidAmount(this.gasTank.getFluid()));
                            FluidStack receiveFluid = fluidHandler.drain(direction.getOpposite(), requestFluid, true);
                            if (receiveFluid != null) {
                                if (receiveFluid.amount > 0) {
                                    if (this.gasTank.fill(receiveFluid, false) > 0) {
                                        this.gasTank.fill(receiveFluid, true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (nengYong())
            {
                discharge(func_70301_a(0));
                if (this.energy.extractEnergy(500000L, false) >= 500000L)
                {
                    if (this.timer == 0) {
                        this.timer = 1200;
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
                    this.energy.extractEnergy(500000L, true);
                }
            }
            else
            {
                this.timer = 0;
            }
            if (this.ticks % 10L == 0L) {
                for (EntityPlayer player : getPlayersUsing()) {
                    PacketDispatcher.sendPacketToPlayer(func_70319_e(), (Player)player);
                }
            }
        }
    }

    public long onReceiveEnergy(ForgeDirection from, long receive, boolean doReceive)
    {
        if (nengYong()) {
            return super.onReceiveEnergy(from, receive, doReceive);
        }
        return 0L;
    }

    public void onReceivePacket(ByteArrayDataInput data, EntityPlayer player, Object... extra)
    {
        try
        {
            this.timer = data.readInt();
            this.gasTank.setFluid(new FluidStack(AtomicScience.FLUIDSTACK_URANIUM_HEXAFLOURIDE.fluidID, data.readInt()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Packet func_70319_e()
    {
        return AtomicScience.PACKET_TILE.getPacket(this, new Object[] { Integer.valueOf(this.timer), Integer.valueOf(AtomicScience.getFluidAmount(this.gasTank.getFluid())) });
    }

    public void func_70295_k_()
    {
        if (!this.field_70331_k.field_72995_K) {
            for (EntityPlayer player : getPlayersUsing()) {
                PacketDispatcher.sendPacketToPlayer(func_70319_e(), (Player)player);
            }
        }
    }

    public void func_70305_f() {}

    public boolean nengYong()
    {
        if (this.gasTank.getFluid() != null) {
            if (this.gasTank.getFluid().amount >= Settings.uraniumHexaflourideRatio) {
                return (func_94041_b(2, new ItemStack(AtomicScience.itemUranium))) && (func_94041_b(3, new ItemStack(AtomicScience.itemUranium, 1, 1)));
            }
        }
        return false;
    }

    public void yong()
    {
        if (nengYong())
        {
            this.gasTank.drain(Settings.uraniumHexaflourideRatio, true);
            if (this.field_70331_k.field_73012_v.nextFloat() > 0.6D) {
                incrStackSize(2, new ItemStack(AtomicScience.itemUranium));
            } else {
                incrStackSize(3, new ItemStack(AtomicScience.itemUranium, 1, 1));
            }
        }
    }

    public void func_70307_a(NBTTagCompound nbt)
    {
        super.func_70307_a(nbt);
        this.timer = nbt.func_74762_e("smeltingTicks");

        NBTTagCompound compound = nbt.func_74775_l("gas");
        this.gasTank.setFluid(FluidStack.loadFluidStackFromNBT(compound));
    }

    public void func_70310_b(NBTTagCompound nbt)
    {
        super.func_70310_b(nbt);
        nbt.func_74768_a("smeltingTicks", this.timer);
        if (this.gasTank.getFluid() != null)
        {
            NBTTagCompound compound = new NBTTagCompound();
            this.gasTank.getFluid().writeToNBT(compound);
            nbt.func_74782_a("gas", compound);
        }
    }

    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        if (AtomicScience.FLUIDSTACK_URANIUM_HEXAFLOURIDE.isFluidEqual(resource)) {
            return this.gasTank.fill(resource, doFill);
        }
        return 0;
    }

    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        return null;
    }

    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return null;
    }

    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return AtomicScience.FLUIDSTACK_URANIUM_HEXAFLOURIDE.fluidID == fluid.getID();
    }

    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return false;
    }

    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[] { this.gasTank.getInfo() };
    }

    public int[] func_94128_d(int side)
    {
        return new int[] { 2, side == 1 ? new int[] { 0, 1 } : 3 };
    }

    public boolean func_102007_a(int slotID, ItemStack itemStack, int side)
    {
        return (slotID == 1) && (func_94041_b(slotID, itemStack));
    }

    public boolean func_102008_b(int slotID, ItemStack itemstack, int j)
    {
        return (slotID == 2) || (slotID == 3);
    }

    public boolean func_94041_b(int i, ItemStack itemStack)
    {
        switch (i)
        {
            case 0:
                return CompatibilityModule.isHandler(itemStack.func_77973_b());
            case 1:
                return true;
            case 2:
                return itemStack.field_77993_c == AtomicScience.itemUranium.field_77779_bT;
            case 3:
                return itemStack.field_77993_c == AtomicScience.itemUranium.field_77779_bT;
        }
        return false;
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
