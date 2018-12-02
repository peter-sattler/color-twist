package net.sattler22;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sattler22.crowdtwist.Animation;

/**
 * Crowd Twist Animation Challenge 2018 Test Harness
 * 
 * @author Pete Sattler
 * @version Fall 2018
 */
public final class AnimationTestHarness {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnimationTestHarness.class);

    @Test
    public void animateSingleRedPixel() {
        final int testCaseNbr = 1;
        final int speed = 2;
        final String[] expected = new String[] { "..R....", 
                                                 "....R..", 
                                                 "......R", 
                                                 "......." };
        checkAnimation(testCaseNbr, speed, expected[0], expected);
    }

    @Test
    public void animateFivePixelsPassingThruEachOther() {
        final int testCaseNbr = 2;
        final int speed = 3;
        final String[] expected = new String[] { "RR..YRY", 
                                                 ".Y.OR..", 
                                                 "Y.....R", 
                                                 "......." };
        checkAnimation(testCaseNbr, speed, expected[0], expected);
    }

    @Test
    public void animateAllPixelsExitByTimeOne() {
        final int testCaseNbr = 3;
        final int speed = 10;
        final String[] expected = new String[] { "RYRYRYRYRY", 
                                                 ".........." };
        checkAnimation(testCaseNbr, speed, expected[0], expected);
    }

    @Test
    public void animateInitialPixelsAreEmpty() {
        final int testCaseNbr = 4;
        final int speed = 1;
        final String[] expected = new String[] { "..." };
        checkAnimation(testCaseNbr, speed, expected[0], expected);
    }
    
    @Test
    public void animate() {
        final int testCaseNbr = 5;
        final int speed = 1;
        final String[] expected = new String[] { "YRRY.YR.YRR.R.YRRY.",
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
        final Animation animation = new Animation(speed, initialString);
        final String[] actual = animation.animate();
        assertArrayEquals(expected, actual);
        displayAnimation(testCaseNbr, speed, actual);
    }

    private void displayAnimation(int testCaseNbr, int speed, String[] actual) {
        int time = 0;
        LOGGER.info("Test Case: {}, Speed={}", testCaseNbr, speed);
        for(String s : actual) {
            LOGGER.info("Test Case: {}, time={}: [{}]", testCaseNbr, String.format("%02d", time++), s);
        }
    }
}
