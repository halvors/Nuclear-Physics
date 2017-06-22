package org.halvors.quantum.common.entity.particle;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
import org.halvors.quantum.common.block.reactor.fusion.IElectromagnet;
import org.halvors.quantum.common.effect.poison.PoisonRadiation;
import org.halvors.quantum.common.tile.particle.TileAccelerator;
import org.halvors.quantum.common.transform.vector.Vector3;

import java.util.List;

public class EntityParticle extends Entity implements IEntityAdditionalSpawnData {
    // Speed by which a particle will turn into anitmatter.
    public static final float antimatterCreationSpeed = 0.9F;
    private static final int movementDirectionDataWatcherId = 20;

    public ForgeChunkManager.Ticket updateTicket;
    public boolean didParticleCollide = false;
    private int lastTurn = 60;
    private Vector3 movementVector = new Vector3();
    private ForgeDirection movementDirection = ForgeDirection.NORTH;

    public EntityParticle(World world) {
        super(world);

        renderDistanceWeight = 4F;
        ignoreFrustumCheck = true;

        setSize(0.3F, 0.3F);
    }

    public EntityParticle(World world, Vector3 position, Vector3 movementVector, ForgeDirection movementDirection) {
        this(world);

        this.movementVector = movementVector;
        this.movementDirection = movementDirection;

        setPosition(position.x, position.y, position.z);
    }

    /**
     * Checks to see if a new particle can be spawned at the location.
     * @param world - world to check in
     * @param position - location to check
     * @return true if the spawn location is clear and 2 electromagnets are next to the location
     */
    public static boolean canSpawnParticle(World world, Vector3 position) {
        Block block = position.getBlock(world);

        if (block == null || block.isAir(world, position.intX(), position.intY(), position.intZ())) {
            int electromagnetCount = 0;

            for (int side = 0; side <= 6; side++) {
                ForgeDirection direction = ForgeDirection.getOrientation(side);

                if (isElectromagnet(world, position, direction)) {
                    electromagnetCount++;
                }
            }

            return electromagnetCount >= 2;
        }

        return false;
    }

    /**
     * Checks to see if the block is an instance of IElectromagnet and is turned on
     * @param world - world to check in
     * @param position - position to look for the block/tile
     * @param direction - direction to check in
     * @return true if the location contains an active electromagnet block
     */
    public static boolean isElectromagnet(World world, Vector3 position, ForgeDirection direction) {
        Vector3 checkPos = position.clone().translate(direction);
        TileEntity tile = checkPos.getTileEntity(world);

        return tile instanceof IElectromagnet && ((IElectromagnet) tile).isRunning();
    }

    @Override
    public void writeSpawnData(ByteBuf data) {
        data.writeInt(movementVector.intX());
        data.writeInt(movementVector.intY());
        data.writeInt(movementVector.intZ());
        data.writeInt(movementDirection.ordinal());
    }

    @Override
    public void readSpawnData(ByteBuf data) {
        movementVector = new Vector3(data.readInt(), data.readInt(), data.readInt());
        movementDirection = ForgeDirection.getOrientation(data.readInt());
    }

    //////////////////////////////////

