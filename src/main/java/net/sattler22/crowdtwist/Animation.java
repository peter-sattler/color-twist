package net.sattler22.crowdtwist;

import net.jcip.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Crowd Twist Animation Challenge
 *
 * @author Pete Sattler
 * @version October 2025
 * @since Fall 2018
 */
@Immutable
public final class Animation {

    private static final Logger logger = LoggerFactory.getLogger(Animation.class);
    private final int speed;
    private final String initialState;
    private final Pixel emptyPixel;
    private final Pixel[] initialValue;

    /**
     * Constructs a new totally awesome Crowd Twist animation
     *
     * @param speed The number of positions each pixel moves in one unit of time (no direction)
     * @param initialState The initial animation state (any illegal characters are replaced by an
     *                     <code>EMPTY</code>pixel).
     */
    public Animation(int speed, String initialState) {
        this.speed = speed;
        this.initialState = initialState;
        this.emptyPixel = Pixel.empty(speed);  //Might as well store this once and reuse it
        this.initialValue = new Pixel[initialState.length()];
        int index = 0;
        for (final String id : initialState.split("")) {
            final Optional<PixelColor> pixelColor = PixelColor.lookup(id);
            if (pixelColor.isPresent()) {
                final Pixel pixel = Pixel.of(speed, pixelColor.get());
                this.initialValue[index] = pixel;
            }
            else {
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
        Pixel[] row = Arrays.copyOf(initialValue, initialValue.length);
        do {
            resultList.add(pixelArray2String(row));
            row = animateOneRow(row);
        } while(hasMore(row));
        if (hasMore(initialValue))
            resultList.add(pixelArray2String(row));
        final String[] resultArray = new String[resultList.size()];
        return resultList.toArray(resultArray);
    }

    private static String pixelArray2String(Pixel[] pixels) {
        return Arrays.stream(pixels).map(pixel -> pixel.mixColor().id()).collect(Collectors.joining());
    }

    private static boolean hasMore(Pixel[] pixels) {
        for (final Pixel pixel : pixels)
            if (pixel.mixColor() != PixelColor.EMPTY)
                return true;
        return false;
    }

    private Pixel[] animateOneRow(final Pixel[] pixels) {
        final Pixel[] result = new Pixel[pixels.length];
        Arrays.fill(result, emptyPixel);
        //Loop through all pixels in the row:
        for (int currentIndex = 0; currentIndex < pixels.length; currentIndex++) {
            final Pixel pixel = pixels[currentIndex];
            if (pixel.hasMovement()) {
                final List<Pixel.MoveInstruction> moveInstructions = pixel.moveInstructions();
                for (final Pixel.MoveInstruction moveInstruction : moveInstructions) {
                    final int targetIndex = currentIndex + moveInstruction.direction();
                    if (isOnDisplay(targetIndex)) {
                        //Mix together the color already at the target position with the color moving there:
                        final PixelColor existingTargetColor = result[targetIndex].mixColor();
                        final PixelColor mixedColor = existingTargetColor.mix(moveInstruction.pixel().mixColor());
                        result[targetIndex] = Pixel.of(speed, mixedColor);
                    }
                }
            }
        }
        return result;
    }

    private boolean isOnDisplay(int pos) {
        return pos > -1 && pos < initialValue.length;
    }

    /**
     * Get speed
     *
     * @return The number of positions each pixel moves in one unit time (no direction)
     */
    public int speed() {
        return speed;
    }

    /**
     * Get initial state
     *
     * @return The initial state (any illegal characters are replaced by an <code>EMPTY</code> pixel)
     */
    public String initialState() {
        return initialState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(speed, Arrays.hashCode(initialValue));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (this.getClass() != other.getClass())
            return false;
        final Animation that = (Animation) other;
        return this.speed == that.speed && Arrays.deepEquals(this.initialValue, that.initialValue);
    }

    @Override
    public String toString() {
        return String.format("%s [speed=%s, emptyPixel=%s, initialValue=%s]", getClass().getSimpleName(), speed, emptyPixel, Arrays.toString(initialValue));
    }
}
