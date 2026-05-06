package net.sattler22.crowdtwist;

import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Crowd Twist Animation Challenge
 *
 * @author Pete Sattler
 * @since Fall 2018
 * @version May 2026
 */
@Immutable
public final class Animation {

    private static final Logger logger = LoggerFactory.getLogger(Animation.class);

    private final int speed;
    private final Pixel emptyPixel;
    private final List<Pixel> initialPixels;
    private final String initialState;

    /**
     * Constructs a new, totally awesome Crowd Twist animation
     *
     * @param speed The number of positions each pixel moves in one unit of time (no direction)
     * @param initialState The initial animation state (any illegal characters are replaced by an {@code EMPTY} pixel)
     */
    public Animation(int speed, String initialState) {
        if (speed < 1)
            throw new IllegalArgumentException("Speed must be positive");
        Objects.requireNonNull(initialState, "Initial state is required");
        this.speed = speed;
        this.emptyPixel = Pixel.empty(speed);  //Might as well store this once and reuse it
        this.initialPixels = parseInitialPixels(initialState);
        this.initialState = toDisplayString(initialPixels);
    }

    private List<Pixel> parseInitialPixels(String state) {
        return state.chars()
                .mapToObj(character -> String.valueOf((char) character))
                .map(this::parsePixel)
                .toList();
    }

    private Pixel parsePixel(String id) {
        return PixelColor.lookup(id)
                .map(color -> Pixel.of(speed, color))
                .orElseGet(() -> {
                    logger.warn("Replacing invalid pixel ID {} with an empty one", id);
                    return emptyPixel;
                });
    }

    /**
     * Run the animation
     *
     * @return A list of one or more animation frames
     */
    public List<String> animate() {
        final List<String> frames = new ArrayList<>();
        List<Pixel> currentRow = initialPixels;
        do {
            frames.add(toDisplayString(currentRow));
            currentRow = animateOneRow(currentRow);
        } while (hasVisiblePixels(currentRow));
        if (hasVisiblePixels(initialPixels))
            frames.add(toDisplayString(currentRow));
        return List.copyOf(frames);
    }

    private static String toDisplayString(List<Pixel> pixels) {
        return pixels.stream()
                .map(Pixel::mixColor)
                .map(PixelColor::id)
                .collect(Collectors.joining());
    }

    private static boolean hasVisiblePixels(List<Pixel> pixels) {
        return pixels.stream().anyMatch(pixel -> !pixel.mixColor().isEmpty());
    }

    private List<Pixel> animateOneRow(List<Pixel> currentRow) {
        final int rowLength = currentRow.size();
        final List<Pixel> nextRow = createEmptyRow(rowLength);
        //Loop through all pixels in the row:
        for (int currentIndex = 0; currentIndex < rowLength; currentIndex++) {
            final Pixel currentPixel = currentRow.get(currentIndex);
            if (currentPixel.hasMovement()) {
                for (final Pixel.MoveInstruction moveInstruction : currentPixel.moveInstructions()) {
                    final int targetIndex = currentIndex + moveInstruction.direction();
                    if (isOnDisplay(targetIndex, rowLength)) {
                        //Mix together the color already at the target position with the color moving there:
                        final PixelColor existingColor = nextRow.get(targetIndex).mixColor();
                        final PixelColor incomingColor = moveInstruction.pixel().mixColor();
                        final PixelColor mixedColor = existingColor.mix(incomingColor);
                        nextRow.set(targetIndex, Pixel.of(speed, mixedColor));
                    }
                }
            }
        }
        return List.copyOf(nextRow);
    }

    private List<Pixel> createEmptyRow(int length) {
        return new ArrayList<>(Collections.nCopies(length, emptyPixel));
    }

    private static boolean isOnDisplay(int pos, int displayLength) {
        return pos >= 0 && pos < displayLength;
    }

    /**
     * Get the speed
     *
     * @return The number of positions each pixel moves in one unit time (no direction)
     */
    public int speed() {
        return speed;
    }

    /**
     * Get the initial state
     *
     * @return The initial animation state (any illegal characters are replaced by an {@code EMPTY} pixel)
     */
    public String initialState() {
        return initialState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(speed, initialPixels);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Animation that))
            return false;
        return this.speed == that.speed && initialPixels.equals(that.initialPixels);
    }

    @Override
    public String toString() {
        return "%s [speed=%d, initialState=%s]".formatted(getClass().getSimpleName(), speed, initialState);
    }
}
