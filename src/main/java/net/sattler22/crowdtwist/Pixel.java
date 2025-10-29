package net.sattler22.crowdtwist;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A <code>Pixel</code> has speed and color. A composite color is represented by its primary color components.
 *
 * @author Pete Sattler
 * @version October 2025
 * @since Fall 2018
 */
public record Pixel(int speed, PixelColor primaryColor1, PixelColor primaryColor2) {

    /**
     * New pixel factory
     *
     * @param speed The number of positions each pixel moves in one unit time (no direction)
     * @param pixelColor Any supported primary or composite pixel color
     * @return A new pixel with either a single primary color or a composite color represented by two primary colors
     */
    public static Pixel of(int speed, PixelColor pixelColor) {
        Objects.requireNonNull(pixelColor);
        if (pixelColor == PixelColor.EMPTY || pixelColor.isPrimary())
            return new Pixel(speed, pixelColor, PixelColor.EMPTY);
        //Otherwise break the composite color into its primary color components:
        final List<PixelColor> primaryColors = PixelColor.findPrimaryColors(pixelColor);
        return new Pixel(speed, primaryColors.getFirst(), primaryColors.get(1));
    }

    /**
     * Empty pixel factory
     *
     * @param speed The number of positions each pixel moves in one unit time (no direction)
     * @return A new pixel with the given speed and both primary colors set to <code>EMPTY</code>
     */
    public static Pixel empty(int speed) {
        return new Pixel(speed, PixelColor.EMPTY, PixelColor.EMPTY);
    }

    /**
     * Pixel movement condition check
     *
     * @return True if the pixel moves either LEFT or RIGHT
     */
    public boolean hasMovement() {
        return primaryColor1.hasMovement() || primaryColor2.hasMovement();
    }

    /**
     * Mix the two primary colors together
     */
    public PixelColor mixColor() {
        return primaryColor1.mix(primaryColor2);
    }

    /**
     * Generate move instructions
     *
     * @return A list of instructions used to move the pixel according to its speed and direction
     */
    public List<MoveInstruction> moveInstructions() {
        final List<MoveInstruction> moveInstructions = new ArrayList<>();
        if (primaryColor1 != PixelColor.EMPTY)
            moveInstructions.add(new MoveInstruction(speed, primaryColor1));
        if (primaryColor2 != PixelColor.EMPTY)
            moveInstructions.add(new MoveInstruction(speed, primaryColor2));
        return moveInstructions;
    }

    /**
     * Move instruction
     */
    public static class MoveInstruction {

        private final Pixel pixel;

        private MoveInstruction(int speed, PixelColor pixelColor) {
            this.pixel = Pixel.of(speed, pixelColor);
        }

        public int direction() {
            return pixel.mixColor().direction(pixel.speed());
        }

        public Pixel pixel() {
            return pixel;
        }

        @Override
        public String toString() {
            return String.format("%s [pixel=%s]", getClass().getSimpleName(), pixel);
        }
    }
}
