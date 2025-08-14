public class MinMaxMetrics {

    // Add all necessary member variables
    private volatile long minMetric;
    private volatile long maxMetric;

    /**
     * Initializes all member variables
     */
    public MinMaxMetrics() {
        // Add code here
        this.minMetric = Long.MAX_VALUE;
        this.maxMetric = Long.MIN_VALUE;
    }

    /**
     * Adds a new sample to our metrics.
     */
    public synchronized void addSample(long newSample) {
        // Add code here
        synchronized (this) {
            this.minMetric = Math.min(this.minMetric, newSample);
            this.maxMetric = Math.max(this.maxMetric, newSample);
        }
    }

    /**
     * Returns the smallest sample we've seen so far.
     */
    public long getMin() {
        // Add code here
        return this.minMetric;
    }

    /**
     * Returns the biggest sample we've seen so far.
     */
    public long getMax() {
        // Add code here
        return this.maxMetric;
    }
}