    @Override
    public void onUpdate() {
        TileEntity tile = worldObj.getTileEntity(movementVector.intX(), movementVector.intY(), movementVector.intZ());

        if (tile != null && tile instanceof TileAccelerator) {
            TileAccelerator tileAccelerator = (TileAccelerator) tile;
            double acceleration = 0.0009;

            // Play sound effects.
            if (ticksExisted % 10 == 0) {
                worldObj.playSoundAtEntity(this, Reference.PREFIX + "tile.accelerator", 1, (float) (0.6 + (0.4 * (getParticleVelocity() / TileAccelerator.clientParticleVelocity))));
            }

            // Sanity check
            if (tileAccelerator.entityParticle == null) {
                tileAccelerator.entityParticle = this;
            }

            //Force load chunks.
            // TODO: Calculate direction so to only load two chunks instead of 5.
            for (int x = -1; x < 1; x++) {
                for (int z = -1; z < 1; z++) {
                    ForgeChunkManager.forceChunk(updateTicket, new ChunkCoordIntPair(((int) posX >> 4) + x, ((int) posZ >> 4) + z));
                }
            }

            // Update data watcher.
            if (!worldObj.isRemote) {
                dataWatcher.updateObject(movementDirectionDataWatcherId, (byte) movementDirection.ordinal());
            } else {
                movementDirection = ForgeDirection.getOrientation(dataWatcher.getWatchableObjectByte(movementDirectionDataWatcherId));
            }

            // TODO: Double check this? Should it be the old one instead?
            //if (!isElectromagnet(worldObj, new Vector3(this), movementDirection.getRotation(ForgeDirection.UP)) || !isElectromagnet(worldObj, new Vector3(this), movementDirection.getRotation(ForgeDirection.DOWN)) && lastTurn <= 0) {

            if ((!isElectromagnet(worldObj, new Vector3(this), movementDirection.getRotation(ForgeDirection.UP)) || !isElectromagnet(worldObj, new Vector3(this), movementDirection.getRotation(ForgeDirection.DOWN))) && lastTurn <= 0) {
                acceleration = turn();
                motionX = 0;
                motionY = 0;
                motionZ = 0;
                lastTurn = 40;
            }

            lastTurn--;

            // Checks if the current block condition allows the particle to exist.
            if (!canSpawnParticle(worldObj, new Vector3(this)) || isCollided) {
                handleCollisionWithEntity();

                return;
            }

            Vector3 dongLi = new Vector3();
            dongLi.translate(movementDirection);
            dongLi.scale(acceleration);
            motionX = Math.min(dongLi.x + motionX, antimatterCreationSpeed);
            motionY = Math.min(dongLi.y + motionY, antimatterCreationSpeed);
            motionZ = Math.min(dongLi.z + motionZ, antimatterCreationSpeed);
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
                handleCollisionWithEntity();
            }
        } else {
            setDead();
        }
    }

    @Override
    protected void entityInit() {
        dataWatcher.addObject(movementDirectionDataWatcherId, (byte) 3);

        if (updateTicket == null) {
            updateTicket = ForgeChunkManager.requestTicket(Quantum.getInstance(), worldObj, ForgeChunkManager.Type.ENTITY);
            updateTicket.getModData();
            updateTicket.bindEntity(this);
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        movementVector = new Vector3(nbt.getCompoundTag("vector"));
        ForgeDirection.getOrientation(nbt.getByte("direction"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setTag("vector", movementVector.writeToNBT(new NBTTagCompound()));
        nbt.setByte("direction", (byte) movementDirection.ordinal());
    }

    @Override
    public void applyEntityCollision(Entity entity) {
        handleCollisionWithEntity();
    }

    @Override
    public void setDead() {
        ForgeChunkManager.releaseTicket(updateTicket);

        super.setDead();
    }

    /**
     * Try to move the particle left or right depending on which side is empty.
     *
     * @return The new velocity.
     */
    private double turn() {
        // TODO: Rewrite to allow for up and down turning
        int[][] RELATIVE_MATRIX = new int[][] { new int[] { 3, 2, 1, 0, 5, 4 }, new int[] { 4, 5, 0, 1, 2, 3}, new int[] { 0, 1, 3, 2, 4, 5 }, new int[] { 0, 1, 2, 3, 5, 4}, new int[] { 0, 1, 5, 4, 3, 2 }, new int[] { 0, 1, 4, 5, 2, 3 } };
        ForgeDirection zuoFangXiang = ForgeDirection.getOrientation(RELATIVE_MATRIX[movementDirection.ordinal()][ForgeDirection.EAST.ordinal()]);
        Vector3 zuoBian = new Vector3(this).floor();
        zuoBian.translate(zuoFangXiang);
        ForgeDirection youFangXiang = ForgeDirection.getOrientation(RELATIVE_MATRIX[movementDirection.ordinal()][ForgeDirection.WEST.ordinal()]);
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

    private void handleCollisionWithEntity() {
        worldObj.playSoundAtEntity(this, Reference.PREFIX + "tile.antimatter", 1.5F, 1F - worldObj.rand.nextFloat() * 0.3F);

        if (!worldObj.isRemote) {
            if (getParticleVelocity() > antimatterCreationSpeed / 2) {
                float radius = 1F;
                AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(posX - radius, posY - radius, posZ - radius, posX + radius, posY + radius, posZ + radius);
                List<EntityParticle> entitiesNearby = worldObj.getEntitiesWithinAABB(EntityParticle.class, bounds);

                if (entitiesNearby.size() > 0) {
                    didParticleCollide = true;
                    setDead();

                    return;
                }
            }

            worldObj.createExplosion(this, posX, posY, posZ, (float) getParticleVelocity() * 2.5F, true);
        }

        float radius = 6;
        AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(posX - radius, posY - radius, posZ - radius, posX + radius, posY + radius, posZ + radius);
        List<EntityLivingBase> entitiesNearby = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, bounds);

        for (EntityLivingBase entity : entitiesNearby) {
            PoisonRadiation.INSTANCE.poisonEntity(new Vector3(entity), entity);
        }

        setDead();
    }

    public double getParticleVelocity() {
        return Math.abs(motionX) + Math.abs(motionY) + Math.abs(motionZ);
    }
}
