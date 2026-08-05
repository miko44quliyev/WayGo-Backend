package waygo.infrastructure.support;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrafficMathTest {

    @Test
    void congestionFromSpeedShouldClampIntoRange() {
        assertEquals(100, TrafficMath.congestionFromSpeed(0.0));
        assertEquals(0, TrafficMath.congestionFromSpeed(50.0));
        assertEquals(0, TrafficMath.congestionFromSpeed(100.0));
    }

    @Test
    void zScoreShouldReturnZeroWhenDeviationIsTiny() {
        assertEquals(0.0, TrafficMath.zScore(10.0, 30.0, 0.0));
    }

    @Test
    void reliabilityScoreShouldCapAtMaximum() {
        assertEquals(0.95, TrafficMath.reliabilityScore(100, true));
    }
}
