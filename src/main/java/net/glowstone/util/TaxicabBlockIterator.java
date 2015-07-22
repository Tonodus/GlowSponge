package net.glowstone.util;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;

import java.util.*;

public class TaxicabBlockIterator implements Iterator<Location> {

    private static final Direction[] VALID_FACES = new Direction[]{
            Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST
    };

    private final Queue<Object> pendingAnalysis = new LinkedList<>();
    private final Queue<Location> nextValidBlocks = new LinkedList<>();
    private final Set<Location> usedBlocks = new HashSet<>();
    private int currentDistance = 1;
    private int validBlockCount = 0;

    private int maxDistance = Integer.MAX_VALUE;
    private int maxBlocks = Integer.MAX_VALUE;
    private Validator<BlockType> validator;

    public TaxicabBlockIterator(Location origin) {
        pendingAnalysis.add(origin);
        pendingAnalysis.add(DistanceMarker.INSTANCE);
        usedBlocks.add(origin);
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public void setMaxBlocks(int maxBlocks) {
        this.maxBlocks = maxBlocks;
    }

    public void setValidator(Validator<BlockType> validator) {
        this.validator = validator;
    }

    private boolean isValid(Location block) {
        return validator == null || validator.isValid(block.getBlockType());
    }

    @Override
    public boolean hasNext() {
        if (validBlockCount >= maxBlocks) {
            return false;
        }

        // Keep going till the valid block queue contains something, we reach the distance limit, or we empty the pending analysis queue.
        // Note that the pending analysis queue will always contain at least one element: the end of distance marker.
        while (nextValidBlocks.isEmpty() && currentDistance <= maxDistance && pendingAnalysis.size() >= 2) {
            Object object = pendingAnalysis.remove();

            // If we find the end of distance marker, we'll increase the distance, and then we'll re-add it to the end.
            if (object == DistanceMarker.INSTANCE) {
                pendingAnalysis.add(object);
                currentDistance++;
                continue;
            }

            // If it wasn't the EoD marker, it must be a block. We'll look now for valid blocks around it.
            Location block = (Location) object;
            for (Direction face : VALID_FACES) {
                Location near = block.getRelative(face);

                // Only analyse the block if we haven't checked it yet.
                if (usedBlocks.add(near) && isValid(near)) {
                    nextValidBlocks.add(near);
                    pendingAnalysis.add(near);
                }
            }
        }

        return !nextValidBlocks.isEmpty();
    }

    @Override
    public Location next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        validBlockCount++;
        return nextValidBlocks.remove();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    private static final class DistanceMarker {

        public static final DistanceMarker INSTANCE = new DistanceMarker();

        private DistanceMarker() {
        }

    }
}
