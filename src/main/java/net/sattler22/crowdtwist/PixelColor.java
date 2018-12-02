package net.sattler22.crowdtwist;

import java.io.Serializable;
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
 * @version Fall 2018
 */
public enum PixelColor {

    RED("R", true, 1), YELLOW("Y", true, -1), ORANGE("O", false, 0), EMPTY(".", false, 0);

    private String id;
    private boolean primary;
    private int directionalMultiplier;

    /**
     * Primary color mixing map
     */
    private static final Map<PixelColorKeyPair, PixelColor> PRIMARY_COLOR_MIXING_MAP = new HashMap<>();
    static {
        PRIMARY_COLOR_MIXING_MAP.put(new PixelColorKeyPair(RED, YELLOW), ORANGE);
        PRIMARY_COLOR_MIXING_MAP.put(new PixelColorKeyPair(YELLOW, RED), ORANGE);
    }

    /**
     * Composite color breakdown to primary colors map
     */
    private static final Map<PixelColor, List<PixelColor>> COMPOSITE_COLOR_BREAKDOWN_MAP = Collections.singletonMap(ORANGE, Arrays.asList(RED, YELLOW));

    private PixelColor(String id, boolean primary, int directionalMultiplier) {
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
        for (PixelColor pixelColor : PixelColor.values()) {
            if (id.equals(pixelColor.id)) {
                return Optional.of(pixelColor);
            }
        }
        return Optional.empty();
    }

    /**
     * Breakdown a composite color into its primary color components
     */
    public static List<PixelColor> findPrimaryColors(PixelColor pixelColor) {
        requireCompositeColor(pixelColor);
        return COMPOSITE_COLOR_BREAKDOWN_MAP.get(pixelColor);
    }

    /**
     * Get the pixel color unique identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Primary color check
     * 
     * @return True if the color is a primary color
     */
    public boolean isPrimary() {
        return primary;
    }

    /**
     * Mix together two primary colors
     */
    public PixelColor mix(PixelColor that) {
        if (this == that)
            return this;
        if (this == EMPTY)
            return that;
        if (that == EMPTY)
            return this;
        if (!this.isPrimary())
            requirePrimaryColor(this);
        if (!that.isPrimary())
            requirePrimaryColor(that);
        return PRIMARY_COLOR_MIXING_MAP.get(new PixelColorKeyPair(this, that));
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
        throw new IllegalArgumentException(String.format("%s is not a % color", pixelColor.name(), pixelColorType));
    }

    /**
     * Pixel color movement check
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
    public int getDirection(int speed) {
        return speed * directionalMultiplier;
    }

    /**
     * Pixel color key pair
     */
    static class PixelColorKeyPair implements Serializable {

        private static final long serialVersionUID = 5118273244158259056L;
        private PixelColor pixelColor1;
        private PixelColor pixelColor2;

        public PixelColorKeyPair(PixelColor pixelColor1, PixelColor pixelColor2) {
            super();
            this.pixelColor1 = pixelColor1;
            this.pixelColor2 = pixelColor2;
        }

        @Override
        public int hashCode() {
            return Objects.hash(pixelColor1, pixelColor2);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other)
                return true;
            if (other == null)
                return false;
            if (this.getClass() != other.getClass())
                return false;
            final PixelColorKeyPair that = (PixelColorKeyPair) other;
            return Objects.equals(this.pixelColor1, that.pixelColor1) && Objects.equals(this.pixelColor2, that.pixelColor2);
        }

        @Override
        public String toString() {
            return String.format("%s [pixelColor1=%s, pixelColor2=%s]", getClass().getSimpleName(), pixelColor1, pixelColor2);
        }
    }
}
