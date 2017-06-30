package org.halvors.quantum.common.utility.transform.vector;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.List;

public class VectorWorld extends Vector3 implements IVectorWorld {
    public World world;

    public VectorWorld(World world, double x, double y, double z) {
        super(x, y, z);
        this.world = world;
    }

    public VectorWorld(IVectorWorld vectorWorld) {
        this(vectorWorld.getWorld(), vectorWorld.getX(), vectorWorld.getY(), vectorWorld.getZ());
    }

    public VectorWorld(NBTTagCompound nbt) {
        super(nbt);

        world = DimensionManager.getWorld(nbt.getInteger("d"));
    }

    public VectorWorld(Entity entity) {
        super(entity);

        world = entity.getEntityWorld();
    }

    public VectorWorld(TileEntity tile) {
        super(tile);

        world = tile.getWorld();
    }

    public VectorWorld(World world, IVector3 v) {
        x = v.getX();
        y = v.getY();
        z = v.getZ();
        this.world = world;
    }

    public VectorWorld(World world, MovingObjectPosition pos) {
        this.x = pos.blockX;
        this.y = pos.blockY;
        this.z = pos.blockZ;
        this.world = world;
    }

    @Override
    public World getWorld() {
        return world;
    }

    public VectorWorld translate(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;

        return this;
    }

    public VectorWorld clone() {
        return new VectorWorld(this.world, this.x, this.y, this.z);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("d", this.world.provider.getDimension());

        return nbt;
    }

    public TileEntity getTileEntity() {
        if (this.world != null) {
            return super.getTileEntity(this.world);
        }

        return null;
    }

    public Block getBlock() {
        return super.getBlock(world);
    }

    public boolean setBlock(Block block, int metadata, int notify) {
        return super.setBlock(this.world, block, metadata, notify);
    }

    public boolean setBlock(Block block, int metadata)
    {
        return setBlock(block, metadata, 3);
    }

    public boolean setBlock(Block block)
    {
        return setBlock(block, 0);
    }

    public List<Entity> getEntitiesWithin(Class<? extends Entity> par1Class) {
        return super.getEntitiesWithin(this.world, par1Class);
    }

    public static VectorWorld fromCenter(Entity e) {
        return new VectorWorld(e.world, e.posX, e.posY - e.getYOffset() + e.height / 2.0F, e.posZ);
    }

    public static VectorWorld fromCenter(TileEntity e) {
        return new VectorWorld(e.getWorld(), e.xCoord + 0.5D, e.yCoord + 0.5D, e.zCoord + 0.5D);
    }

    public MovingObjectPosition rayTraceEntities(VectorWorld target) {
        return super.rayTraceEntities(target.getWorld(), target);
    }

    public MovingObjectPosition rayTraceEntities(Entity target)
    {
        return super.rayTraceEntities(this.world, target);
    }

    public MovingObjectPosition rayTraceEntities(Vector3 target)
    {
        return super.rayTraceEntities(this.world, target);
    }

    public boolean equals(Object o) {
        if ((o instanceof VectorWorld)) {
            return (super.equals(o)) && (this.world == ((VectorWorld)o).world);
        }

        return super.equals(o);
    }

    public String toString() {
        return "VectorWorld [" + this.x + "," + this.y + "," + this.z + "," + this.world + "]";
    }
}
