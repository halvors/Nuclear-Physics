package org.halvors.nuclearphysics.common.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import org.halvors.nuclearphysics.api.tile.IElectromagnet;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.init.ModPotions;
import org.halvors.nuclearphysics.common.init.ModSoundEvents;
import org.halvors.nuclearphysics.common.tile.particle.TileParticleAccelerator;
import org.halvors.nuclearphysics.common.utility.VectorUtility;

import javax.annotation.Nonnull;
import java.util.List;

public class EntityParticle extends Entity implements IEntityAdditionalSpawnData {
    private static final DataParameter<EnumFacing> movementDirectionParameter = EntityDataManager.createKey(EntityParticle.class, DataSerializers.FACING);

    public Ticket updateTicket;
    private boolean didCollide;
    private int lastTurn = 60;

    private BlockPos movementPos;
    private EnumFacing movementDirection = EnumFacing.NORTH;

    public EntityParticle(final World world) {
        super(world);

        ignoreFrustumCheck = true;

        if (world.isRemote) {
            setRenderDistanceWeight(4);
        }

        setSize(0.3F, 0.3F);
    }

    public EntityParticle(final World world, final BlockPos pos, final BlockPos movementPos, final EnumFacing movementDirection) {
        this(world);

        this.movementPos = movementPos;
        this.movementDirection = movementDirection;

        setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    @Override
    public boolean isInRangeToRenderDist(final double distance) {
        return super.isInRangeToRenderDist(distance);
    }

    /**
     * Checks to see if a new particle can be spawned at the location.
     * @param world - world to check in
     * @param pos - location to check
     * @return true if the spawn location is clear and 2 electromagnets are next to the location
     */
    public static boolean canSpawnParticle(final World world, final BlockPos pos) {
        if (world.isAirBlock(pos)) {
            int electromagnetCount = 0;

            for (EnumFacing side : EnumFacing.values()) {
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
     * @param side - direction to check in
     * @return true if the location contains an active electromagnet block
     */
    public static boolean isElectromagnet(final World world, final BlockPos pos, final EnumFacing side) {
        final BlockPos checkPos = pos.offset(side);
        final TileEntity tile = world.getTileEntity(checkPos);

        return tile instanceof IElectromagnet && ((IElectromagnet) tile).isRunning();
    }

    @Override
    public void writeSpawnData(final ByteBuf data) {
        data.writeInt(movementPos.getX());
        data.writeInt(movementPos.getY());
        data.writeInt(movementPos.getZ());
        data.writeInt(movementDirection.ordinal());
    }

    @Override
    public void readSpawnData(final ByteBuf data) {
        movementPos = new BlockPos(data.readInt(), data.readInt(), data.readInt());
        movementDirection = EnumFacing.getFront(data.readInt());
    }

    @Override
    public void onUpdate() {
        final TileEntity tile = world.getTileEntity(movementPos);

        if (tile instanceof TileParticleAccelerator) {
            final TileParticleAccelerator tileParticleAccelerator = (TileParticleAccelerator) tile;
            double acceleration = 0.0006;

            // Play sound effects.
            if (ticksExisted % 10 == 0) {
                world.playSound(posX, posY, posZ, ModSoundEvents.ANTIMATTER, SoundCategory.BLOCKS, 1, (float) (0.6 + (0.4 * (getVelocity() / TileParticleAccelerator.ANTIMATTER_CREATION_SPEED))), true);
            }

            // Sanity check
            if (tileParticleAccelerator.getEntityParticle() == null) {
                tileParticleAccelerator.setEntityParticle(this);
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
                dataManager.set(movementDirectionParameter, movementDirection);
            } else {
                movementDirection = dataManager.get(movementDirectionParameter);
            }

            final BlockPos pos = getPosition().down();

            if ((!isElectromagnet(world, pos, movementDirection.rotateAround(Axis.Y)) ||
                 !isElectromagnet(world, pos, movementDirection.getOpposite().rotateAround(Axis.Y))) && lastTurn <= 0) {
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

            final BlockPos accelerationPos = VectorUtility.scale(BlockPos.ORIGIN.offset(movementDirection), acceleration);

            motionX = Math.min(accelerationPos.getX() + motionX, TileParticleAccelerator.ANTIMATTER_CREATION_SPEED);
            motionY = Math.min(accelerationPos.getY() + motionY, TileParticleAccelerator.ANTIMATTER_CREATION_SPEED);
            motionZ = Math.min(accelerationPos.getZ() + motionZ, TileParticleAccelerator.ANTIMATTER_CREATION_SPEED);
            isAirBorne = true;

            lastTickPosX = posX;
            lastTickPosY = posY;
            lastTickPosZ = posZ;

            move(motionX, motionY, motionZ);
            setPosition(posX, posY, posZ);

            if (lastTickPosX == posX && lastTickPosY == posY && lastTickPosZ == posZ && getVelocity() <= 0 && lastTurn <= 0) {
                setDead();
            }

            world.spawnParticle(EnumParticleTypes.PORTAL, posX, posY, posZ, 0, 0, 0);
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX, posY, posZ, 0, 0, 0);

            final float radius = 0.5F;

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
        dataManager.register(movementDirectionParameter, EnumFacing.SOUTH);

        if (updateTicket == null) {
            updateTicket = ForgeChunkManager.requestTicket(NuclearPhysics.getInstance(), world, Type.ENTITY);

            if (updateTicket != null) {
                updateTicket.getModData();
                updateTicket.bindEntity(this);
            }
        }
    }

    @Override
    protected void readEntityFromNBT(@Nonnull final NBTTagCompound tag) {
        movementPos = new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
        movementDirection = EnumFacing.getFront(tag.getByte("direction"));
    }

    @Override
    protected void writeEntityToNBT(@Nonnull final NBTTagCompound tag) {
        tag.setInteger("x", movementPos.getX());
        tag.setInteger("y", movementPos.getY());
        tag.setInteger("z", movementPos.getZ());
        tag.setByte("direction", (byte) movementDirection.ordinal());
    }

    @Override
    public void applyEntityCollision(@Nonnull final Entity entity) {
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
        final EnumFacing leftDirection = movementDirection.getOpposite().rotateAround(Axis.Y);
        final EnumFacing rightDirection = movementDirection.rotateAround(Axis.Y);
        final BlockPos pos = getPosition().down();

        if (world.isAirBlock(pos.offset(leftDirection))) {
            movementDirection = leftDirection;
        } else if (world.isAirBlock(pos.offset(rightDirection))) {
            movementDirection = rightDirection;
        } else {
            setDead();

            return 0;
        }

        setPosition(Math.floor(posX) + 0.5, Math.floor(posY) + 0.5, Math.floor(posZ) + 0.5);

        return getVelocity() - (getVelocity() / Math.min(Math.max(70 * getVelocity(), 4), 30));
    }

    private void handleCollisionWithEntity() {
        world.playSound(posX, posY, posZ, ModSoundEvents.ANTIMATTER, SoundCategory.BLOCKS, 1.5F, 1F - world.rand.nextFloat() * 0.3F, true);

        if (!world.isRemote) {
            if (getVelocity() > TileParticleAccelerator.ANTIMATTER_CREATION_SPEED / 2) {
                final float radius = 1;
                final AxisAlignedBB bounds = new AxisAlignedBB(posX - radius, posY - radius, posZ - radius, posX + radius, posY + radius, posZ + radius);
                final List<EntityParticle> entitiesNearby = world.getEntitiesWithinAABB(EntityParticle.class, bounds);

                if (entitiesNearby.size() > 0) {
                    didCollide = true;
                    setDead();

                    return;
                }
            }

            world.createExplosion(this, posX, posY, posZ, (float) getVelocity() * 2.5F, true);
        }

        final float radius = 6;
        final AxisAlignedBB bounds = new AxisAlignedBB(posX - radius, posY - radius, posZ - radius, posX + radius, posY + radius, posZ + radius);
        final List<EntityLivingBase> entitiesNearby = world.getEntitiesWithinAABB(EntityLivingBase.class, bounds);

        for (EntityLivingBase entity : entitiesNearby) {
            ModPotions.poisonRadiation.poisonEntity(entity);
        }

        setDead();
    }

    public double getVelocity() {
        return Math.abs(motionX) + Math.abs(motionY) + Math.abs(motionZ);
    }

    public boolean didCollide() {
        return didCollide;
    }
}
