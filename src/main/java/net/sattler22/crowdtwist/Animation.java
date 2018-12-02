package net.sattler22.crowdtwist;

import static net.sattler22.crowdtwist.PixelColor.EMPTY;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sattler22.crowdtwist.Pixel.MoveInstruction;

/**
 * Crowd Twist Animation Challenge 2018
 * 
 * @author Pete Sattler
 * @version Fall 2018
 * @implSpec This class is immutable and thread-safe
 */
public final class Animation implements Serializable {

    private static final long serialVersionUID = -7744637730300100216L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Animation.class);
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
        this.emptyPixel = Pixel.empty(speed);  // Might as well store this once and reuse it
        int index = 0;
        this.initialValue = new Pixel[initialString.length()];
        for (String id : initialString.split("")) {
            Optional<PixelColor> pixelColor = PixelColor.lookup(id);
            if (pixelColor.isPresent()) {
                Pixel pixel = Pixel.of(speed, pixelColor.get());
                this.initialValue[index] = pixel;
            } else {
                LOGGER.warn("Substituting an empty pixel for a missing one");
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
        List<String> resultList = new ArrayList<>();
        Pixel[] row = initialValue;
        resultList.add(pixelArray2String(row));
        while (!isComplete(row = animateOneRow(row))) {
            resultList.add(pixelArray2String(row));
        }
        if (!isComplete(initialValue))
            resultList.add(pixelArray2String(row));
        String[] resultArray = new String[resultList.size()];
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
        for (Pixel pixel : pixels) {
            if (pixel.mixColor() != EMPTY)
                return false;
        }
        return true;
    }

    /**
     * Animate a single row
     */
    private Pixel[] animateOneRow(final Pixel[] pixels) {
        Pixel[] result = new Pixel[pixels.length];
        Arrays.fill(result, emptyPixel);
        //Loop thru all pixels in the row:
        for (int currentIndex = 0; currentIndex < pixels.length; currentIndex++) {
            final Pixel pixel = pixels[currentIndex];
            if (pixel.hasMovement()) {
                List<MoveInstruction> moveInstructions = pixel.getMoveInstructions();
                for (MoveInstruction moveInstruction : moveInstructions) {
                    final int targetIndex = currentIndex + moveInstruction.getDirection();
                    if (isOnDisplay(targetIndex)) {
                        //Mix together the color already at the target position with the color moving there:
                        final PixelColor existingTargetColor = result[targetIndex].mixColor();
                        final PixelColor mixedColor = existingTargetColor.mix(moveInstruction.getPixel().mixColor());
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
