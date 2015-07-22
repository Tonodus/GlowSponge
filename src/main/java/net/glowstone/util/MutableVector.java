package net.glowstone.util;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.world.Location;

public class MutableVector {
    private double x, y, z;

    public MutableVector(Vector3d position) {
        this(position.getX(), position.getY(), position.getZ());
    }

    public MutableVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public MutableVector() {
        this(0, 0, 0);
    }

    public MutableVector(Location position) {
        this(position.getX(), position.getY(), position.getZ());
    }

    // Getter and cloning

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public int getBlockX() {
        return (int) Math.floor(x);
    }

    public int getBlockY() {
        return (int) Math.floor(y);
    }

    public int getBlockZ() {
        return (int) Math.floor(z);
    }

    public MutableVector clone() {
        return new MutableVector(x, y, z);
    }

    // Setting values from other objects

    public void updateFrom(MutableVector location) {
        this.x = location.x;
        this.y = location.y;
        this.z = location.z;
    }

    public void updateFrom(Vector3d location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    public void updateFrom(Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    // Converting to other objects

    public Vector3d toVector3d() {
        return new Vector3d(x, y, z);
    }

    public Vector3i toVector3i() {
        return new Vector3i(getBlockX(), getBlockY(), getBlockZ());
    }

    // Creating based on this

    public MutableVector addNew(MutableVector add) {
        return new MutableVector(x + add.x, y + add.y, z + add.z);
    }

    public MutableVector addNew(double x, double y, double z) {
        return new MutableVector(this.x + x, this.y + y, this.z + z);
    }

    // Modifying

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void normalize() {
        divide(length());
    }

    public void add(Vector3d vec) {
        this.x += vec.getX();
        this.y += vec.getY();
        this.z += vec.getZ();
    }

    public void add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public void sub(Vector3d vec) {
        this.x -= vec.getX();
        this.y -= vec.getY();
        this.z -= vec.getZ();
    }

    public void multiply(double v) {
        this.x *= v;
        this.y *= v;
        this.z *= v;
    }

    public void divide(double v) {
        this.x /= v;
        this.y /= v;
        this.z /= v;
    }

    // Utils
    private double length() {
        return Math.sqrt(lengthSq());
    }

    private double lengthSq() {
        return x * x + y * y + z * z;
    }
}
