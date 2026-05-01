package net.sattler22.crowdtwist;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void animate_whenSingleRedPixel_thenSuccessful() {
        final List<String> expected = List.of(
                "..R....",
                "....R..",
                "......R",
                "......."
        );
        final Animation animation = new Animation(2, expected.getFirst());
        checkAnimation(10, animation, expected);
    }

    @Test
    void animate_whenFivePixelsPassingThruEachOther_thenSuccessful() {
        final List<String> expected = List.of(
                "RR..YRY",
                ".Y.OR..",
                "Y.....R",
                "......."
        );
        final Animation animation = new Animation(3, expected.getFirst());
        checkAnimation(20, animation, expected);
    }

    @Test
    void animate_whenAllPixelsExitByTimeOne_thenSuccessful() {
        final List<String> expected = List.of(
                "RYRYRYRYRY",
                ".........."
        );
        final Animation animation = new Animation(10, expected.getFirst());
        checkAnimation(30, animation, expected);
    }

    @Test
    void animate_whenInitialPixelsAreEmpty_thenNoAnimation() {
        final List<String> expected = List.of("...");
        final Animation animation = new Animation(10, expected.getFirst());
        checkAnimation(40, animation, expected);
    }

    @Test
    void animate_whenWelcomeToTheGrandIllusion_thenSuccessful() {
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
        final Animation animation = new Animation(1, expected.getFirst());
        checkAnimation(77, animation, expected);  //Styx it to the man!!! ;)
    }

    private static void checkAnimation(int testCaseNbr, Animation animation, List<String> expected) {
        final List<String> actual = animation.animate();
        assertEquals(expected, actual);
        displayAnimation(testCaseNbr, animation.speed(), actual);
    }

    private static void displayAnimation(int testCaseNbr, int speed, List<String> rows) {
        logger.info("==> Animation Results for Test Case #{}", testCaseNbr);
        int time = 0;
        for (final String row : rows)
            logger.info("Test Case: {}, speed={}, time={}: [{}]", testCaseNbr, speed, String.format("%02d", time++), row);
    }
}
