package net.sattler22.crowdtwist;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Crowd Twist Animation Challenge 2018 Unit Tests
 *
 * @author Pete Sattler
 * @version October 2025
 * @since Fall 2018
 */
final class AnimationTest {

    private static final Logger logger = LoggerFactory.getLogger(AnimationTest.class);

    @Test
    void animateSingleRedPixel() {
        final Animation animation = new Animation(2, "..R....");
        final String[] expected = new String[] {
                animation.initialState(),
                "....R..",
                "......R",
                "......."
        };
        checkAnimation(10, animation, expected);
    }

    @Test
    void animateFivePixelsPassingThruEachOther() {
        final Animation animation = new Animation(3, "RR..YRY");
        final String[]  expected = new String[] {
                animation.initialState(),
                ".Y.OR..",
                "Y.....R",
                "......."
        };
        checkAnimation(20, animation, expected);
    }

    @Test
    void animateAllPixelsExitByTimeOne() {
        final Animation animation = new Animation(10, "RYRYRYRYRY");
        final String[]  expected = new String[] {
                animation.initialState(),
                ".........."
        };
        checkAnimation(30, animation, expected);
    }

    @Test
    void animateInitialPixelsAreEmpty() {
        final Animation animation = new Animation(10, "...");
        final String[]  expected = new String[] {
                animation.initialState()
        };
        checkAnimation(40, animation, expected);
    }

    @Test
    void animate() {
        final Animation animation = new Animation(1, "YRRY.YR.YRR.R.YRRY.");
        final String[]  expected = new String[] {
                animation.initialState(),
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
        };
        checkAnimation(50, animation, expected);
    }

    private static void checkAnimation(int testCaseNbr, Animation animation, String[] expected) {
        final String[] actual = animation.animate();
        assertArrayEquals(expected, actual);
        displayAnimation(testCaseNbr, animation.speed(), actual);
    }

    private static void displayAnimation(int testCaseNbr, int speed, String[] rows) {
        logger.info("==> Animation Results for Test Case #{}", testCaseNbr);
        int time = 0;
        for(final String row : rows)
            logger.info("Test Case: {}, speed={}, time={}: [{}]", testCaseNbr, speed, String.format("%02d", time++), row);
    }
}
