package org.halvors.quantum.common.entity.particle;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.block.IElectromagnet;
import org.halvors.quantum.common.effect.poison.PoisonRadiation;
import org.halvors.quantum.common.tile.particle.TileAccelerator;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.common.transform.vector.VectorHelper;

import java.util.List;

public class EntityParticle extends Entity implements IEntityAdditionalSpawnData {
    private static final int MOVE_TICK_RATE = 20;
    private int lastTurn = 60;
    private Vector3 movementPosition = new Vector3();
    private ForgeDirection movementDirection = ForgeDirection.NORTH;
    public ForgeChunkManager.Ticket updateTicket;
    public boolean didParticleCollide = false;

    public EntityParticle(World world) {
        super(world);

        setSize(0.3F, 0.3F);
        renderDistanceWeight = 4F;
        ignoreFrustumCheck = true;
    }

    public EntityParticle(World world, Vector3 position, Vector3 movementPosition, ForgeDirection movementDirection) {
        this(world);

        this.movementPosition = movementPosition;
        this.movementDirection = movementDirection;

        setPosition(position.x, position.y, position.z);
    }

    public static boolean canRenderAcceleratedParticle(World world, Vector3 pos) {
        if (pos.getBlock(world) != Blocks.air) {
            return false;
        }

        for (int i = 0; i <= 1; i++) {
            ForgeDirection dir = ForgeDirection.getOrientation(i);

            if (!isElectromagnet(world, pos, dir)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isElectromagnet(World world, Vector3 position, ForgeDirection dir) {
        Vector3 checkPos = position.clone().translate(dir);
        TileEntity tile = checkPos.getTileEntity(world);

        return tile instanceof IElectromagnet && ((IElectromagnet) tile).isRunning();
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(movementPosition.intX());
        buffer.writeInt(movementPosition.intY());
        buffer.writeInt(movementPosition.intZ());
        buffer.writeInt(movementDirection.ordinal());
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        movementPosition.x = additionalData.readInt();
        movementPosition.y = additionalData.readInt();
        movementPosition.z = additionalData.readInt();
        movementDirection = ForgeDirection.getOrientation(additionalData.readInt());
    }

    @Override
    protected void entityInit() {
        dataWatcher.addObject(MOVE_TICK_RATE, (byte) 3);

        if (updateTicket == null) {
            updateTicket = ForgeChunkManager.requestTicket(Quantum.getInstance(), worldObj, ForgeChunkManager.Type.ENTITY);
            updateTicket.getModData();
            updateTicket.bindEntity(this);
        }
    }

    @Override
    public void onUpdate() {
        /** Play sound fxs. */
        if (ticksExisted % 10 == 0) {
            worldObj.playSoundAtEntity(this, Reference.PREFIX + "accelerator", 1f, (float) (0.6f + (0.4 * (getParticleVelocity() / TileAccelerator.clientParticleVelocity))));
        }

        /** Check if the accelerator tile entity exists. */
        TileEntity tile = worldObj.getTileEntity(movementPosition.intX(), movementPosition.intY(), movementPosition.intZ());

        if (!(tile instanceof TileAccelerator)) {
            setDead();
            
            return;
        }

        TileAccelerator tileAccelerator = (TileAccelerator) tile;

        if (tileAccelerator.entityParticle == null) {
            tileAccelerator.entityParticle = this;
        }

        for (int x = -1; x < 1; x++) {
            for (int z = -1; z < 1; z++) {
                ForgeChunkManager.forceChunk(updateTicket, new ChunkCoordIntPair(((int) posX >> 4) + x, ((int) posZ >> 4) + z));
            }
        }

        try {
            if (!worldObj.isRemote) {
                dataWatcher.updateObject(MOVE_TICK_RATE, (byte) movementDirection.ordinal());
            } else {
                movementDirection = ForgeDirection.getOrientation(dataWatcher.getWatchableObjectByte(MOVE_TICK_RATE));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        double acceleration = 0.0006F;

        if (!isElectromagnet(worldObj, new Vector3(this), movementDirection.getRotation(ForgeDirection.UP)) || !isElectromagnet(worldObj, new Vector3(this), movementDirection.getRotation(ForgeDirection.DOWN)) && lastTurn <= 0) {
            acceleration = turn();
            motionX = 0;
            motionY = 0;
            motionZ = 0;
            lastTurn = 40;
        }

        lastTurn--;

        /** Checks if the current block condition allows the particle to exist */
        if (!canRenderAcceleratedParticle(worldObj, new Vector3(this)) || isCollided) {
            explode();
            
            return;
        }

        Vector3 dongLi = new Vector3();
        dongLi.translate(movementDirection);
        dongLi.scale(acceleration);
        motionX = Math.min(dongLi.x + motionX, TileAccelerator.clientParticleVelocity);
        motionY = Math.min(dongLi.y + motionY, TileAccelerator.clientParticleVelocity);
        motionZ = Math.min(dongLi.z + motionZ, TileAccelerator.clientParticleVelocity);
        isAirBorne = true;

        lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;

        moveEntity(motionX, motionY, motionZ);
        setPosition(posX, posY, posZ);

        if (lastTickPosX == posX && lastTickPosY == posY && lastTickPosZ == posZ && getParticleVelocity() <= 0 && lastTurn <= 0) {
            setDead();
        }

        worldObj.spawnParticle("portal", posX, posY, posZ, 0, 0, 0);
        worldObj.spawnParticle("largesmoke", posX, posY, posZ, 0, 0, 0);

        float radius = 0.5F;

        AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(posX - radius, posY - radius, posZ - radius, posX + radius, posY + radius, posZ + radius);
        List<Entity> entitiesNearby = worldObj.getEntitiesWithinAABB(Entity.class, bounds);

        if (entitiesNearby.size() > 1) {
            explode();
        }
    }

    /** Try to move the particle left or right depending on which side is empty.
     *
     * @return The new velocity. */
    private double turn() {
        ForgeDirection zuoFangXiang = VectorHelper.getOrientationFromSide(movementDirection, ForgeDirection.EAST);
        Vector3 zuoBian = new Vector3(this).floor();
        zuoBian.translate(zuoFangXiang);

        ForgeDirection youFangXiang = VectorHelper.getOrientationFromSide(movementDirection, ForgeDirection.WEST);
        Vector3 youBian = new Vector3(this).floor();
        youBian.translate(youFangXiang);

        if (zuoBian.getBlock(worldObj) == Blocks.air) {
            movementDirection = zuoFangXiang;
        } else if (youBian.getBlock(worldObj) == Blocks.air) {
            movementDirection = youFangXiang;
        } else {
            setDead();

            return 0;
        }

        setPosition(Math.floor(posX) + 0.5, Math.floor(posY) + 0.5, Math.floor(posZ) + 0.5);

        return getParticleVelocity() - (getParticleVelocity() / Math.min(Math.max(70 * getParticleVelocity(), 4), 30));

    }

    public void explode() {
        worldObj.playSoundAtEntity(this, Reference.PREFIX + "antimatter", 1.5f, 1f - worldObj.rand.nextFloat() * 0.3f);

        if (!worldObj.isRemote) {
            if (getParticleVelocity() > TileAccelerator.clientParticleVelocity / 2) {
                /* Check for nearby particles and if colliding with another one, drop strange matter. */
                float radius = 1F;

                AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(posX - radius, posY - radius, posZ - radius, posX + radius, posY + radius, posZ + radius);
                List<EntityParticle> entitiesNearby = worldObj.getEntitiesWithinAABB(EntityParticle.class, bounds);

                if (entitiesNearby.size() > 0) {
                    didParticleCollide = true;
                    setDead();

                    return;
                }
            }

            worldObj.createExplosion(this, posX, posY, posZ, (float) getParticleVelocity() * 2.5f, true);
        }

        float radius = 6;

        AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(posX - radius, posY - radius, posZ - radius, posX + radius, posY + radius, posZ + radius);
        List<EntityLiving> livingNearby = worldObj.getEntitiesWithinAABB(EntityLiving.class, bounds);

        for (EntityLiving entity : livingNearby) {
            PoisonRadiation.INSTANCE.poisonEntity(new Vector3(entity), entity);
        }

        setDead();
    }

    public double getParticleVelocity() {
        return Math.abs(motionX) + Math.abs(motionY) + Math.abs(motionZ);
    }

    @Override
    public void applyEntityCollision(Entity par1Entity)
    {
        explode();
    }


    @Override
    public void setDead() {
        ForgeChunkManager.releaseTicket(updateTicket);
        super.setDead();
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        movementPosition = new Vector3(nbt.getCompoundTag("position"));
        ForgeDirection.getOrientation(nbt.getByte("direction"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setTag("position", movementPosition.writeToNBT(new NBTTagCompound()));
        nbt.setByte("direction", (byte) movementDirection.ordinal());
    }
}
