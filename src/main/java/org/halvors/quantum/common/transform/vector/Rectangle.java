package org.halvors.quantum.common.transform.vector;

public class Rectangle {
    public Vector2 min;
    public Vector2 max;

    public Rectangle() {
        this(new Vector2(), new Vector2());
    }

    public Rectangle(Vector2 min, Vector2 max) {
        this.min = min;
        this.max = max;
    }

    public boolean isIn(Vector2 point) {
        return (point.x > this.min.x) && (point.x < this.max.x) && (point.y > this.min.y) && (point.y < this.max.y);
    }

    public boolean isIn(Rectangle region) {
        return (region.max.y > this.min.y) && (region.min.y < this.max.y);
    }
}
