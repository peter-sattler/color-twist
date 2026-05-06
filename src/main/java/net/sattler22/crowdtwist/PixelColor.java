package net.sattler22.crowdtwist;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Pixel color attributes, definitions and breakdowns
 *
 * @author Pete Sattler
 * @since Fall 2018
 * @version May 2026
 */
public enum PixelColor {

    RED("R", true, 1),
    YELLOW("Y", true, -1),
    ORANGE("O", false, 0),
    EMPTY(".", false, 0);

    private final String id;
    private final boolean primary;
    private final int directionalMultiplier;

    private static final Map<String, PixelColor> COLORS_BY_ID =
            Arrays.stream(values())
                    .collect(Collectors.toUnmodifiableMap(PixelColor::id, color -> color));

    PixelColor(String id, boolean primary, int directionalMultiplier) {
        this.id = id;
        this.primary = primary;
        this.directionalMultiplier = directionalMultiplier;
    }

    private static final Map<PixelColorPair, PixelColor> PRIMARY_COLORS = Map.of(
            new PixelColorPair(RED, YELLOW), ORANGE,
            new PixelColorPair(YELLOW, RED), ORANGE
    );

    private static final Map<PixelColor, List<PixelColor>> COMPOSITE_COLOR_BREAKDOWN = Map.of(
            ORANGE, List.of(RED, YELLOW)
    );

    /**
     * Pixel color pair
     *
     * @param first The first pixel color
     * @param second The second pixel color
     */
    private record PixelColorPair(PixelColor first, PixelColor second) {
    }

    /**
     * Look-up a pixel color by its identifier
     *
     * @param id The matching color unique identifier (if present)
     */
    public static Optional<PixelColor> lookup(String id) {
        Objects.requireNonNull(id, "ID is required");
        return Optional.ofNullable(COLORS_BY_ID.get(id));
    }

    /**
     * Find primary color breakdown for a composite color
     *
     * @param compositeColor The composite color
     * @return One or more primary color components
     */
    public static List<PixelColor> findPrimaryColors(PixelColor compositeColor) {
        Objects.requireNonNull(compositeColor, "Composite color is required");
        if (compositeColor.isEmpty() || compositeColor.isPrimary())
            throw new IllegalArgumentException("%s is not a composite color".formatted(compositeColor.name()));
        return COMPOSITE_COLOR_BREAKDOWN.get(compositeColor);
    }

    /**
     * Get the pixel color unique identifier
     */
    public String id() {
        return id;
    }

    /**
     * Empty pixel condition check
     *
     * @return True if the pixel is a empty. Otherwise, returns false.
     */
    public boolean isEmpty() {
        return this == EMPTY;
    }

    /**
     * Primary color condition check
     *
     * @return True if the color is a primary color. Otherwise, returns false.
     */
    public boolean isPrimary() {
        return primary;
    }

    /**
     * Mix two primary colors together
     *
     * @param other The primary color to mix in
     * @return The mixed color
     */
    public PixelColor mix(PixelColor other) {
        Objects.requireNonNull(other, "Mix in color is required");
        if (this == other)
            return this;
        if (this == EMPTY)
            return other;
        if (other == EMPTY)
            return this;
        requirePrimaryColor(this);
        requirePrimaryColor(other);
        final PixelColor mixedColor = PRIMARY_COLORS.get(new PixelColorPair(this, other));
        if (mixedColor == null)
            throw new IllegalStateException("No mix defined for %s and %s".formatted(this, other));
        return mixedColor;
    }

    private static void requirePrimaryColor(PixelColor pixelColor) {
        Objects.requireNonNull(pixelColor, "Pixel color is required");
        if (!pixelColor.isPrimary())
            throw new IllegalArgumentException("%s is not a primary color".formatted(pixelColor.name()));
    }

    /**
     * Pixel movement condition check
     *
     * @return True if color moves either LEFT or RIGHT
     */
    public boolean hasMovement() {
        return directionalMultiplier != 0;
    }

    /**
     * Applies pixel direction to the given speed
     *
     * @param speed The number of positions each pixel moves in one unit time (no direction)
     * @return The speed with the color's directional multiplier applied; negative for LEFT and positive for RIGHT
     */
    public int direction(int speed) {
        return speed * directionalMultiplier;
    }
}
