package Models;

import java.util.Objects;

public class Vector {
    private Double x;
    private Double y;

    public Vector(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Vector() {
    	this.x = 0.0;
    	this.y = 0.0;
	}

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Vector subtract(Vector v) {
        return new Vector(x - v.x, y - v.y);
    }

    public Vector add(Vector v) {
        return new Vector(x + v.x, y + v.y);
    }

    public Vector dot(Double c) {
        return new Vector(x * c, y * c);
    }

    public Double dot(Vector v) {
        return x * v.x + y * v.y;
    }

    public Double distance (Vector v) {
        return this.subtract(v).norm();
    }

    public Double norm() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector normalize() {
        Double norm = norm();
        return this.dot(1.0 / norm);
    }

    // Get positive angle from X axis in trigonometric direction
    public Double getAngle() {
        Double angle = Math.atan2(getY(), getX());
        if (angle < 0) return angle + 2 * Math.PI;
        return angle;

    }

    public Vector projectionOnVector(Vector v) {
        Double angle1 = v.getAngle();
        angle1.equals(null);
        Double angle2 = this.getAngle();
        Double relAngle = Math.abs(angle1 - angle2);
        return v.normalize().dot(Math.cos(relAngle) * this.norm());
    }

    @Override
    public boolean equals(Object o) {
        Double IMPRECISION = 1E-15;
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Math.abs(x - vector.x) < IMPRECISION && Math.abs(y - vector.y) < IMPRECISION;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(x=" + x + ", y=" + y + ")";
    }
}
