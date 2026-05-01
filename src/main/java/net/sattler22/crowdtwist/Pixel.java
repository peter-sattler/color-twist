package net.sattler22.crowdtwist;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A {@code Pixel} has speed and color. A composite color is represented by its primary color components.
 *
 * @author Pete Sattler
 * @since Fall 2018
 * @version May 2026
 */
public record Pixel(int speed, PixelColor primaryColor1, PixelColor primaryColor2) {

    /**
     * Creates a new pixel
     *
     * @param speed The number of positions each pixel moves in one unit time (no direction)
     * @param pixelColor Any supported primary or composite pixel color
     * @return A new pixel with either a single primary color or a composite color represented by two primary colors
     */
    public static Pixel of(int speed, PixelColor pixelColor) {
        Objects.requireNonNull(pixelColor, "Pixel color is required");
        if (pixelColor.isEmpty() || pixelColor.isPrimary())
            return new Pixel(speed, pixelColor, PixelColor.EMPTY);
        //Otherwise break the composite color into its primary color components:
        final List<PixelColor> primaryColors = PixelColor.findPrimaryColors(pixelColor);
        return new Pixel(speed, primaryColors.getFirst(), primaryColors.get(1));
    }

    /**
     * Creates an empty pixel
     *
     * @param speed The number of positions each pixel moves in one unit time (no direction)
     * @return A new pixel with the given speed and both primary colors set to {@code EMPTY}
     */
    public static Pixel empty(int speed) {
        return new Pixel(speed, PixelColor.EMPTY, PixelColor.EMPTY);
    }

    public Pixel {
        Objects.requireNonNull(primaryColor1, "First primary color is required");
        Objects.requireNonNull(primaryColor2, "Second primary color is required");
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
     *
     * @return The visible color of this pixel
     */
    public PixelColor mixColor() {
        return primaryColor1.mix(primaryColor2);
    }

    /**
     * Creates move instructions for each non-empty primary color component
     *
     * @return A list of instructions used to move the pixel according to its speed and direction
     */
    public List<MoveInstruction> moveInstructions() {
        final List<MoveInstruction> moveInstructions = new ArrayList<>(2);
        if (!primaryColor1.isEmpty())
            moveInstructions.add(new MoveInstruction(Pixel.of(speed, primaryColor1)));
        if (!primaryColor2.isEmpty())
            moveInstructions.add(new MoveInstruction(Pixel.of(speed, primaryColor2)));
        return List.copyOf(moveInstructions);
    }

    /**
     * Move instruction for a single pixel
     */
    public record MoveInstruction(Pixel pixel) {

        public MoveInstruction {
            Objects.requireNonNull(pixel, "Pixel is required");
        }

        public int direction() {
            return pixel.mixColor().direction(pixel.speed());
        }
    }
}
