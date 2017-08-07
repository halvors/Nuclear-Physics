package org.halvors.quantum.atomic.common.utility.transform.vector;

import com.google.common.io.ByteArrayDataInput;
import net.minecraft.nbt.NBTTagCompound;

public class Vector2 implements Cloneable, IVector2 {
    private double x;
    private double y;

    public Vector2()
    {
        this(0.0D, 0.0D);
    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(IVector2 vec) {
        this.x = vec.getX();
        this.y = vec.getY();
    }

    public Vector2(ByteArrayDataInput data)
    {
        this(data.readDouble(), data.readDouble());
    }

    public Vector2(NBTTagCompound nbt)
    {
        this(nbt.getDouble("x"), nbt.getDouble("y"));
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    public int intX()
    {
        return (int)Math.floor(this.x);
    }

    public int intY()
    {
        return (int)Math.floor(this.y);
    }

    public Vector2 clone()
    {
        return new Vector2(this.x, this.y);
    }

    public double getMagnitude()
    {
        return Math.sqrt(getMagnitudeSquared());
    }

    public double getMagnitudeSquared()
    {
        return this.x * this.x + this.y * this.y;
    }

    public Vector2 normalize() {
        double d = getMagnitude();

        if (d != 0.0D) {
            scale(1.0D / d);
        }

        return this;
    }

    public static double distance(Vector2 point1, Vector2 point2)
    {
        return point1.clone().distance(point2);
    }

    public static double slope(Vector2 point1, Vector2 point2) {
        double xDifference = point1.x - point2.x;
        double yDiference = point1.y - point2.y;
        return yDiference / xDifference;
    }

    public Vector2 midPoint(Vector2 pos)
    {
        return new Vector2((this.x + pos.x) / 2.0D, (this.y + pos.y) / 2.0D);
    }

    public double distance(Vector2 target) {
        Vector2 difference = clone().subtract(target);
        return difference.getMagnitude();
    }

    public Vector2 add(Vector2 par1) {
        this.x += par1.x;
        this.y += par1.y;
        return this;
    }

    public Vector2 add(double par1) {
        this.x += par1;
        this.y += par1;
        return this;
    }

    public Vector2 subtract(Vector2 par1) {
        this.x -= par1.x;
        this.y -= par1.y;

        return this;
    }

    public Vector2 invert() {
        scale(-1.0D);

        return this;
    }

    public Vector2 scale(double amount) {
        this.x *= amount;
        this.y *= amount;

        return this;
    }

    @Deprecated
    public Vector2 multiply(double amount)
    {
        return scale(amount);
    }

    public Vector2 round()
    {
        return new Vector2(Math.round(this.x), Math.round(this.y));
    }

    public Vector2 ceil()
    {
        return new Vector2(Math.ceil(this.x), Math.ceil(this.y));
    }

    public Vector2 floor()
    {
        return new Vector2(Math.floor(this.x), Math.floor(this.y));
    }

    public int hashCode() {
        long x = Double.doubleToLongBits(this.x);
        long y = Double.doubleToLongBits(this.y);

        return 31 * (int)(x ^ x >>> 32) + (int)(y ^ y >>> 32);
    }

    public boolean equals(Object o) {
        if ((o instanceof Vector2))
        {
            Vector2 vector = (Vector2)o;
            return (this.x == vector.x) && (this.y == vector.y);
        }
        return false;
    }

    public String toString()
    {
        return "Vector2 [" + this.x + "," + this.y + "]";
    }
}
