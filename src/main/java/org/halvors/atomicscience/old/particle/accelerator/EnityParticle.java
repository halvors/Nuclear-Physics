package org.halvors.atomicscience.old.particle.accelerator;

import atomicscience.AtomicScience;
import calclavia.api.atomicscience.IElectromagnet;
import calclavia.lib.prefab.poison.Poison;
import calclavia.lib.prefab.poison.PoisonRadiation;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorHelper;

public class EntityParticle
        extends Entity
        implements IEntityAdditionalSpawnData
{
    private static final int FANG_XIANG_DATA = 20;
    public ForgeChunkManager.Ticket youPiao;
    public boolean didParticleCollision = false;
    private int lastTurn = 60;
    private Vector3 jiQi = new Vector3();
    private ForgeDirection fangXiang = ForgeDirection.NORTH;

    public EntityParticle(World par1World)
    {
        super(par1World);
        func_70105_a(0.3F, 0.3F);
        this.field_70155_l = 4.0D;
        this.field_70158_ak = true;
    }

    public EntityParticle(World par1World, Vector3 diDian, Vector3 jiQi, ForgeDirection direction)
    {
        this(par1World);
        func_70107_b(diDian.x, diDian.y, diDian.z);
        this.jiQi = jiQi;
        this.fangXiang = direction;
    }

    public static boolean canCunZai(World worldObj, Vector3 position)
    {
        if (position.getBlockID(worldObj) != 0) {
            return false;
        }
        for (int i = 0; i <= 1; i++)
        {
            ForgeDirection dir = ForgeDirection.getOrientation(i);
            if (!isElectromagnet(worldObj, position, dir)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isElectromagnet(World world, Vector3 position, ForgeDirection dir)
    {
        Vector3 checkPos = position.clone().translate(dir);
        TileEntity tile = checkPos.getTileEntity(world);
        if ((tile instanceof IElectromagnet)) {
            return ((IElectromagnet)tile).isRunning();
        }
        return false;
    }

    public void writeSpawnData(ByteArrayDataOutput data)
    {
        data.writeInt(this.jiQi.intX());
        data.writeInt(this.jiQi.intY());
        data.writeInt(this.jiQi.intZ());
        data.writeInt(this.fangXiang.ordinal());
    }

    public void readSpawnData(ByteArrayDataInput data)
    {
        this.jiQi.x = data.readInt();
        this.jiQi.y = data.readInt();
        this.jiQi.z = data.readInt();
        this.fangXiang = ForgeDirection.getOrientation(data.readInt());
    }

    protected void func_70088_a()
    {
        this.field_70180_af.func_75682_a(20, Byte.valueOf((byte)3));
        if (this.youPiao == null)
        {
            this.youPiao = ForgeChunkManager.requestTicket(AtomicScience.instance, this.field_70170_p, ForgeChunkManager.Type.ENTITY);
            this.youPiao.getModData();
            this.youPiao.bindEntity(this);
        }
    }

    public void func_70071_h_()
    {
        if (this.field_70173_aa % 10 == 0) {
            this.field_70170_p.func_72956_a(this, "atomicscience:accelerator", 1.0F, (float)(0.6000000238418579D + 0.4D * (getSuDu() / 0.8999999761581421D)));
        }
        TileEntity t = this.field_70170_p.func_72796_p(this.jiQi.intX(), this.jiQi.intY(), this.jiQi.intZ());
        if (!(t instanceof TileAccelerator))
        {
            func_70106_y();
            return;
        }
        TileAccelerator tileEntity = (TileAccelerator)t;
        if (tileEntity.entityParticle == null) {
            tileEntity.entityParticle = this;
        }
        for (int x = -1; x < 1; x++) {
            for (int z = -1; z < 1; z++) {
                ForgeChunkManager.forceChunk(this.youPiao, new ChunkCoordIntPair(((int)this.field_70165_t >> 4) + x, ((int)this.field_70161_v >> 4) + z));
            }
        }
        try
        {
            if (!this.field_70170_p.field_72995_K) {
                this.field_70180_af.func_75692_b(20, Byte.valueOf((byte)this.fangXiang.ordinal()));
            } else {
                this.fangXiang = ForgeDirection.getOrientation(this.field_70180_af.func_75683_a(20));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        double acceleration = 6.000000284984708E-4D;
        if (((!isElectromagnet(this.field_70170_p, new Vector3(this), this.fangXiang.getRotation(ForgeDirection.UP))) || (!isElectromagnet(this.field_70170_p, new Vector3(this), this.fangXiang.getRotation(ForgeDirection.DOWN)))) && (this.lastTurn <= 0))
        {
            acceleration = turn();
            this.field_70159_w = 0.0D;
            this.field_70181_x = 0.0D;
            this.field_70179_y = 0.0D;
            this.lastTurn = 40;
        }
        this.lastTurn -= 1;
        if ((!canCunZai(this.field_70170_p, new Vector3(this))) || (this.field_70132_H))
        {
            explode();
            return;
        }
        Vector3 dongLi = new Vector3();
        dongLi.translate(this.fangXiang);
        dongLi.scale(acceleration);
        this.field_70159_w = Math.min(dongLi.x + this.field_70159_w, 0.8999999761581421D);
        this.field_70181_x = Math.min(dongLi.y + this.field_70181_x, 0.8999999761581421D);
        this.field_70179_y = Math.min(dongLi.z + this.field_70179_y, 0.8999999761581421D);
        this.field_70160_al = true;

        this.field_70142_S = this.field_70165_t;
        this.field_70137_T = this.field_70163_u;
        this.field_70136_U = this.field_70161_v;

        func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);

        func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
        if ((this.field_70142_S == this.field_70165_t) && (this.field_70137_T == this.field_70163_u) && (this.field_70136_U == this.field_70161_v) && (getSuDu() <= 0.0D) && (this.lastTurn <= 0)) {
            func_70106_y();
        }
        this.field_70170_p.func_72869_a("portal", this.field_70165_t, this.field_70163_u, this.field_70161_v, 0.0D, 0.0D, 0.0D);
        this.field_70170_p.func_72869_a("largesmoke", this.field_70165_t, this.field_70163_u, this.field_70161_v, 0.0D, 0.0D, 0.0D);

        float radius = 0.5F;

        AxisAlignedBB bounds = AxisAlignedBB.func_72330_a(this.field_70165_t - radius, this.field_70163_u - radius, this.field_70161_v - radius, this.field_70165_t + radius, this.field_70163_u + radius, this.field_70161_v + radius);
        List<Entity> entitiesNearby = this.field_70170_p.func_72872_a(Entity.class, bounds);
        if (entitiesNearby.size() > 1)
        {
            explode();
            return;
        }
    }

    private double turn()
    {
        ForgeDirection zuoFangXiang = VectorHelper.getOrientationFromSide(this.fangXiang, ForgeDirection.EAST);
        Vector3 zuoBian = new Vector3(this).floor();
        zuoBian.translate(zuoFangXiang);

        ForgeDirection youFangXiang = VectorHelper.getOrientationFromSide(this.fangXiang, ForgeDirection.WEST);
        Vector3 youBian = new Vector3(this).floor();
        youBian.translate(youFangXiang);
        if (zuoBian.getBlockID(this.field_70170_p) == 0)
        {
            this.fangXiang = zuoFangXiang;
        }
        else if (youBian.getBlockID(this.field_70170_p) == 0)
        {
            this.fangXiang = youFangXiang;
        }
        else
        {
            func_70106_y();
            return 0.0D;
        }
        func_70107_b(Math.floor(this.field_70165_t) + 0.5D, Math.floor(this.field_70163_u) + 0.5D, Math.floor(this.field_70161_v) + 0.5D);

        return getSuDu() - getSuDu() / Math.min(Math.max(70.0D * getSuDu(), 4.0D), 30.0D);
    }

    public void explode()
    {
        this.field_70170_p.func_72956_a(this, "atomicscience:antimatter", 1.5F, 1.0F - this.field_70170_p.field_73012_v.nextFloat() * 0.3F);
        if (!this.field_70170_p.field_72995_K)
        {
            if (getSuDu() > 0.44999998807907104D)
            {
                float radius = 1.0F;

                AxisAlignedBB bounds = AxisAlignedBB.func_72330_a(this.field_70165_t - radius, this.field_70163_u - radius, this.field_70161_v - radius, this.field_70165_t + radius, this.field_70163_u + radius, this.field_70161_v + radius);
                List<EntityParticle> entitiesNearby = this.field_70170_p.func_72872_a(EntityParticle.class, bounds);
                if (entitiesNearby.size() > 0)
                {
                    this.didParticleCollision = true;
                    func_70106_y();
                    return;
                }
            }
            this.field_70170_p.func_72876_a(this, this.field_70165_t, this.field_70163_u, this.field_70161_v, (float)getSuDu() * 2.5F, true);
        }
        float radius = 6.0F;

        AxisAlignedBB bounds = AxisAlignedBB.func_72330_a(this.field_70165_t - radius, this.field_70163_u - radius, this.field_70161_v - radius, this.field_70165_t + radius, this.field_70163_u + radius, this.field_70161_v + radius);
        List<EntityLiving> livingNearby = this.field_70170_p.func_72872_a(EntityLiving.class, bounds);
        for (EntityLiving entity : livingNearby) {
            PoisonRadiation.INSTANCE.poisonEntity(new Vector3(entity), entity);
        }
        func_70106_y();
    }

    public double getSuDu()
    {
        return Math.abs(this.field_70159_w) + Math.abs(this.field_70181_x) + Math.abs(this.field_70179_y);
    }

    public void func_70108_f(Entity par1Entity)
    {
        explode();
    }

    public void func_70106_y()
    {
        ForgeChunkManager.releaseTicket(this.youPiao);
        super.func_70106_y();
    }

    protected void func_70037_a(NBTTagCompound nbt)
    {
        this.jiQi = new Vector3(nbt.func_74775_l("jiqi"));
        ForgeDirection.getOrientation(nbt.func_74771_c("fangXiang"));
    }

    protected void func_70014_b(NBTTagCompound nbt)
    {
        nbt.func_74782_a("jiqi", this.jiQi.writeToNBT(new NBTTagCompound()));
        nbt.func_74774_a("fangXiang", (byte)this.fangXiang.ordinal());
    }
}
