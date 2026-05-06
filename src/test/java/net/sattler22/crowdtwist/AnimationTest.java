package net.sattler22.crowdtwist;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Crowd Twist Animation Challenge 2018 Unit Tests
 *
 * @author Pete Sattler
 * @since Fall 2018
 * @version May 2026
 */
final class AnimationTest {

    private static final Logger logger = LoggerFactory.getLogger(AnimationTest.class);

    @Test
    void newInstance_whenSpeedIsNegative_thenThrowIllegalArgumentException() {
        checkNewInstanceSpeed(-1);
    }

    @Test
    void newInstance_whenSpeedIsZero_thenThrowIllegalArgumentException() {
        checkNewInstanceSpeed(0);
    }

    private static void checkNewInstanceSpeed(int speed) {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            new Animation(speed, "R")
        );
        assertEquals("Speed must be positive", exception.getMessage());
    }

    @Test
    void newInstance_whenInitialStateIsNull_thenThrowNullPointerException() {
        final NullPointerException exception = assertThrows(NullPointerException.class, () ->
            new Animation(5, null)
        );
        assertEquals("Initial state is required", exception.getMessage());
    }

    @Test
    void newInstance_whenHappyPath_thenSuccessful() {
        final int expectedSpeed = 10;
        final String expectedInitialState = "R";
        final Animation animation = new Animation(expectedSpeed, expectedInitialState);
        assertEquals(expectedSpeed, animation.speed());
        assertEquals(expectedInitialState, animation.initialState());
    }

    @Test
    void animate_whenSingleRedPixel_thenSuccessful() {
        final int speed = 2;
        final List<String> expected = List.of(
                "..R....",
                "....R..",
                "......R",
                "......."
        );
        displayAnimation(10, speed, checkAnimation(speed, expected));
    }

    @Test
    void animate_whenFivePixelsPassingThruEachOther_thenSuccessful() {
        final int speed = 3;
        final List<String> expected = List.of(
                "RR..YRY",
                ".Y.OR..",
                "Y.....R",
                "......."
        );
        displayAnimation(20, speed, checkAnimation(speed, expected));
    }

    @Test
    void animate_whenAllPixelsExitByTimeOne_thenSuccessful() {
        final int speed = 10;
        final List<String> expected = List.of(
                "RYRYRYRYRY",
                ".........."
        );
        displayAnimation(30, speed, checkAnimation(speed, expected));
    }

    @Test
    void animate_whenInitialPixelsAreEmpty_thenNoAnimation() {
        final int speed = 10;
        final List<String> expected = List.of("...");
        displayAnimation(40, speed, checkAnimation(speed, expected));
    }

    @Test
    void animate_whenWelcomeToTheGrandIllusion_thenSuccessful() {
        final int speed = 1;
        final List<String> expected = List.of(
                "YRRY.YR.YRR.R.YRRY.",
                "..ORY..O..RR.O..OR.",
                ".Y.OR.Y.R..RO.RY.RR",
                "Y.Y.RO...R.YRRYR..R",
                ".Y..YRR...O..OR.R..",
                "Y..Y..RR.Y.RY.RR.R.",
                "..Y....RO..YR..RR.R",
                ".Y.....YRRY..R..RR.",
                "Y.....Y..OR...R..RR",
                ".....Y..Y.RR...R..R",
                "....Y..Y...RR...R..",
                "...Y..Y.....RR...R.",
                "..Y..Y.......RR...R",
                ".Y..Y.........RR...",
                "Y..Y...........RR..",
                "..Y.............RR.",
                ".Y...............RR",
                "Y.................R",
                "..................."
        );
        displayAnimation(77, speed, checkAnimation(speed, expected));  //Styx it to the man!!! ;)
    }

    private static List<String> checkAnimation(int speed, List<String> expected) {
        final Animation animation = new Animation(speed, expected.getFirst());
        final List<String> actual = animation.animate();
        assertEquals(expected, actual);
        return actual;
    }

    private static void displayAnimation(int testCaseNbr, int speed, List<String> rows) {
        logger.info("==> Test Case #{} Animation Results", testCaseNbr);
        int timeIndex = 0;
        for (final String row : rows)
            logger.info("time={}, speed={}: [{}]", String.format("%02d", timeIndex++), speed, row);
    }
}
