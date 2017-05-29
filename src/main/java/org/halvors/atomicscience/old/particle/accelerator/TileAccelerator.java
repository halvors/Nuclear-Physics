package org.halvors.atomicscience.old.particle.accelerator;

import atomicscience.AtomicScience;
import atomicscience.Settings;
import atomicscience.particle.fulmination.ItemAntimatter;
import calclavia.api.atomicscience.IElectromagnet;
import calclavia.lib.network.PacketAnnotation;
import calclavia.lib.network.Synced;
import calclavia.lib.prefab.tile.IRotatable;
import calclavia.lib.prefab.tile.TileElectricalInventory;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.electricity.IVoltageInput;
import universalelectricity.api.energy.EnergyStorageHandler;
import universalelectricity.api.energy.IEnergyInterface;
import universalelectricity.api.vector.Vector3;

public class TileAccelerator
        extends TileElectricalInventory
        implements IElectromagnet, IRotatable, IInventory, ISidedInventory, IVoltageInput
{
    public static final int DIAN = 4800000;
    public static final float SU_DU = 0.9F;
    @Synced
    public float yongDianLiang = 0.0F;
    @Synced
    public int antimatter;
    public EntityParticle entityParticle;
    @Synced
    public float velocity;
    @Synced
    private long clientEnergy = 0L;
    private int lastSpawnTick = 0;

    public TileAccelerator()
    {
        this.energy = new EnergyStorageHandler(9600000L, 240000L);
        this.maxSlots = 4;
    }

    public boolean canConnect(ForgeDirection direction, Object obj)
    {
        return obj instanceof IEnergyInterface;
    }

    public long onReceiveEnergy(ForgeDirection from, long receive, boolean doReceive)
    {
        if (doReceive) {
            this.yongDianLiang += (float)receive;
        }
        if ((func_70301_a(0) != null) && ((this.field_70331_k.func_72864_z(this.field_70329_l, this.field_70330_m, this.field_70327_n)) || (this.field_70331_k.func_94577_B(this.field_70329_l, this.field_70330_m, this.field_70327_n) > 0))) {
            return super.onReceiveEnergy(from, receive, doReceive);
        }
        return 0L;
    }

    public void func_70316_g()
    {
        super.func_70316_g();
        if (!this.field_70331_k.field_72995_K)
        {
            this.clientEnergy = this.energy.getEnergy();
            this.velocity = 0.0F;
            if (this.entityParticle != null) {
                this.velocity = ((float)this.entityParticle.getSuDu());
            }
            if (AtomicScience.isItemStackEmptyCell(func_70301_a(1))) {
                if (func_70301_a(1).field_77994_a > 0) {
                    if (this.antimatter >= 125) {
                        if (func_70301_a(2) != null)
                        {
                            if (func_70301_a(2).field_77993_c == AtomicScience.itemAntimatter.field_77779_bT)
                            {
                                ItemStack newStack = func_70301_a(2).func_77946_l();
                                if (newStack.field_77994_a < newStack.func_77976_d())
                                {
                                    func_70298_a(1, 1);
                                    this.antimatter -= 125;
                                    newStack.field_77994_a += 1;
                                    func_70299_a(2, newStack);
                                }
                            }
                        }
                        else
                        {
                            this.antimatter -= 125;
                            func_70298_a(1, 1);
                            func_70299_a(2, new ItemStack(AtomicScience.itemAntimatter));
                        }
                    }
                }
            }
            if (this.field_70331_k.func_72864_z(this.field_70329_l, this.field_70330_m, this.field_70327_n))
            {
                if (this.energy.checkExtract())
                {
                    if (this.entityParticle == null)
                    {
                        if ((func_70301_a(0) != null) && (this.lastSpawnTick >= 40))
                        {
                            Vector3 spawnDiDian = new Vector3(this);
                            spawnDiDian.translate(getDirection().getOpposite());
                            spawnDiDian.translate(0.5D);
                            if (EntityParticle.canCunZai(this.field_70331_k, spawnDiDian))
                            {
                                this.yongDianLiang = 0.0F;
                                this.entityParticle = new EntityParticle(this.field_70331_k, spawnDiDian, new Vector3(this), getDirection().getOpposite());
                                this.field_70331_k.func_72838_d(this.entityParticle);
                                func_70298_a(0, 1);
                                this.lastSpawnTick = 0;
                            }
                        }
                    }
                    else
                    {
                        if (this.entityParticle.field_70128_L)
                        {
                            if (this.entityParticle.didParticleCollision) {
                                if (this.field_70331_k.field_73012_v.nextFloat() <= Settings.darkMatterSpawnChance) {
                                    incrStackSize(3, new ItemStack(AtomicScience.itemDarkMatter));
                                }
                            }
                            this.entityParticle = null;
                        }
                        else if (this.velocity > 0.9F)
                        {
                            this.field_70331_k.func_72908_a(this.field_70329_l, this.field_70330_m, this.field_70327_n, "atomicscience:antimatter", 2.0F, 1.0F - this.field_70331_k.field_73012_v.nextFloat() * 0.3F);
                            this.antimatter += 5 + this.field_70331_k.field_73012_v.nextInt(5);
                            this.yongDianLiang = 0.0F;
                            this.entityParticle.func_70106_y();
                            this.entityParticle = null;
                        }
                        if (this.entityParticle != null) {
                            this.field_70331_k.func_72908_a(this.field_70329_l, this.field_70330_m, this.field_70327_n, "atomicscience:accelerator", 1.5F, (float)(0.6000000238418579D + 0.4D * this.entityParticle.getSuDu() / 0.8999999761581421D));
                        }
                    }
                    this.energy.extractEnergy();
                }
                else
                {
                    if (this.entityParticle != null) {
                        this.entityParticle.func_70106_y();
                    }
                    this.entityParticle = null;
                }
            }
            else
            {
                if (this.entityParticle != null) {
                    this.entityParticle.func_70106_y();
                }
                this.entityParticle = null;
            }
            if (this.ticks % 5L == 0L) {
                for (EntityPlayer player : getPlayersUsing()) {
                    PacketDispatcher.sendPacketToPlayer(func_70319_e(), (Player)player);
                }
            }
            this.lastSpawnTick += 1;
        }
    }

    public Packet func_70319_e()
    {
        return AtomicScience.PACKET_ANNOTATION.getPacket(this);
    }

    public void func_70307_a(NBTTagCompound par1NBTTagCompound)
    {
        super.func_70307_a(par1NBTTagCompound);
        this.yongDianLiang = par1NBTTagCompound.func_74760_g("yongDianLiang");
        this.antimatter = par1NBTTagCompound.func_74762_e("fanWuSu");
    }

    public void func_70310_b(NBTTagCompound par1NBTTagCompound)
    {
        super.func_70310_b(par1NBTTagCompound);
        par1NBTTagCompound.func_74776_a("yongDianLiang", this.yongDianLiang);
        par1NBTTagCompound.func_74768_a("fanWuSu", this.antimatter);
    }

    public long getVoltageInput(ForgeDirection dir)
    {
        return 1000L;
    }

    public int[] func_94128_d(int side)
    {
        return new int[] { 0, 1, 2, 3 };
    }

    public boolean func_102007_a(int slotID, ItemStack itemStack, int j)
    {
        return (func_94041_b(slotID, itemStack)) && (slotID != 2) && (slotID != 3);
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
                return true;
            case 1:
                return AtomicScience.isItemStackEmptyCell(itemStack);
            case 2:
                return itemStack.func_77973_b() instanceof ItemAntimatter;
            case 3:
                return itemStack.func_77973_b() instanceof ItemDarkMatter;
        }
        return false;
    }

    public long onExtractEnergy(ForgeDirection from, long extract, boolean doExtract)
    {
        return 0L;
    }

    public void onWrongVoltage(ForgeDirection direction, long voltage) {}

    public boolean isRunning()
    {
        return true;
    }
}
