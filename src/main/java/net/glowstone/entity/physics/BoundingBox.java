package net.glowstone.entity.physics;

import com.flowpowered.math.vector.Vector3d;
import net.glowstone.util.MutableVector;

/**
 * A rectangular bounding box with minimum and maximum corners.
 */
public class BoundingBox implements Cloneable {
    public final MutableVector minCorner;
    public final MutableVector maxCorner;

    private BoundingBox(MutableVector minCorner, MutableVector maxCorner) {
        this.minCorner = minCorner;
        this.maxCorner = maxCorner;
    }

    public BoundingBox() {
        this(new MutableVector(), new MutableVector());
    }

    public Vector3d getSize() {
        return new Vector3d(maxCorner.getX() - minCorner.getX(), maxCorner.getY() - minCorner.getY(), maxCorner.getZ() - minCorner.getZ());
    }

    public final boolean intersects(BoundingBox other) {
        return intersects(this, other);
    }

    public static boolean intersects(BoundingBox a, BoundingBox b) {
        MutableVector minA = a.minCorner, maxA = a.maxCorner;
        MutableVector minB = b.minCorner, maxB = b.maxCorner;
        return (maxA.getX() >= minB.getX() && minA.getX() <= maxB.getX() &&
                maxA.getY() >= minB.getY() && minA.getY() <= maxB.getY() &&
                maxA.getZ() >= minB.getZ() && minA.getZ() <= maxB.getZ());
    }

    public static BoundingBox fromCorners(MutableVector a, MutableVector b) {
        double x = Math.min(a.getX(), b.getX());
        double y = Math.min(a.getY(), b.getY());
        double z = Math.min(a.getZ(), b.getZ());
        MutableVector minCorner = new MutableVector(x, y, z);

        x = Math.max(a.getX(), b.getX());
        y = Math.max(a.getY(), b.getY());
        z = Math.max(a.getZ(), b.getZ());
        MutableVector maxCorner = new MutableVector(x, y, z);

        return new BoundingBox(minCorner, maxCorner);
    }

    public static BoundingBox fromPositionAndSize(MutableVector pos, MutableVector size) {
        return new BoundingBox(pos, pos.addNew(size));
    }

    public static BoundingBox copyOf(BoundingBox original) {
        return new BoundingBox(original.minCorner.clone(), original.maxCorner.clone());
    }

}
