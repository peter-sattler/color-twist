package net.sattler22.crowdtwist;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Pixel color attributes, mixes and breakdowns
 *
 * @author Pete Sattler
 * @version October 2025
 * @since Fall 2018
 */
public enum PixelColor {

    RED("R", true, 1),
    YELLOW("Y", true, -1),
    ORANGE("O", false, 0),
    EMPTY(".", false, 0);

    private final String id;
    private final boolean primary;
    private final int directionalMultiplier;

    private static final Map<PixelColorKeyPair, PixelColor> PRIMARY_COLOR_MIXING_MAP = new HashMap<>();
    static {
        PRIMARY_COLOR_MIXING_MAP.put(new PixelColorKeyPair(RED, YELLOW), ORANGE);
        PRIMARY_COLOR_MIXING_MAP.put(new PixelColorKeyPair(YELLOW, RED), ORANGE);
    }

    private static final Map<PixelColor, List<PixelColor>> COMPOSITE_COLOR_BREAKDOWN_MAP =
            Collections.singletonMap(ORANGE, Arrays.asList(RED, YELLOW));

    PixelColor(String id, boolean primary, int directionalMultiplier) {
        this.id = id;
        this.primary = primary;
        this.directionalMultiplier = directionalMultiplier;
    }

    /**
     * Look-up a pixel color
     *
     * @param id The pixel color unique identifier
     */
    public static Optional<PixelColor> lookup(String id) {
        Objects.requireNonNull(id);
        for (final PixelColor pixelColor : PixelColor.values())
            if (id.equals(pixelColor.id))
                return Optional.of(pixelColor);
        return Optional.empty();
    }

    /**
     * Find primary color breakdown
     *
     * @param pixelColor The pixel color
     * @return One or more primary color components
     */
    public static List<PixelColor> findPrimaryColors(PixelColor pixelColor) {
        requireCompositeColor(pixelColor);
        return COMPOSITE_COLOR_BREAKDOWN_MAP.get(pixelColor);
    }

    /**
     * Get the pixel color unique identifier
     */
    public String id() {
        return id;
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
     * @param mixInColor The pixel color to mix in
     * @return The mixed color
     */
    public PixelColor mix(PixelColor mixInColor) {
        if (this == mixInColor)
            return this;
        if (this == EMPTY)
            return mixInColor;
        if (mixInColor == EMPTY)
            return this;
        if (!this.isPrimary())
            requirePrimaryColor(this);
        if (!mixInColor.isPrimary())
            requirePrimaryColor(mixInColor);
        return PRIMARY_COLOR_MIXING_MAP.get(new PixelColorKeyPair(this, mixInColor));
    }

    private static void requirePrimaryColor(PixelColor pixelColor) {
        if (!pixelColor.isPrimary())
            requireColorType(pixelColor, "primary");
    }

    private static void requireCompositeColor(PixelColor pixelColor) {
        if (pixelColor.isPrimary())
            requireColorType(pixelColor, "composite");
    }

    private static void requireColorType(PixelColor pixelColor, String pixelColorType) {
        Objects.requireNonNull(pixelColor, "Pixel color is required");
        throw new IllegalArgumentException(String.format("%s is not a %s color", pixelColor.name(), pixelColorType));
    }

    /**
     * Pixel color movement condition check
     *
     * @return True if color moves either LEFT or RIGHT
     */
    public boolean hasMovement() {
        return directionalMultiplier != 0;
    }

    /**
     * Get pixel color direction
     *
     * @param speed The number of positions each pixel moves in one unit time (no direction)
     * @return The speed with the color's directional multiplier applied; negative for LEFT and positive for RIGHT
     */
    public int direction(int speed) {
        return speed * directionalMultiplier;
    }

    /**
     * Pixel color key pair
     */
    record PixelColorKeyPair(PixelColor pixelColor1, PixelColor pixelColor2) {
    }
}
