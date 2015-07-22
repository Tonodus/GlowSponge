package net.glowstone.util;

import com.google.common.collect.ImmutableList;
import org.spongepowered.api.util.Direction;

import java.util.List;

import static org.spongepowered.api.util.Direction.*;

/**
 * A static class housing position-related utilities and constants.
 * @author Graham Edgecombe
 */
public final class Position {

    private Position() {
    }

    /**
     * The number of integer values between each double value. For example, if
     * the coordinate was {@code 1.5}, this would be sent as
     * {@code 1.5 * 32 = 48} within certain packets.
     */
    public static final int GRANULARITY = 32;

    /**
     * Common Rotation values used blocks such as Signs, Skulls, and Banners.
     * The order relates to the data/tag that is applied to the block on placing.
     */
    public static final List<Direction> ROTATIONS = ImmutableList.of(NORTH, NORTH_NORTHEAST, NORTHEAST,
            EAST_NORTHEAST, EAST, EAST_SOUTHEAST, SOUTHEAST, SOUTH_SOUTHEAST, SOUTH, SOUTH_SOUTHWEST,
            SOUTHWEST, WEST_SOUTHWEST, WEST, WEST_NORTHWEST, NORTHWEST, NORTH_NORTHWEST);

    /**
     * Gets the X coordinate multiplied the granularity and rounded to an
     * integer.
     * @return An integer approximation of the X coordinate.
     */
    public static int getIntX(MutableVector loc) {
        return (int) (loc.getX() * GRANULARITY);
    }

    /**
     * Gets the Y coordinate multiplied the granularity and rounded to an
     * integer.
     * @return An integer approximation of the Y coordinate.
     */
    public static int getIntY(MutableVector loc) {
        return (int) (loc.getY() * GRANULARITY);
    }

    /**
     * Gets the Z coordinate multiplied the granularity and rounded to an
     * integer.
     * @return An integer approximation of the Z coordinate.
     */
    public static int getIntZ(MutableVector loc) {
        return (int) (loc.getZ() * GRANULARITY);
    }

    /**
     * Gets an integer approximation of the yaw between 0 and 255.
     * @return An integer approximation of the yaw.
     */
    public static int getIntYaw(double yaw) {
        return (int) (((yaw % 360) / 360) * 256);
    }

    /**
     * Gets an integer approximation of the pitch between 0 and 255.
     * @return An integer approximation of the yaw.
     */
    public static int getIntPitch(double pitch) {
        return (int) (((pitch % 360) / 360) * 256);
    }

    /**
     * Gets whether there has been a position change between the two Vector3ds.
     * @return A boolean.
     */
    public static boolean hasMoved(MutableVector first, MutableVector second) {
        return first.getX() != second.getX() || first.getY() != second.getY() || first.getZ() != second.getZ();
    }

    /**
     * Gets whether there has been a rotation change between the two Vector3ds.
     * @return A boolean.
     */
    public static boolean hasRotated(MutableVector first, MutableVector second) {
        return first.getY() != second.getY() || first.getX() != second.getX();
    }

    /**
     * Get an intercardinal BlockFace from a rotation value, where NORTH is 0.
     * @param rotation byte value rotation to get
     * @return intercardinal BlockFace
     * @throws IndexOutOfBoundsException If 0 > value > 15
     */
    public static Direction getDirection(byte rotation) {
        return ROTATIONS.get(rotation);
    }

    /**
     * Gets the byte rotation for an intercardinal BlockFace, where NORTH is 0.
     * @param rotation Rotation to get
     * @return byte data value for the given rotation, or -1 if rotation is SELF or null
     */
    public static byte getDirection(Direction rotation) {
        return (byte) ROTATIONS.indexOf(rotation);
    }

}
