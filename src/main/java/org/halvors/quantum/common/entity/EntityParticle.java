package org.halvors.quantum.common.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import org.halvors.quantum.api.tile.IElectromagnet;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.block.machine.BlockMachine;
import org.halvors.quantum.common.effect.poison.PoisonRadiation;
import org.halvors.quantum.common.tile.particle.TileAccelerator;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

import java.util.List;

public class EntityParticle extends Entity implements IEntityAdditionalSpawnData {
    // Speed by which a particle will turn into anitmatter.
    private static final float antimatterCreationSpeed = 0.9F;
    private static final DataParameter<EnumFacing> movementDirectionDataManager = EntityDataManager.createKey(EntityParticle.class, DataSerializers.FACING);

    public ForgeChunkManager.Ticket updateTicket;
    public boolean didParticleCollide = false;
    private int lastTurn = 60;
    private Vector3 movementVector = new Vector3();
    //private EnumFacing movementDirection = EnumFacing.NORTH;

    private BlockPos movementPos;
    private EnumFacing movementDirection;

    public EntityParticle(World world) {
        super(world);

        ignoreFrustumCheck = true;

        setRenderDistanceWeight(4F);
        setSize(0.3F, 0.3F);
    }

    public EntityParticle(World world, Vector3 position, Vector3 movementVector, EnumFacing movementDirection) {
        this(world);

        this.movementVector = movementVector;
        this.movementDirection = movementDirection;

        setPosition(position.x, position.y, position.z);
    }

