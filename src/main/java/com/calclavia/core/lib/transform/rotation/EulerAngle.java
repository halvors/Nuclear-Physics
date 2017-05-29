package com.calclavia.core.lib.transform.rotation;

import com.calclavia.core.lib.transform.vector.IVector3;
import net.minecraftforge.common.util.ForgeDirection;

public class EulerAngle
        implements IRotation, IVector3
{
    public double yaw;
    public double pitch;
    public double roll;

    public EulerAngle()
    {
        this(0.0D, 0.0D, 0.0D);
    }

    public EulerAngle(ForgeDirection dir)
    {
        switch (dir)
        {
            case DOWN:
                this.pitch = -90.0D;
                break;
            case UP:
                this.pitch = 90.0D;
                break;
            case NORTH:
                this.yaw = 0.0D;
                break;
            case SOUTH:
                this.yaw = 180.0D;
                break;
            case WEST:
                this.yaw = 90.0D;
                break;
            case EAST:
                this.yaw = -90.0D;
                break;
        }
    }

    public EulerAngle(double yaw, double pitch, double roll)
    {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }

    public EulerAngle(double[] angles)
    {
        this(angles[0], angles[1], angles[2]);
    }

    public EulerAngle(EulerAngle angle)
    {
        this(angle.yaw(), angle.pitch(), angle.roll());
    }

    public EulerAngle(double yaw, double pitch)
    {
        this(yaw, pitch, 0.0D);
    }

    public double yaw()
    {
        return this.yaw;
    }

    public double pitch()
    {
        return this.pitch;
    }

    public double roll()
    {
        return this.roll;
    }

    public double yawRadians()
    {
        return Math.toRadians(yaw());
    }

    public double pitchRadians()
    {
        return Math.toRadians(pitch());
    }

    public double rollRadians()
    {
        return Math.toRadians(roll());
    }

    public double x()
    {
        return -Math.sin(yawRadians()) * Math.cos(pitchRadians());
    }

    public double y()
    {
        return Math.sin(pitchRadians());
    }

    public double z()
    {
        return -Math.cos(yawRadians()) * Math.cos(pitchRadians());
    }

    public double[] toArray()
    {
        return new double[] { yaw(), pitch(), roll() };
    }

    public double[] toRadianArray()
    {
        return new double[] { yawRadians(), pitchRadians(), rollRadians() };
    }

    public EulerAngle set(int i, double value)
    {
        switch (i)
        {
            case 0:
                this.yaw = value;
                break;
            case 1:
                this.pitch = value;
                break;
            case 2:
                this.roll = value;
        }
        return this;
    }

    public EulerAngle difference(EulerAngle other)
    {
        return new EulerAngle(yaw() - other.yaw(), pitch() - other.pitch(), roll() - other.roll());
    }

    public EulerAngle absoluteDifference(EulerAngle other)
    {
        return new EulerAngle(getAngleDifference(yaw(), other.yaw()), getAngleDifference(pitch(), other.pitch()), getAngleDifference(roll(), other.roll()));
    }

    public boolean isWithin(EulerAngle other, double margin)
    {
        for (int i = 0; i < 3; i++) {
            if (absoluteDifference(other).toArray()[i] > margin) {
                return false;
            }
        }
        return true;
    }

    public static double getAngleDifference(double angleA, double angleB)
    {
        return Math.abs(angleA - angleB);
    }

    public EulerAngle clone()
    {
        return new EulerAngle(yaw(), pitch(), roll());
    }

    public static double clampAngleTo360(double var)
    {
        return clampAngle(var, -360.0D, 360.0D);
    }

    public static double clampAngleTo180(double var)
    {
        return clampAngle(var, -180.0D, 180.0D);
    }

    public static double clampAngle(double var, double min, double max)
    {
        while (var < min) {
            var += 360.0D;
        }
        while (var > max) {
            var -= 360.0D;
        }
        return var;
    }

    public int hashCode()
    {
        long x = Double.doubleToLongBits(yaw());
        long y = Double.doubleToLongBits(pitch());
        long z = Double.doubleToLongBits(roll());
        int hash = (int)(x ^ x >>> 32);
        hash = 31 * hash + (int)(y ^ y >>> 32);
        hash = 31 * hash + (int)(z ^ z >>> 32);
        return hash;
    }

    public boolean equals(Object o)
    {
        if ((o instanceof EulerAngle))
        {
            EulerAngle angle = (EulerAngle)o;
            return (yaw() == angle.yaw()) && (pitch() == angle.pitch()) && (roll() == angle.roll());
        }
        return false;
    }

    public String toString()
    {
        return "Angle [" + yaw() + "," + pitch() + "," + roll() + "]";
    }
}
