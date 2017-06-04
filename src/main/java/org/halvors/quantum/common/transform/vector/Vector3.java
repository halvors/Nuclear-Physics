package org.halvors.quantum.common.transform.vector;

import com.google.common.io.ByteArrayDataInput;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.common.transform.rotation.EulerAngle;
import org.halvors.quantum.common.transform.rotation.Quaternion;

public class Vector3 implements Cloneable, IVector3, Comparable<IVector3> {
    public double x;
    public double y;
    public double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3()
    {
        this(0.0D, 0.0D, 0.0D);
    }

    @Deprecated
    public Vector3(Vector3 vector)
    {
        this(vector.x, vector.y, vector.z);
    }

    public Vector3(IVector3 vector)
    {
        this(vector.x(), vector.y(), vector.z());
    }

    public Vector3(double amount)
    {
        this(amount, amount, amount);
    }

    public Vector3(Entity par1)
    {
        this(par1.posX, par1.posY, par1.posZ);
    }

    public Vector3(TileEntity par1)
    {
        this(par1.xCoord, par1.yCoord, par1.zCoord);
    }

    public Vector3(Vec3 par1)
    {
        this(par1.xCoord, par1.yCoord, par1.zCoord);
    }

    public Vector3(MovingObjectPosition par1)
    {
        this(par1.blockX, par1.blockY, par1.blockZ);
    }

    public Vector3(ChunkCoordinates par1)
    {
        this(par1.posX, par1.posY, par1.posZ);
    }

    public Vector3(ForgeDirection direction)
    {
        this(direction.offsetX, direction.offsetY, direction.offsetZ);
    }

    public Vector3(NBTTagCompound nbt)
    {
        this(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"));
    }

    public Vector3(ByteArrayDataInput data)
    {
        this(data.readDouble(), data.readDouble(), data.readDouble());
    }

    public Vector3(double yaw, double pitch)
    {
        this(new EulerAngle(yaw, pitch));
    }

    public static Vector3 fromCenter(Entity e) {
        return new Vector3(e.posX, e.posY - e.yOffset + e.height / 2.0F, e.posZ);
    }

    public static Vector3 fromCenter(TileEntity e) {
        return new Vector3(e.xCoord + 0.5D, e.yCoord + 0.5D, e.zCoord + 0.5D);
    }

    public double x()
    {
        return this.x;
    }

    public double y()
    {
        return this.y;
    }

    public double z()
    {
        return this.z;
    }

    public int intX()
    {
        return (int)Math.floor(this.x);
    }

    public int intY()
    {
        return (int)Math.floor(this.y);
    }

    public int intZ()
    {
        return (int)Math.floor(this.z);
    }

    public float floatX()
    {
        return (float)this.x;
    }

    public float floatY()
    {
        return (float)this.y;
    }

    public float floatZ()
    {
        return (float)this.z;
    }

    public Vector3 clone()
    {
        return new Vector3(x(), y(), z());
    }

    public Block getBlock(IBlockAccess world)
    {
        return world == null ? null : world.getBlock(intX(), intY(), intZ());
    }

    public int getBlockMetadata(IBlockAccess world)
    {
        return world.getBlockMetadata(intX(), intY(), intZ());
    }

    public TileEntity getTileEntity(IBlockAccess world)
    {
        return world.getTileEntity(intX(), intY(), intZ());
    }

    public boolean setBlock(World world, Block block, int metadata, int notify) {
        return world.setBlock(intX(), intY(), intZ(), block, metadata, notify);
    }

    public boolean setBlock(World world, Block block, int metadata)
    {
        return setBlock(world, block, metadata, 3);
    }

    public boolean setBlock(World world, Block block)
    {
        return setBlock(world, block, 0);
    }

    public Vector2 toVector2()
    {
        return new Vector2(this.x, this.z);
    }

    public Vec3 toVec3()
    {
        return Vec3.createVectorHelper(this.x, this.y, this.z);
    }

    public EulerAngle toAngle() {
        return new EulerAngle(Math.toDegrees(Math.atan2(this.x, this.z)), Math.toDegrees(-Math.atan2(this.y, Math.hypot(this.z, this.x))));
    }

    public EulerAngle toAngle(IVector3 target)
    {
        return clone().difference(target).toAngle();
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setDouble("x", this.x);
        nbt.setDouble("y", this.y);
        nbt.setDouble("z", this.z);

        return nbt;
    }

    public ForgeDirection toForgeDirection() {
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            if (x == direction.offsetX && y == direction.offsetY && z == direction.offsetZ) {
                return direction;
            }
        }

        return ForgeDirection.UNKNOWN;
    }

