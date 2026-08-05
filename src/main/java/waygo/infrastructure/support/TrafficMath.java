package waygo.infrastructure.support;

public final class TrafficMath {

    private TrafficMath() {
    }

    public static int congestionFromSpeed(double speedKmh) {
        return clampInt((int) Math.round(100 - (speedKmh * 2.0)), 0, 100);
    }

    public static double zScore(double value, double mean, double standardDeviation) {
        if (standardDeviation <= 0.0001) {
            return 0.0;
        }
        return (value - mean) / standardDeviation;
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = lat1 - lat2;
        double dLon = lon1 - lon2;
        return Math.sqrt(dLat * dLat + dLon * dLon);
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static int clampInt(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double reliabilityScore(int sampleCount, boolean hasSnapshot) {
        double score = 0.35 + Math.min(sampleCount, 20) * 0.03;
        if (hasSnapshot) {
            score += 0.1;
        }
        return clamp(score, 0.0, 0.95);
    }
}
