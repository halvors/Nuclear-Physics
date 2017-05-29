package org.halvors.atomicscience.old.process.sensor;

import atomicscience.AtomicScience;
import calclavia.lib.content.module.TileBase;
import calclavia.lib.network.PacketAnnotation;
import calclavia.lib.network.PacketHandler;
import calclavia.lib.network.Synced;
import calclavia.lib.prefab.item.ItemBlockSaved;
import calclavia.lib.thermal.ThermalGrid;
import calclavia.lib.utility.inventory.InventoryUtility;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.ILuaContext;
import dan200.computer.api.IPeripheral;
import java.util.ArrayList;
import li.cil.oc.api.network.Arguments;
import li.cil.oc.api.network.Callback;
import li.cil.oc.api.network.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorWorld;

//@Optional.Interface(iface="li.cil.oc.api.network.SimpleComponent", modid="OpenComputers")
public class TileThermometer
        extends TileBase
        implements IPeripheral, SimpleComponent
{
    public static final int MAX_THRESHOLD = 5000;
    private static Icon iconSide;
    @Synced
    public float detectedTemperature = 295.0F;
    @Synced
    public Vector3 trackCoordinate;
    @Synced
    private int threshold = 1000;
    private float prevTemperature;

    public TileThermometer()
    {
        super(Material.field_76233_E);
        this.canProvidePower = true;
        this.normalRender = false;
        this.forceStandardRender = true;
        this.itemBlock = ItemBlockThermometer.class;
    }

    public Icon getIcon(int side, int meta)
    {
        return (side == 1) || (side == 0) ? super.getIcon(side, meta) : iconSide;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        super.registerIcons(iconRegister);
        iconSide = iconRegister.func_94245_a("atomicscience:machine");
    }

    protected boolean use(EntityPlayer player, int side, Vector3 hit)
    {
        if (player.func_70093_af()) {
            setThreshold(getThershold() + 100);
        } else {
            setThreshold(getThershold() - 100);
        }
        return true;
    }

    protected boolean configure(EntityPlayer player, int side, Vector3 hit)
    {
        if (player.func_70093_af()) {
            setThreshold(getThershold() - 10);
        } else {
            setThreshold(getThershold() + 10);
        }
        return true;
    }

    public int getStrongRedstonePower(IBlockAccess access, int side)
    {
        TileThermometer tileEntity = (TileThermometer)access.func_72796_p(x(), y(), z());
        return tileEntity.isOverThreshold() ? 15 : 0;
    }

    public ArrayList<ItemStack> getDrops(int metadata, int fortune)
    {
        return new ArrayList();
    }

    public void onRemove(int par5, int par6)
    {
        ItemStack stack = ItemBlockSaved.getItemStackWithNBT(func_70311_o(), world(), x(), y(), z());
        InventoryUtility.dropItemStack(world(), center(), stack);
    }

    public void func_70316_g()
    {
        super.func_70316_g();
        if (!this.field_70331_k.field_72995_K) {
            if (this.ticks % 10L == 0L)
            {
                this.detectedTemperature = 0.0F;
                if (this.trackCoordinate != null) {
                    this.detectedTemperature = ThermalGrid.instance().getTemperature(new VectorWorld(world(), this.trackCoordinate));
                } else {
                    this.detectedTemperature = ThermalGrid.instance().getTemperature(new VectorWorld(this));
                }
                if (this.prevTemperature != this.detectedTemperature)
                {
                    notifyChange();
                    PacketHandler.sendPacketToClients(func_70319_e(), this.field_70331_k, new Vector3(this), 25.0D);
                    this.prevTemperature = this.detectedTemperature;
                }
            }
        }
    }

    public Packet func_70319_e()
    {
        return AtomicScience.PACKET_ANNOTATION.getPacket(this);
    }

    public void setTrack(Vector3 track)
    {
        this.trackCoordinate = track;
    }

    public void func_70307_a(NBTTagCompound nbt)
    {
        super.func_70307_a(nbt);
        this.threshold = nbt.func_74762_e("threshold");
        if (nbt.func_74764_b("trackCoordinate")) {
            this.trackCoordinate = new Vector3(nbt.func_74775_l("trackCoordinate"));
        } else {
            this.trackCoordinate = null;
        }
    }

    public void func_70310_b(NBTTagCompound nbt)
    {
        super.func_70310_b(nbt);
        nbt.func_74768_a("threshold", this.threshold);
        if (this.trackCoordinate != null) {
            nbt.func_74766_a("trackCoordinate", this.trackCoordinate.writeToNBT(new NBTTagCompound()));
        }
    }

    public int getThershold()
    {
        return this.threshold;
    }

    public void setThreshold(int newThreshold)
    {
        this.threshold = (newThreshold % 5000);
        if (this.threshold <= 0) {
            this.threshold = 5000;
        }
        markUpdate();
    }

    public boolean isOverThreshold()
    {
        return this.detectedTemperature >= getThershold();
    }

    public String getType()
    {
        return "AS Thermometer";
    }

    public String[] getMethodNames()
    {
        return new String[] { "getTemperature", "getWarningTemperature", "setWarningTemperature", "isAboveWarningTemperature" };
    }

    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments)
            throws Exception
    {
        int getTemperature = 0;
        int getWarningTemperature = 1;
        int setWarningTemperature = 2;
        int isAboveWarningTemperature = 3;
        switch (method)
        {
            case 0:
                return new Object[] { Float.valueOf(this.detectedTemperature) };
            case 1:
                return new Object[] { Integer.valueOf(getThershold()) };
            case 3:
                return new Object[] { Boolean.valueOf(isOverThreshold()) };
            case 2:
                if (arguments.length <= 0) {
                    throw new IllegalArgumentException("Not enough Arguments. Must provide one argument");
                }
                if (arguments.length >= 2) {
                    throw new IllegalArgumentException("Too many Arguments. Must provide one argument");
                }
                if (!(arguments[0] instanceof Number)) {
                    throw new IllegalArgumentException("Invalid Argument. Must provide a number");
                }
                synchronized (this)
                {
                    setThreshold(((Integer)arguments[0]).intValue());
                }
                return new Object[] { Boolean.valueOf(this.threshold == ((Integer)arguments[0]).intValue() ? 1 : false) };
        }
        return null;
    }

    public boolean canAttachToSide(int side)
    {
        return true;
    }

    public void attach(IComputerAccess computer) {}

    public void detach(IComputerAccess computer) {}

    public String getComponentName()
    {
        return "AS_Thermometer";
    }

    @Callback
    @Optional.Method(modid="OpenComputers")
    public Object[] getTemperature(Context context, Arguments args)
    {
        return new Object[] { Float.valueOf(this.detectedTemperature) };
    }

    @Callback
    @Optional.Method(modid="OpenComputers")
    public Object[] getWarningTemperature(Context context, Arguments args)
    {
        return new Object[] { Integer.valueOf(getThershold()) };
    }

    @Callback
    @Optional.Method(modid="OpenComputers")
    public Object[] isAboveWarningTemperature(Context context, Arguments args)
    {
        return new Object[] { Boolean.valueOf(isOverThreshold()) };
    }

    @Callback
    @Optional.Method(modid="OpenComputers")
    public Object[] setWarningTemperature(Context context, Arguments args)
    {
        if (args.count() <= 0) {
            throw new IllegalArgumentException("Not enough Arguments. Must provide one argument");
        }
        if (args.count() >= 2) {
            throw new IllegalArgumentException("Too many Arguments. Must provide one argument");
        }
        if (!args.isInteger(0)) {
            throw new IllegalArgumentException("Invalid Argument. Must provide an Integer");
        }
        synchronized (this)
        {
            setThreshold(args.checkInteger(0));
        }
        return new Object[] { Boolean.valueOf(this.threshold == args.checkInteger(0) ? 1 : false) };
    }
}
