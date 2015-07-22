package net.glowstone.world;

public class ChunkUtils {
    /**
     * The dimensions of a chunk (width: x, height: z, depth: y).
     */
    public static final int WIDTH = 16, HEIGHT = 16, DEPTH = 256;

    /**
     * The Y depth of a single chunk section.
     */
    public static final int SEC_DEPTH = 16;


    /**
     * Attempt to get the ChunkSection at the specified height.
     * @param y the y value.
     * @return The ChunkSection, or null if it is empty.
     */
    public static ChunkSection getSection(int y, ChunkSection[] sections) {
        int idx = y >> 4;
        if (y < 0 || y >= DEPTH || idx >= sections.length) {
            return null;
        }
        return sections[idx];
    }

    /**
     * Converts a three-dimensional coordinate to an index within the
     * one-dimensional arrays.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return The index within the arrays.
     */
    public static int coordToIndex(int x, int z, int y) {
        if (x < 0 || z < 0 || y < 0 || x >= WIDTH || z >= HEIGHT || y >= DEPTH)
            throw new IndexOutOfBoundsException("Coords (x=" + x + ",y=" + y + ",z=" + z + ") invalid");

        return (y * HEIGHT + z) * WIDTH + x;
    }

    public static ChunkSection getOrCreateSection(int y, ChunkSection[] sections) {
        int idx = y >> 4;
        if (y < 0 || y >= DEPTH || idx >= sections.length) {
            return null;
        }
        ChunkSection section = sections[idx];
        if (section == null) {
            section = sections[idx] = new ChunkSection();
        }
        return section;
    }
}
