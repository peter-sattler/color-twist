package net.sattler22.crowdtwist;

import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private final String initialState;
    private final List<Pixel> initialPixels;

    /**
     * Constructs a new, totally awesome Crowd Twist animation
     *
     * @param speed The number of positions each pixel moves in one unit of time (no direction)
     * @param initialState The initial animation state (any illegal characters are replaced by an {@code EMPTY} pixel).
     */
    public Animation(int speed, String initialState) {
        Objects.requireNonNull(initialState, "Initial state is required");
        this.speed = speed;
        this.emptyPixel = Pixel.empty(speed);  //Might as well store this once and reuse it
        this.initialState = initialState;
        this.initialPixels = parseInitialPixels(initialState);
    }

    private List<Pixel> parseInitialPixels(String state) {
        final List<Pixel> pixels = new ArrayList<>(state.length());
        for (final char id : state.toCharArray())
            pixels.add(parsePixel(String.valueOf(id)));
        return List.copyOf(pixels);
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
            frames.add(parseInitialState(currentRow));
            currentRow = animateOneRow(currentRow);
        } while (hasVisiblePixels(currentRow));
        if (hasVisiblePixels(initialPixels))
            frames.add(parseInitialState(currentRow));
        return List.copyOf(frames);
    }

    private static String parseInitialState(List<Pixel> pixels) {
        final StringBuilder builder = new StringBuilder(pixels.size());
        for (final Pixel pixel : pixels)
            builder.append(pixel.mixColor().id());
        return builder.toString();
    }

    private static boolean hasVisiblePixels(List<Pixel> pixels) {
        for (final Pixel pixel : pixels) {
            if (!pixel.mixColor().isEmpty())
                return true;
        }
        return false;
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
        final List<Pixel> row = new ArrayList<>(length);
        for (int i = 0; i < length; i++)
            row.add(emptyPixel);
        return row;
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
     * Get the normalized initial state
     *
     * @return The initial state with illegal characters are replaced by an {@code EMPTY} pixel
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
        if (other == null)
            return false;
        if (!(other instanceof Animation that))
            return false;
        return this.speed == that.speed && initialPixels.equals(that.initialPixels);
    }

    @Override
    public String toString() {
        return "%s [speed=%d, initialState=%s, initialPixels=%s]"
                .formatted(getClass().getSimpleName(), speed, initialState, initialPixels);
    }
}