    public double getMagnitude()
    {
        return Math.sqrt(getMagnitudeSquared());
    }

    public double getMagnitudeSquared()
    {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public Vector3 normalize() {
        double d = getMagnitude();

        if (d != 0.0D) {
            scale(1.0D / d);
        }

        return this;
    }

    public static double distance(Vector3 vec1, IVector3 vec2)
    {
        return vec1.distance(vec2);
    }

    public static double distance(IVector3 vec1, IVector3 vec2)
    {
        return new Vector3(vec1).distance(vec2);
    }

    public double distance(double x, double y, double z) {
        Vector3 difference = clone().difference(x, y, z);

        return difference.getMagnitude();
    }

    public double distance(IVector3 compare)
    {
        return distance(compare.x(), compare.y(), compare.z());
    }

    public double distance(Entity entity)
    {
        return distance(entity.posX, entity.posY, entity.posZ);
    }

    public Vector3 invert() {
        scale(-1.0D);

        return this;
    }

    public Vector3 translate(ForgeDirection side, double amount)
    {
        return translate(new Vector3(side).scale(amount));
    }

    public Vector3 translate(ForgeDirection side)
    {
        return translate(side, 1.0D);
    }

    @Deprecated
    public Vector3 modifyPositionFromSide(ForgeDirection side, double amount)
    {
        return translate(side, amount);
    }

    @Deprecated
    public Vector3 modifyPositionFromSide(ForgeDirection side)
    {
        return translate(side);
    }

    public Vector3 translate(IVector3 addition) {
        this.x += addition.x();
        this.y += addition.y();
        this.z += addition.z();

        return this;
    }

    public Vector3 translate(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;

        return this;
    }

    public Vector3 translate(double addition) {
        this.x += addition;
        this.y += addition;
        this.z += addition;

        return this;
    }

    public static Vector3 translate(Vector3 first, IVector3 second)
    {
        return first.clone().translate(second);
    }

    public static Vector3 translate(Vector3 translate, double addition) {
        return translate.clone().translate(addition);
    }

    public Vector3 add(IVector3 amount)
    {
        return translate(amount);
    }

    public Vector3 add(double amount)
    {
        return translate(amount);
    }

    public Vector3 subtract(IVector3 amount)
    {
        return translate(-amount.x(), -amount.y(), -amount.z());
    }

    public Vector3 subtract(double amount)
    {
        return translate(-amount);
    }

    public Vector3 subtract(double x, double y, double z)
    {
        return difference(x, y, z);
    }

    public Vector3 difference(IVector3 amount)
    {
        return translate(-amount.x(), -amount.y(), -amount.z());
    }

    public Vector3 difference(double amount)
    {
        return translate(-amount);
    }

    public Vector3 difference(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;

        return this;
    }

    public Vector3 scale(double amount) {
        this.x *= amount;
        this.y *= amount;
        this.z *= amount;

        return this;
    }

    public Vector3 scale(double x, double y, double z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;

        return this;
    }

    public Vector3 scale(Vector3 amount) {
        this.x *= amount.x;
        this.y *= amount.y;
        this.z *= amount.z;

        return this;
    }

    public static Vector3 scale(Vector3 vec, double amount)
    {
        return vec.scale(amount);
    }

    public static Vector3 scale(Vector3 vec, Vector3 amount)
    {
        return vec.scale(amount);
    }

    public Vector3 max(Vector3 other) {
        return new Vector3(Math.max(this.x, other.x), Math.max(this.y, other.y), Math.max(this.z, other.z));
    }

    public Vector3 min(Vector3 other) {
        return new Vector3(Math.min(this.x, other.x), Math.min(this.y, other.y), Math.min(this.z, other.z));
    }

    public Vector3 round()
    {
        return new Vector3(Math.round(this.x), Math.round(this.y), Math.round(this.z));
    }

    public Vector3 ceil()
    {
        return new Vector3(Math.ceil(this.x), Math.ceil(this.y), Math.ceil(this.z));
    }

    public Vector3 floor()
    {
        return new Vector3(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
    }

    public Vector3 toRound() {
        this.x = Math.round(this.x);
        this.y = Math.round(this.y);
        this.z = Math.round(this.z);

        return this;
    }

    public Vector3 toCeil() {
        this.x = Math.ceil(this.x);
        this.y = Math.ceil(this.y);
        this.z = Math.ceil(this.z);

        return this;
    }

    public Vector3 toFloor() {
        this.x = Math.floor(this.x);
        this.y = Math.floor(this.y);
        this.z = Math.floor(this.z);

        return this;
    }

    public List<Entity> getEntitiesWithin(World worldObj, Class<? extends Entity> par1Class) {
        return worldObj.getEntitiesWithinAABB(par1Class, AxisAlignedBB.getBoundingBox(intX(), intY(), intZ(), intX() + 1, intY() + 1, intZ() + 1));
    }

    public Vector3 midPoint(Vector3 pos) {
        return new Vector3((this.x + pos.x) / 2.0D, (this.y + pos.y) / 2.0D, (this.z + pos.z) / 2.0D);
    }

    public Vector3 toCrossProduct(Vector3 compare) {
        double newX = this.y * compare.z - this.z * compare.y;
        double newY = this.z * compare.x - this.x * compare.z;
        double newZ = this.x * compare.y - this.y * compare.x;
        this.x = newX;
        this.y = newY;
        this.z = newZ;

        return this;
    }

    public Vector3 crossProduct(Vector3 compare)
    {
        return clone().toCrossProduct(compare);
    }

    public Vector3 xCrossProduct()
    {
        return new Vector3(0.0D, this.z, -this.y);
    }

    public Vector3 zCrossProduct()
    {
        return new Vector3(-this.y, this.x, 0.0D);
    }

    public double dotProduct(Vector3 vec2)
    {
        return this.x * vec2.x + this.y * vec2.y + this.z * vec2.z;
    }

    public Vector3 getPerpendicular() {
        if (this.z == 0.0D) {
            return zCrossProduct();
        }

        return xCrossProduct();
    }

    public boolean isZero()
    {
        return equals(ZERO());
    }

    public Vector3 rotate(float angle, Vector3 axis)
    {
        return translateMatrix(getRotationMatrix(angle, axis), this);
    }

    public double[] getRotationMatrix(float angle) {
        double[] matrix = new double[16];
        Vector3 axis = clone().normalize();
        double x = axis.x;
        double y = axis.y;
        double z = axis.z;
        angle = (float)(angle * 0.0174532925D);
        float cos = (float)Math.cos(angle);
        float ocos = 1.0F - cos;
        float sin = (float)Math.sin(angle);
        matrix[0] = (x * x * ocos + cos);
        matrix[1] = (y * x * ocos + z * sin);
        matrix[2] = (x * z * ocos - y * sin);
        matrix[4] = (x * y * ocos - z * sin);
        matrix[5] = (y * y * ocos + cos);
        matrix[6] = (y * z * ocos + x * sin);
        matrix[8] = (x * z * ocos + y * sin);
        matrix[9] = (y * z * ocos - x * sin);
        matrix[10] = (z * z * ocos + cos);
        matrix[15] = 1.0D;

        return matrix;
    }

    public static Vector3 translateMatrix(double[] matrix, Vector3 translation) {
        double x = translation.x * matrix[0] + translation.y * matrix[1] + translation.z * matrix[2] + matrix[3];
        double y = translation.x * matrix[4] + translation.y * matrix[5] + translation.z * matrix[6] + matrix[7];
        double z = translation.x * matrix[8] + translation.y * matrix[9] + translation.z * matrix[10] + matrix[11];
        translation.x = x;
        translation.y = y;
        translation.z = z;

        return translation;
    }

    public static double[] getRotationMatrix(float angle, Vector3 axis)
    {
        return axis.getRotationMatrix(angle);
    }

    @Deprecated
    public void rotate(double yaw, double pitch, double roll)
    {
        rotate(new EulerAngle(yaw, roll));
    }

    public void rotate(EulerAngle angle) {
        double yawRadians = angle.yawRadians();
        double pitchRadians = angle.pitchRadians();
        double rollRadians = angle.rollRadians();

        double x = this.x;
        double y = this.y;
        double z = this.z;

        this.x = (x * Math.cos(yawRadians) * Math.cos(pitchRadians) + z * (Math.cos(yawRadians) * Math.sin(pitchRadians) * Math.sin(rollRadians) - Math.sin(yawRadians) * Math.cos(rollRadians)) + y * (Math.cos(yawRadians) * Math.sin(pitchRadians) * Math.cos(rollRadians) + Math.sin(yawRadians) * Math.sin(rollRadians)));
        this.z = (x * Math.sin(yawRadians) * Math.cos(pitchRadians) + z * (Math.sin(yawRadians) * Math.sin(pitchRadians) * Math.sin(rollRadians) + Math.cos(yawRadians) * Math.cos(rollRadians)) + y * (Math.sin(yawRadians) * Math.sin(pitchRadians) * Math.cos(rollRadians) - Math.cos(yawRadians) * Math.sin(rollRadians)));
        this.y = (-x * Math.sin(pitchRadians) + z * Math.cos(pitchRadians) * Math.sin(rollRadians) + y * Math.cos(pitchRadians) * Math.cos(rollRadians));
    }

    public void rotate(double yaw, double pitch)
    {
        rotate(new EulerAngle(yaw, pitch));
    }

    public void rotate(double yaw) {
        double yawRadians = Math.toRadians(yaw);

        double x = this.x;
        double z = this.z;

        if (yaw != 0.0D) {
            this.x = (x * Math.cos(yawRadians) - z * Math.sin(yawRadians));
            this.z = (x * Math.sin(yawRadians) + z * Math.cos(yawRadians));
        }
    }

    public Vector3 rotate(Quaternion rotator) {
        rotator.rotate(this);
        return this;
    }

    @Deprecated
    public static Vector3 getDeltaPositionFromRotation(float rotationYaw, float rotationPitch) {
        return new Vector3(rotationYaw, rotationPitch);
    }

    public double getAngle(Vector3 vec2)
    {
        return anglePreNorm(clone().normalize(), vec2.clone().normalize());
    }

    public static double getAngle(Vector3 vec1, Vector3 vec2)
    {
        return vec1.getAngle(vec2);
    }

    public double anglePreNorm(Vector3 vec2)
    {
        return Math.acos(dotProduct(vec2));
    }

    public static double anglePreNorm(Vector3 vec1, Vector3 vec2)
    {
        return Math.acos(vec1.clone().dotProduct(vec2));
    }

    public static Vector3 UP()
    {
        return new Vector3(0.0D, 1.0D, 0.0D);
    }

    public static Vector3 DOWN()
    {
        return new Vector3(0.0D, -1.0D, 0.0D);
    }

    public static Vector3 NORTH()
    {
        return new Vector3(0.0D, 0.0D, -1.0D);
    }

    public static Vector3 SOUTH()
    {
        return new Vector3(0.0D, 0.0D, 1.0D);
    }

    public static Vector3 WEST()
    {
        return new Vector3(-1.0D, 0.0D, 0.0D);
    }

    public static Vector3 EAST()
    {
        return new Vector3(1.0D, 0.0D, 0.0D);
    }

    public static Vector3 ZERO()
    {
        return new Vector3(0.0D, 0.0D, 0.0D);
    }

    public static Vector3 CENTER()
    {
        return new Vector3(0.5D, 0.5D, 0.5D);
    }

    public MovingObjectPosition rayTrace(World world, float rotationYaw, float rotationPitch, double reachDistance) {
        Vector3 lookVector = getDeltaPositionFromRotation(rotationYaw, rotationPitch);
        Vector3 reachPoint = clone().translate(lookVector.clone().scale(reachDistance));

        return rayTrace(world, reachPoint);
    }

    public MovingObjectPosition rayTrace(World world, Vector3 reachPoint) {
        MovingObjectPosition pickedBlock = rayTraceBlocks(world, reachPoint.clone());
        MovingObjectPosition pickedEntity = rayTraceEntities(world, reachPoint.clone());

        if (pickedBlock == null) {
            return pickedEntity;
        }

        if (pickedEntity == null) {
            return pickedBlock;
        }

        double dBlock = distance(new Vector3(pickedBlock.hitVec));
        double dEntity = distance(new Vector3(pickedEntity.hitVec));

        if (dEntity < dBlock) {
            return pickedEntity;
        }

        return pickedBlock;
    }

    public MovingObjectPosition rayTraceBlocks(World world, Vector3 end) {
        return world.rayTraceBlocks(toVec3(), end.toVec3());
    }

    @Deprecated
    public MovingObjectPosition rayTraceEntities(World world, float rotationYaw, float rotationPitch, boolean collisionFlag, double reachDistance) {
        return rayTraceEntities(world, rotationYaw, rotationPitch, reachDistance);
    }

    public MovingObjectPosition rayTraceEntities(World world, float rotationYaw, float rotationPitch, double reachDistance) {
        return rayTraceEntities(world, getDeltaPositionFromRotation(rotationYaw, rotationPitch).scale(reachDistance));
    }

    public MovingObjectPosition rayTraceEntities(World world, Vector3 target) {
        MovingObjectPosition pickedEntity = null;
        Vec3 startingPosition = toVec3();
        Vec3 look = target.toVec3();
        double reachDistance = distance(target);

        double checkBorder = 1.1D * reachDistance;

        AxisAlignedBB boxToScan = AxisAlignedBB.getBoundingBox(-checkBorder, -checkBorder, -checkBorder, checkBorder, checkBorder, checkBorder).offset(this.x, this.y, this.z);

        List<Entity> entitiesInBounds = world.getEntitiesWithinAABB(null, boxToScan);
        double closestEntity = reachDistance;

        if ((entitiesInBounds == null) || (entitiesInBounds.isEmpty())) {
            return null;
        }

        for (Entity possibleHits : entitiesInBounds) {
            if ((possibleHits != null) && (possibleHits.canBeCollidedWith()) && (possibleHits.boundingBox != null)) {
                float border = possibleHits.getCollisionBorderSize();
                AxisAlignedBB aabb = possibleHits.boundingBox.expand(border, border, border);
                MovingObjectPosition hitMOP = aabb.calculateIntercept(startingPosition, target.toVec3());

                if (hitMOP != null) {
                    if (aabb.isVecInside(startingPosition)) {
                        if ((0.0D < closestEntity) || (closestEntity == 0.0D)) {
                            pickedEntity = new MovingObjectPosition(possibleHits);

                            if (pickedEntity != null) {
                                pickedEntity.hitVec = hitMOP.hitVec;
                                closestEntity = 0.0D;
                            }
                        }
                    } else {
                        double distance = startingPosition.distanceTo(hitMOP.hitVec);

                        if ((distance < closestEntity) || (closestEntity == 0.0D)) {
                            pickedEntity = new MovingObjectPosition(possibleHits);
                            pickedEntity.hitVec = hitMOP.hitVec;
                            closestEntity = distance;
                        }
                    }
                }
            }
        }

        return pickedEntity;
    }

    public MovingObjectPosition rayTraceEntities(World world, Entity target) {
        return rayTraceEntities(world, new Vector3(target));
    }

    public int hashCode() {
        long x = Double.doubleToLongBits(this.x);
        long y = Double.doubleToLongBits(this.y);
        long z = Double.doubleToLongBits(this.z);
        int hash = (int)(x ^ x >>> 32);
        hash = 31 * hash + (int)(y ^ y >>> 32);
        hash = 31 * hash + (int)(z ^ z >>> 32);

        return hash;
    }

    public boolean equals(Object o) {
        if ((o instanceof Vector3)) {
            Vector3 vector3 = (Vector3) o;

            return (this.x == vector3.x) && (this.y == vector3.y) && (this.z == vector3.z);
        }

        return false;
    }

    public String toString()
    {
        return "Vector3 [" + this.x + "," + this.y + "," + this.z + "]";
    }

    public int compareTo(IVector3 o)
    {
        return (int)(ZERO().distance(this) - ZERO().distance(o));
    }
}
