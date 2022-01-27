package net.sattler22.crowdtwist;

import static net.sattler22.crowdtwist.PixelColor.EMPTY;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.jcip.annotations.Immutable;

/**
 * A pixel has speed and color. A composite color is represented by its primary color components.
 *
 * @author Pete Sattler
 * @version Fall 2018
 */
@Immutable
public final class Pixel {

    private final int speed;
    private final PixelColor primaryColor1;
    private final PixelColor primaryColor2;

    /**
     * New pixel factory
     *
     * @param speed The number of positions each pixel moves in one unit time (no direction)
     * @param pixelColor Any supported primary or composite pixel color
     * @return A new pixel with either a single primary color or a composite color represented by two primary colors
     */
    public static Pixel of(int speed, PixelColor pixelColor) {
        Objects.requireNonNull(pixelColor);
        if (pixelColor == EMPTY || pixelColor.isPrimary())
            return new Pixel(speed, pixelColor, EMPTY);
        //Otherwise break the composite color into its primary color components:
        final var primaryColors = PixelColor.findPrimaryColors(pixelColor);
        return new Pixel(speed, primaryColors.get(0), primaryColors.get(1));
    }

    /**
     * Empty pixel factory
     *
     * @param speed The number of positions each pixel moves in one unit time (no direction)
     * @return A new pixel with the given speed and both primary colors set to <code>EMPTY</code>
     */
    public static Pixel empty(int speed) {
        return new Pixel(speed, EMPTY, PixelColor.EMPTY);
    }

    private Pixel(int speed, PixelColor primaryColor1, PixelColor primaryColor2) {
        this.speed = speed;
        this.primaryColor1 = primaryColor1;
        this.primaryColor2 = primaryColor2;
    }

    /**
     * Get the number of positions each pixel moves in one unit time (no direction)
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Pixel movement check
     *
     * @return True if the pixel moves either LEFT or RIGHT
     */
    public boolean hasMovement() {
        return primaryColor1.hasMovement() || primaryColor2.hasMovement();
    }

    /**
     * Mix together the two primary colors
     */
    public PixelColor mixColor() {
        return primaryColor1.mix(primaryColor2);
    }

    /**
     * Generate move instruction
     *
     * @return A list of instructions used to move the pixel according to its speed and direction
     */
    public List<MoveInstruction> getMoveInstructions() {
        final List<MoveInstruction> moveInstructions = new ArrayList<>();
        if (primaryColor1 != EMPTY)
            moveInstructions.add(new MoveInstruction(speed, primaryColor1));
        if (primaryColor2 != EMPTY)
            moveInstructions.add(new MoveInstruction(speed, primaryColor2));
        return moveInstructions;
    }

    static class MoveInstruction {

        private Pixel pixel;

        private MoveInstruction(int speed, PixelColor pixelColor) {
            this.pixel = Pixel.of(speed, pixelColor);
        }

        public int getDirection() {
            return pixel.mixColor().getDirection(pixel.getSpeed());
        }

        public Pixel getPixel() {
            return pixel;
        }

        @Override
        public String toString() {
            return String.format("%s [pixel=%s]", getClass().getSimpleName(), pixel);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(primaryColor1, primaryColor2);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (this.getClass() != other.getClass())
            return false;
        final var that = (Pixel) other;
        return this.primaryColor1 == that.primaryColor1 && this.primaryColor2 == that.primaryColor2;
    }

    @Override
    public String toString() {
        return mixColor().getId();
    }
}
