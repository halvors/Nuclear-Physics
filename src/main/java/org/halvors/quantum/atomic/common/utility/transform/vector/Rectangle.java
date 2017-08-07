package org.halvors.quantum.atomic.common.utility.transform.vector;

public class Rectangle {
    private Vector2 min;
    private Vector2 max;

    public Rectangle() {
        this(new Vector2(), new Vector2());
    }

    public Rectangle(Vector2 min, Vector2 max) {
        this.min = min;
        this.max = max;
    }

    public boolean isIn(Vector2 point) {
        return (point.getX() > min.getX()) && (point.getX() < max.getX()) && (point.getY() > min.getY()) && (point.getY() < max.getY());
    }

    public boolean isIn(Rectangle region) {
        return (region.max.getY() > min.getY()) && (region.min.getY() < max.getY());
    }
}
