package org.halvors.quantum.common.utility.transform.rotation;

import org.halvors.quantum.common.utility.transform.vector.Vector3;

public class Rotation {
    public double angle;
    public Vector3 axis;
    private Quaternion quat;

    public Rotation(double angle, Vector3 axis) {
        this.angle = angle;
        this.axis = axis;
    }

    public Rotation(double angle, double x, double y, double z)
    {
        this(angle, new Vector3(x, y, z));
    }

    public Rotation(Quaternion quat) {
        this.quat = quat;
        this.angle = (Math.acos(quat.s) * 2.0D);

        if (this.angle == 0.0D) {
            this.axis = new Vector3(0.0D, 1.0D, 0.0D);
        } else {
            double sa = Math.sin(this.angle * 0.5D);
            this.axis = new Vector3(quat.x / sa, quat.y / sa, quat.z / sa);
        }
    }

    public void apply(Vector3 vec) {
        if (quat == null) {
            quat = Quaternion.aroundAxis(this.axis, this.angle);
        }

        vec.rotate(quat);
    }

    public void applyN(Vector3 normal)
    {
        apply(normal);
    }
}
