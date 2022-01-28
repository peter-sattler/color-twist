package net.sattler22.crowdtwist;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Crowd Twist Animation Challenge 2018 Test Harness
 *
 * @author Pete Sattler
 * @version Fall 2018
 */
final class AnimationTest {

    private static final Logger logger = LoggerFactory.getLogger(AnimationTest.class);

    @Test
    void animateSingleRedPixel() {
        final var testCaseNbr = 1;
        final var speed = 2;
        final var expected = new String[] { "..R....",
                                            "....R..",
                                            "......R",
                                            "......." };
        checkAnimation(testCaseNbr, speed, expected[0], expected);
    }

    @Test
    void animateFivePixelsPassingThruEachOther() {
        final var testCaseNbr = 2;
        final var speed = 3;
        final var expected = new String[] { "RR..YRY",
                                            ".Y.OR..",
                                            "Y.....R",
                                            "......." };
        checkAnimation(testCaseNbr, speed, expected[0], expected);
    }

    @Test
    void animateAllPixelsExitByTimeOne() {
        final var testCaseNbr = 3;
        final var speed = 10;
        final var expected = new String[] { "RYRYRYRYRY",
                                            ".........." };
        checkAnimation(testCaseNbr, speed, expected[0], expected);
    }

    @Test
    void animateInitialPixelsAreEmpty() {
        final var testCaseNbr = 4;
        final var speed = 1;
        final var expected = new String[] { "..." };
        checkAnimation(testCaseNbr, speed, expected[0], expected);
    }

    @Test
    void animate() {
        final var testCaseNbr = 5;
        final var speed = 1;
        final var expected = new String[] { "YRRY.YR.YRR.R.YRRY.",
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
                                            "..................." };
        checkAnimation(testCaseNbr, speed, expected[0], expected);
    }

    private void checkAnimation(int testCaseNbr, int speed, String initialString, String[] expected) {
        final var animation = new Animation(speed, initialString);
        final var actual = animation.animate();
        assertArrayEquals(expected, actual);
        displayAnimation(testCaseNbr, speed, actual);
    }

    private void displayAnimation(int testCaseNbr, int speed, String[] actual) {
        var time = 0;
        logger.info("Test Case: {}, Speed={}", testCaseNbr, speed);
        for(final var s : actual)
            logger.info("Test Case: {}, time={}: [{}]", testCaseNbr, String.format("%02d", time++), s);
    }
}
