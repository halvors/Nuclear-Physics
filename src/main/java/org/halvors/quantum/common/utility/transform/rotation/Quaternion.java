package org.halvors.quantum.common.utility.transform.rotation;

import org.halvors.quantum.common.utility.transform.vector.Vector3;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Quaternion implements Cloneable {
    public double x;
    public double y;
    public double z;
    public double s;
    public static final double SQRT2 = Math.sqrt(2.0D);

    public Quaternion() {
        this.s = 1.0D;
        this.x = 0.0D;
        this.y = 0.0D;
        this.z = 0.0D;
    }

    public Quaternion(Quaternion Quaternion) {
        this.x = Quaternion.x;
        this.y = Quaternion.y;
        this.z = Quaternion.z;
        this.s = Quaternion.s;
    }

    public Quaternion(double d, double d1, double d2, double d3) {
        this.x = d1;
        this.y = d2;
        this.z = d3;
        this.s = d;
    }

    public Quaternion set(Quaternion Quaternion) {
        this.x = Quaternion.x;
        this.y = Quaternion.y;
        this.z = Quaternion.z;
        this.s = Quaternion.s;

        return this;
    }

    public Quaternion set(double d, double d1, double d2, double d3) {
        this.x = d1;
        this.y = d2;
        this.z = d3;
        this.s = d;

        return this;
    }

    public static Quaternion aroundAxis(double ax, double ay, double az, double angle) {
        return new Quaternion().setAroundAxis(ax, ay, az, angle);
    }

    public static Quaternion aroundAxis(Vector3 axis, double angle) {
        return aroundAxis(axis.x, axis.y, axis.z, angle);
    }

    public Quaternion setAroundAxis(double ax, double ay, double az, double angle) {
        angle *= 0.5D;
        double d4 = Math.sin(angle);
        return set(Math.cos(angle), ax * d4, ay * d4, az * d4);
    }

    public Quaternion setAroundAxis(Vector3 axis, double angle)
    {
        return setAroundAxis(axis.x, axis.y, axis.z, angle);
    }

    public Quaternion multiply(Quaternion Quaternion) {
        double d = this.s * Quaternion.s - this.x * Quaternion.x - this.y * Quaternion.y - this.z * Quaternion.z;
        double d1 = this.s * Quaternion.x + this.x * Quaternion.s - this.y * Quaternion.z + this.z * Quaternion.y;
        double d2 = this.s * Quaternion.y + this.x * Quaternion.z + this.y * Quaternion.s - this.z * Quaternion.x;
        double d3 = this.s * Quaternion.z - this.x * Quaternion.y + this.y * Quaternion.x + this.z * Quaternion.s;
        this.s = d;
        this.x = d1;
        this.y = d2;
        this.z = d3;

        return this;
    }

    public Quaternion rightMultiply(Quaternion Quaternion) {
        double d = this.s * Quaternion.s - this.x * Quaternion.x - this.y * Quaternion.y - this.z * Quaternion.z;
        double d1 = this.s * Quaternion.x + this.x * Quaternion.s + this.y * Quaternion.z - this.z * Quaternion.y;
        double d2 = this.s * Quaternion.y - this.x * Quaternion.z + this.y * Quaternion.s + this.z * Quaternion.x;
        double d3 = this.s * Quaternion.z + this.x * Quaternion.y - this.y * Quaternion.x + this.z * Quaternion.s;
        this.s = d;
        this.x = d1;
        this.y = d2;
        this.z = d3;

        return this;
    }

    public double mag()
    {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.s * this.s);
    }

    public Quaternion normalize() {
        double d = mag();

        if (d != 0.0D) {
            d = 1.0D / d;
            this.x *= d;
            this.y *= d;
            this.z *= d;
            this.s *= d;
        }

        return this;
    }

    public Quaternion copy()
    {
        return new Quaternion(this);
    }

    public void rotate(Vector3 vec) {
        double d = -this.x * vec.x - this.y * vec.y - this.z * vec.z;
        double d1 = this.s * vec.x + this.y * vec.z - this.z * vec.y;
        double d2 = this.s * vec.y - this.x * vec.z + this.z * vec.x;
        double d3 = this.s * vec.z + this.x * vec.y - this.y * vec.x;
        vec.x = (d1 * this.s - d * this.x - d2 * this.z + d3 * this.y);
        vec.y = (d2 * this.s - d * this.y + d1 * this.z - d3 * this.x);
        vec.z = (d3 * this.s - d * this.z - d1 * this.y + d2 * this.x);
    }

    public String toString() {
        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "Quaternion[" + new BigDecimal(this.s, cont) + ", " + new BigDecimal(this.x, cont) + ", " + new BigDecimal(this.y, cont) + ", " + new BigDecimal(this.z, cont) + "]";
    }

    public Rotation rotation()
    {
        return new Rotation(this);
    }
}
