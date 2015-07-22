package net.glowstone.world;

import net.glowstone.util.NibbleArray;

/**
 * A single cubic section of a chunk, with all data.
 */
public class ChunkSection {
    private static final int WIDTH = 16;
    private static final int HEIGHT = 16;
    private static final int SEC_DEPTH = 256;
    private static final int ARRAY_SIZE = WIDTH * HEIGHT * SEC_DEPTH;


    // these probably should be made non-public
    public final char[] types;
    public final NibbleArray skyLight;
    public final NibbleArray blockLight;
    public int count; // amount of non-air blocks

    /**
     * Create a new, empty ChunkSection.
     */
    public ChunkSection() {
        types = new char[ARRAY_SIZE];
        skyLight = new NibbleArray(ARRAY_SIZE);
        blockLight = new NibbleArray(ARRAY_SIZE);
        skyLight.fill((byte) 0xf);
    }

    /**
     * Create a ChunkSection with the specified chunk data. This
     * ChunkSection assumes ownership of the arrays passed in, and they
     * should not be further modified.
     */
    public ChunkSection(char[] types, NibbleArray skyLight, NibbleArray blockLight) {
        if (types.length != ARRAY_SIZE || skyLight.size() != ARRAY_SIZE || blockLight.size() != ARRAY_SIZE) {
            throw new IllegalArgumentException("An array length was not " + ARRAY_SIZE + ": " + types.length + " " + skyLight.size() + " " + blockLight.size());
        }
        this.types = types;
        this.skyLight = skyLight;
        this.blockLight = blockLight;
        recount();
    }

    /**
     * Calculate the index into internal arrays for the given coordinates.
     */
    public int index(int x, int y, int z) {
        if (x < 0 || z < 0 || x >= WIDTH || z >= HEIGHT) {
            throw new IndexOutOfBoundsException("Coords (x=" + x + ",z=" + z + ") out of section bounds");
        }
        return ((y & 0xf) << 8) | (z << 4) | x;
    }

    /**
     * Recount the amount of non-air blocks in the chunk section.
     */
    public void recount() {
        count = 0;
        for (char type : types) {
            if (type != 0) {
                count++;
            }
        }
    }

    /**
     * Take a snapshot of this section which will not reflect future changes.
     */
    public ChunkSection snapshot() {
        return new ChunkSection(types.clone(), skyLight.snapshot(), blockLight.snapshot());
    }
}
