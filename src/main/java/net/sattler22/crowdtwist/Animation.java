package net.sattler22.crowdtwist;

import static net.sattler22.crowdtwist.PixelColor.EMPTY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jcip.annotations.Immutable;

/**
 * Crowd Twist Animation Challenge 2018
 *
 * @author Pete Sattler
 * @version Fall 2018
 */
@Immutable
public final class Animation {

    private static final Logger logger = LoggerFactory.getLogger(Animation.class);
    private final int speed;
    private final Pixel emptyPixel;
    private final Pixel[] initialValue;

    /**
     * Constructs a new totally awesome Crowd Twist animation
     *
     * @param speed The number of positions each pixel moves in one unit time (no direction)
     * @param initialString The initial animation string. Any illegal characters are replaced by an <code>EMPTY</code> pixel.
     */
    public Animation(int speed, String initialString) {
        this.speed = speed;
        this.emptyPixel = Pixel.empty(speed);  //Might as well store this once and reuse it
        int index = 0;
        this.initialValue = new Pixel[initialString.length()];
        for (final var id : initialString.split("")) {
            final var pixelColor = PixelColor.lookup(id);
            if (pixelColor.isPresent()) {
                final var pixel = Pixel.of(speed, pixelColor.get());
                this.initialValue[index] = pixel;
            } else {
                logger.warn("Substituting an empty pixel for a missing one");
                this.initialValue[index] = emptyPixel;
            }
            index++;
        }
    }

    /**
     * Run the animation
     *
     * @return An array of String pixel color IDs representing the animation sequence
     */
    public String[] animate() {
        final List<String> resultList = new ArrayList<>();
        var row = initialValue;
        resultList.add(pixelArray2String(row));
        while (!isComplete(row = animateOneRow(row)))
            resultList.add(pixelArray2String(row));
        if (!isComplete(initialValue))
            resultList.add(pixelArray2String(row));
        final var resultArray = new String[resultList.size()];
        return resultList.toArray(resultArray);
    }

    /**
     * Convert a pixel array to a string
     */
    private static String pixelArray2String(Pixel[] pixels) {
        return Arrays.stream(pixels).map(pixel -> pixel.mixColor().getId()).collect(Collectors.joining());
    }

    /**
     * Animation complete check
     */
    private static boolean isComplete(Pixel[] pixels) {
        for (final var pixel : pixels)
            if (pixel.mixColor() != EMPTY)
                return false;
        return true;
    }

    /**
     * Animate a single row
     */
    private Pixel[] animateOneRow(final Pixel[] pixels) {
        final var result = new Pixel[pixels.length];
        Arrays.fill(result, emptyPixel);
        //Loop thru all pixels in the row:
        for (var currentIndex = 0; currentIndex < pixels.length; currentIndex++) {
            final var pixel = pixels[currentIndex];
            if (pixel.hasMovement()) {
                final var moveInstructions = pixel.getMoveInstructions();
                for (final var moveInstruction : moveInstructions) {
                    final var targetIndex = currentIndex + moveInstruction.getDirection();
                    if (isOnDisplay(targetIndex)) {
                        //Mix together the color already at the target position with the color moving there:
                        final var existingTargetColor = result[targetIndex].mixColor();
                        final var mixedColor = existingTargetColor.mix(moveInstruction.getPixel().mixColor());
                        result[targetIndex] = Pixel.of(speed, mixedColor);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Check if a position fits on the display
     */
    private boolean isOnDisplay(int pos) {
        return pos > -1 && pos < initialValue.length;
    }

    /**
     * Get speed
     *
     * @return The number of positions each pixel moves in one unit time (no direction)
     */
    public int getSpeed() {
        return speed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(speed, initialValue);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (this.getClass() != other.getClass())
            return false;
        final var that = (Animation) other;
        return this.speed == that.speed && Arrays.deepEquals(this.initialValue, that.initialValue);
    }

    @Override
    public String toString() {
        return String.format("%s [speed=%s, emptyPixel=%s, initialValue=%s]", getClass().getSimpleName(), speed, emptyPixel, Arrays.toString(initialValue));
    }
}