    /**
     * Checks to see if a new particle can be spawned at the location.
     * @param world - world to check in
     * @param pos - location to check
     * @return true if the spawn location is clear and 2 electromagnets are next to the location
     */
    public static boolean canSpawnParticle(World world, BlockPos pos) {
        if (world.isAirBlock(pos)) {
            int electromagnetCount = 0;

            for (EnumFacing side : EnumFacing.VALUES) {
                if (isElectromagnet(world, pos, side)) {
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
     * @param pos - position to look for the block/tile
     * @param direction - direction to check in
     * @return true if the location contains an active electromagnet block
     */
    public static boolean isElectromagnet(World world, BlockPos pos, EnumFacing direction) {
        Vector3 vector = new Vector3(pos.getX(), pos.getY(), pos.getZ()).clone().translate(direction);
        BlockPos checkPos = new BlockPos(vector.getX(), vector.getY(), vector.getZ());

        //BlockPos checkPos = pos.offset(direction);
        TileEntity tile = world.getTileEntity(checkPos);

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
        movementDirection = EnumFacing.getFront(data.readInt());
    }

    @Override
    public void onUpdate() {
        TileEntity tile = world.getTileEntity(new BlockPos(movementVector.intX(), movementVector.intY(), movementVector.intZ()));

        if (tile instanceof TileAccelerator) {
            TileAccelerator tileAccelerator = (TileAccelerator) tile;
            double acceleration = 0.0006;

            // Play sound effects.
            if (ticksExisted % 10 == 0) {
                //world.playSoundAtEntity(this, Reference.PREFIX + "tile.accelerator", 1, (float) (0.6 + (0.4 * (getParticleVelocity() / TileAccelerator.clientParticleVelocity))));
            }

            // Sanity check
            if (tileAccelerator.entityParticle == null) {
                tileAccelerator.entityParticle = this;
            }

            // Force load chunks.
            // TODO: Calculate direction so to only load two chunks instead of 5.
            for (int x = -1; x < 1; x++) {
                for (int z = -1; z < 1; z++) {
                    ForgeChunkManager.forceChunk(updateTicket, new ChunkPos(((int) posX >> 4) + x, ((int) posZ >> 4) + z));
                }
            }

            // Update data watcher.
            if (!world.isRemote) {
                dataManager.set(movementDirectionDataManager, movementDirection);
            } else {
                movementDirection = dataManager.get(movementDirectionDataManager);
            }

            BlockPos pos = new BlockPos(posX, posY, posZ);

            if ((!isElectromagnet(world, pos, movementDirection.rotateAround(EnumFacing.UP.getAxis())) || !isElectromagnet(world, pos, movementDirection.rotateAround(EnumFacing.DOWN.getAxis()))) && lastTurn <= 0) {
                acceleration = turn();
                motionX = 0;
                motionY = 0;
                motionZ = 0;
                lastTurn = 40;
            }

            lastTurn--;

            // Checks if the current block condition allows the particle to exist.
            if (!canSpawnParticle(world, pos) || isCollided) {
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

            move(motionX, motionY, motionZ);
            setPosition(posX, posY, posZ);

            if (lastTickPosX == posX && lastTickPosY == posY && lastTickPosZ == posZ && getParticleVelocity() <= 0 && lastTurn <= 0) {
                setDead();
            }

            world.spawnParticle(EnumParticleTypes.PORTAL, posX, posY, posZ, 0, 0, 0);
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX, posY, posZ, 0, 0, 0);

            float radius = 0.5F;

            AxisAlignedBB bounds = new AxisAlignedBB(posX - radius, posY - radius, posZ - radius, posX + radius, posY + radius, posZ + radius);
            List<Entity> entitiesNearby = world.getEntitiesWithinAABB(Entity.class, bounds);

            if (entitiesNearby.size() > 1) {
                applyEntityCollision(this);
            }
        } else {
            setDead();
        }
    }

    @Override
    protected void entityInit() {
        dataManager.register(movementDirectionDataManager, EnumFacing.NORTH);

        if (updateTicket == null) {
            updateTicket = ForgeChunkManager.requestTicket(Quantum.getInstance(), world, ForgeChunkManager.Type.ENTITY);
            updateTicket.getModData();
            updateTicket.bindEntity(this);
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag) {
        movementVector = new Vector3(tag.getCompoundTag("vector"));
        movementDirection = EnumFacing.getFront(tag.getByte("direction"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag) {
        tag.setTag("vector", movementVector.writeToNBT(new NBTTagCompound()));
        tag.setByte("direction", (byte) movementDirection.ordinal());
    }

    @Override
    public void applyEntityCollision(Entity entity) {
        handleCollisionWithEntity();
    }

    @Override
    public void setDead() {
        super.setDead();

        ForgeChunkManager.releaseTicket(updateTicket);
    }

    /**
     * Try to move the particle left or right depending on which side is empty.
     *
     * @return The new velocity.
     */
    private double turn() {
        // TODO: Rewrite to allow for up and down turning
        int[][] RELATIVE_MATRIX = new int[][] { new int[] { 3, 2, 1, 0, 5, 4 }, new int[] { 4, 5, 0, 1, 2, 3}, new int[] { 0, 1, 3, 2, 4, 5 }, new int[] { 0, 1, 2, 3, 5, 4}, new int[] { 0, 1, 5, 4, 3, 2 }, new int[] { 0, 1, 4, 5, 2, 3 } };
        EnumFacing zuoFangXiang = EnumFacing.getFront(RELATIVE_MATRIX[movementDirection.ordinal()][EnumFacing.EAST.ordinal()]);
        Vector3 zuoBian = new Vector3(this).floor();
        zuoBian.translate(zuoFangXiang);
        EnumFacing youFangXiang = EnumFacing.getFront(RELATIVE_MATRIX[movementDirection.ordinal()][EnumFacing.WEST.ordinal()]);
        Vector3 youBian = new Vector3(this).floor();
        youBian.translate(youFangXiang);

        if (zuoBian.getBlock(world) == Blocks.AIR) {
            movementDirection = zuoFangXiang;
        } else if (youBian.getBlock(world) == Blocks.AIR) {
            movementDirection = youFangXiang;
        } else {
            setDead();

            return 0;
        }

        setPosition(Math.floor(posX) + 0.5, Math.floor(posY) + 0.5, Math.floor(posZ) + 0.5);

        return getParticleVelocity() - (getParticleVelocity() / Math.min(Math.max(70 * getParticleVelocity(), 4), 30));
    }

    private void handleCollisionWithEntity() {
        //world.playSoundAtEntity(this, Reference.PREFIX + "tile.antimatter", 1.5F, 1F - world.rand.nextFloat() * 0.3F);

        if (!world.isRemote) {
            if (getParticleVelocity() > antimatterCreationSpeed / 2) {
                float radius = 1F;
                AxisAlignedBB bounds = new AxisAlignedBB(posX - radius, posY - radius, posZ - radius, posX + radius, posY + radius, posZ + radius);
                List<EntityParticle> entitiesNearby = world.getEntitiesWithinAABB(EntityParticle.class, bounds);

                if (entitiesNearby.size() > 0) {
                    didParticleCollide = true;
                    setDead();

                    return;
                }
            }

            world.createExplosion(this, posX, posY, posZ, (float) getParticleVelocity() * 2.5F, true);
        }

        float radius = 6;
        AxisAlignedBB bounds = new AxisAlignedBB(posX - radius, posY - radius, posZ - radius, posX + radius, posY + radius, posZ + radius);
        List<EntityLivingBase> entitiesNearby = world.getEntitiesWithinAABB(EntityLivingBase.class, bounds);

        for (EntityLivingBase entity : entitiesNearby) {
            PoisonRadiation.getInstance().poisonEntity(entity.getPosition(), entity);
        }

        setDead();
    }

    public double getParticleVelocity() {
        return Math.abs(motionX) + Math.abs(motionY) + Math.abs(motionZ);
    }
}
