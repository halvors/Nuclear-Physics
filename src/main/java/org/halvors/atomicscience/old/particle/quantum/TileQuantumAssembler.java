package org.halvors.atomicscience.old.particle.quantum;

import atomicscience.AtomicScience;
import calclavia.api.atomicscience.QuantumAssemblerRecipes;
import calclavia.lib.network.IPacketReceiver;
import calclavia.lib.network.PacketTile;
import calclavia.lib.prefab.tile.TileElectricalInventory;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.electricity.IVoltageInput;
import universalelectricity.api.energy.EnergyStorageHandler;

public class TileQuantumAssembler
        extends TileElectricalInventory
        implements IPacketReceiver, IVoltageInput
{
    public static final long DIAN = 10000000000L;
    public final int SHI_JIAN = 2400;
    public int shiJian = 0;
    public float rotationYaw1;
    public float rotationYaw2;
    public float rotationYaw3;
    public EntityItem entityItem;

    public TileQuantumAssembler()
    {
        this.energy = new EnergyStorageHandler(10000000000L, 500000000L);
        this.maxSlots = 7;
    }

    public void func_70316_g()
    {
        super.func_70316_g();
        if (!this.field_70331_k.field_72995_K)
        {
            if (canProcess())
            {
                if (this.energy.checkExtract())
                {
                    if (this.shiJian == 0)
                    {
                        getClass();this.shiJian = 2400;
                    }
                    if (this.shiJian > 0)
                    {
                        this.shiJian -= 1;
                        if (this.shiJian < 1)
                        {
                            process();
                            this.shiJian = 0;
                        }
                    }
                    else
                    {
                        this.shiJian = 0;
                    }
                    this.energy.extractEnergy(10000000000L, true);
                }
            }
            else {
                this.shiJian = 0;
            }
            if (this.ticks % 10L == 0L) {
                for (EntityPlayer player : getPlayersUsing()) {
                    PacketDispatcher.sendPacketToPlayer(func_70319_e(), (Player)player);
                }
            }
        }
        else if (this.shiJian > 0)
        {
            if (this.ticks % 600L == 0L) {
                this.field_70331_k.func_72908_a(this.field_70329_l, this.field_70330_m, this.field_70327_n, "atomicscience:assembler", 0.7F, 1.0F);
            }
            this.rotationYaw1 += 3.0F;
            this.rotationYaw2 += 2.0F;
            this.rotationYaw3 += 1.0F;

            ItemStack itemStack = func_70301_a(6);
            if (itemStack != null)
            {
                itemStack = itemStack.func_77946_l();
                itemStack.field_77994_a = 1;
                if (this.entityItem == null) {
                    this.entityItem = new EntityItem(this.field_70331_k, 0.0D, 0.0D, 0.0D, itemStack);
                } else if (!itemStack.func_77969_a(this.entityItem.func_92059_d())) {
                    this.entityItem = new EntityItem(this.field_70331_k, 0.0D, 0.0D, 0.0D, itemStack);
                }
                this.entityItem.field_70292_b += 1;
            }
            else
            {
                this.entityItem = null;
            }
        }
    }

    public long onReceiveEnergy(ForgeDirection from, long receive, boolean doReceive)
    {
        if (canProcess()) {
            return super.onReceiveEnergy(from, receive, doReceive);
        }
        return 0L;
    }

    public void onReceivePacket(ByteArrayDataInput data, EntityPlayer player, Object... extra)
    {
        try
        {
            this.shiJian = data.readInt();
            int itemID = data.readInt();
            int itemAmount = data.readInt();
            int itemMeta = data.readInt();
            if ((itemID != -1) && (itemAmount != -1) && (itemMeta != -1)) {
                func_70299_a(6, new ItemStack(Item.field_77698_e[itemID], itemAmount, itemMeta));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Packet func_70319_e()
    {
        if (func_70301_a(6) != null) {
            return AtomicScience.PACKET_TILE.getPacket(this, new Object[] { Integer.valueOf(this.shiJian), Integer.valueOf(func_70301_a(6).field_77993_c), Integer.valueOf(func_70301_a(6).field_77994_a), Integer.valueOf(func_70301_a(6).func_77960_j()) });
        }
        return AtomicScience.PACKET_TILE.getPacket(this, new Object[] { Integer.valueOf(this.shiJian), Integer.valueOf(-1), Integer.valueOf(-1), Integer.valueOf(-1) });
    }

    public void func_70295_k_()
    {
        if (!this.field_70331_k.field_72995_K) {
            for (EntityPlayer player : getPlayersUsing()) {
                PacketDispatcher.sendPacketToPlayer(func_70319_e(), (Player)player);
            }
        }
    }

    public boolean canProcess()
    {
        if (func_70301_a(6) != null) {
            if (QuantumAssemblerRecipes.hasItemStack(func_70301_a(6)))
            {
                for (int i = 0; i < 6; i++)
                {
                    if (func_70301_a(i) == null) {
                        return false;
                    }
                    if (func_70301_a(i).field_77993_c != AtomicScience.itemDarkMatter.field_77779_bT) {
                        return false;
                    }
                }
                return func_70301_a(6).field_77994_a < 64;
            }
        }
        return false;
    }

    public void process()
    {
        if (canProcess())
        {
            for (int i = 0; i < 5; i++) {
                if (func_70301_a(i) != null) {
                    func_70298_a(i, 1);
                }
            }
            if (func_70301_a(6) != null) {
                func_70301_a(6).field_77994_a += 1;
            }
        }
    }

    public void func_70307_a(NBTTagCompound par1NBTTagCompound)
    {
        super.func_70307_a(par1NBTTagCompound);
        this.shiJian = par1NBTTagCompound.func_74762_e("smeltingTicks");
    }

    public void func_70310_b(NBTTagCompound par1NBTTagCompound)
    {
        super.func_70310_b(par1NBTTagCompound);
        par1NBTTagCompound.func_74768_a("smeltingTicks", this.shiJian);
    }

    public boolean func_94041_b(int slotID, ItemStack itemStack)
    {
        if (slotID == 6) {
            return true;
        }
        return itemStack.field_77993_c == AtomicScience.itemDarkMatter.field_77779_bT;
    }

    public long getVoltageInput(ForgeDirection from)
    {
        return 1000L;
    }

    public void onWrongVoltage(ForgeDirection direction, long voltage) {}
}
