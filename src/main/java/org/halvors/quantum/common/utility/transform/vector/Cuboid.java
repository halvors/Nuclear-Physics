package org.halvors.quantum.common.utility.transform.vector;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.halvors.quantum.common.utility.transform.rotation.EulerAngle;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Cuboid {
    public Vector3 min;
    public Vector3 max;

    public Cuboid() {
        this(new Vector3(), new Vector3());
    }

    public Cuboid(Cuboid cuboid) {
        this(cuboid.min.clone(), cuboid.max.clone());
    }

    public Cuboid(Vector3 min, Vector3 max) {
        this.min = min.clone();
        this.max = max.clone();
    }

    public Cuboid(double minx, double miny, double minz, double maxx, double maxy, double maxz) {
        this.min = new Vector3(minx, miny, minz);
        this.max = new Vector3(maxx, maxy, maxz);
    }

    public Cuboid(AxisAlignedBB aabb) {
        this(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
    }

    public AxisAlignedBB toAABB() {
        return new AxisAlignedBB(this.min.x, this.min.y, this.min.z, this.max.x, this.max.y, this.max.z);
    }

    public static Cuboid full() {
        return new Cuboid(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    }

    public Rectangle toRectangle() {
        return new Rectangle(this.min.toVector2(), this.max.toVector2());
    }

    public Cuboid add(Vector3 vec) {
        this.min.add(vec);
        this.max.add(vec);
        return this;
    }

    public Cuboid subtract(Vector3 vec) {
        this.min.subtract(vec);
        this.max.subtract(vec);
        return this;
    }

    public Cuboid rotate(EulerAngle angle) {
        this.min.rotate(angle);
        this.max.rotate(angle);

        return this;
    }

    public boolean intersects(Vector3 point) {
        return (point.x > this.min.x) && (point.x < this.max.x) && (point.y > this.min.y) && (point.y < this.max.y) && (point.z > this.min.z) && (point.z < this.max.z);
    }

    public boolean intersects(Cuboid other) {
        return (this.max.x - 1.0E-5D > other.min.x) && (other.max.x - 1.0E-5D > this.min.x) && (this.max.y - 1.0E-5D > other.min.y) && (other.max.y - 1.0E-5D > this.min.y) && (this.max.z - 1.0E-5D > other.min.z) && (other.max.z - 1.0E-5D > this.min.z);
    }

    public Cuboid offset(Cuboid o) {
        this.min.translate(o.min);
        this.max.translate(o.max);
        return this;
    }

    public Cuboid translate(Vector3 vec) {
        this.min.translate(vec);
        this.max.translate(vec);
        return this;
    }

    public Vector3 center() {
        return this.min.clone().add(this.max).scale(0.5D);
    }

    public Cuboid expand(Vector3 difference) {
        this.min.subtract(difference);
        this.max.add(difference);
        return this;
    }

    public Cuboid expand(double difference) {
        this.min.subtract(difference);
        this.max.add(difference);
        return this;
    }

    public List<Vector3> getVectors() {
        List<Vector3> vectors = new ArrayList();

        for (int x = this.min.intX(); x < this.max.intX(); x++) {
            for (int y = this.min.intY(); x < this.max.intY(); y++) {
                for (int z = this.min.intZ(); x < this.max.intZ(); z++) {
                    vectors.add(new Vector3(x, y, z));
                }
            }
        }

        return vectors;
    }

    public List<Vector3> getVectors(Vector3 center, int radius) {
        List<Vector3> vectors = new ArrayList();

        for (int x = this.min.intX(); x < this.max.intX(); x++) {
            for (int y = this.min.intY(); x < this.max.intY(); y++) {
                for (int z = this.min.intZ(); x < this.max.intZ(); z++) {
                    Vector3 vector3 = new Vector3(x, y, z);

                    if (center.distance(vector3) <= radius) {
                        vectors.add(vector3);
                    }
                }
            }
        }

        return vectors;
    }

    public List<Entity> getEntities(World world, Class<? extends Entity> entityClass) {
        return world.getEntitiesWithinAABB(entityClass, toAABB());
    }

    public List<Entity> getEntitiesExlude(World world, Entity entity) {
        return world.getEntitiesWithinAABBExcludingEntity(entity, toAABB());
    }

    public List<Entity> getEntities(World world) {
        return getEntities(world, Entity.class);
    }

    public String toString() {
        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);

        return "Cuboid: [" + new BigDecimal(this.min.x, cont) + ", " + new BigDecimal(this.min.y, cont) + ", " + new BigDecimal(this.min.z, cont) + "] -> [" + new BigDecimal(this.max.x, cont) + ", " + new BigDecimal(this.max.y, cont) + ", " + new BigDecimal(this.max.z, cont) + "]";
    }

    public boolean equals(Object o) {
        if (o instanceof Cuboid) {
            return (this.min.equals(((Cuboid)o).min)) && (this.max.equals(((Cuboid)o).max));
        }

        return false;
    }

    public Cuboid clone() {
        return new Cuboid(this);
    }
}
