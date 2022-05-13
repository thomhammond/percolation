import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private Percolation percolation;
    private double[] samples;

    public PercolationStats(int n, int trials) {
        this.samples = new double[trials];

        for (int i = 0; i < trials; i++) {
            this.percolation = new Percolation(n);
            while (!percolation.percolates()) {
                percolation.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
            }
            samples[i] = (double) percolation.numberOfOpenSites() / percolation.getSize();
        }
    }

    public double mean() {
        return StdStats.mean(samples);
    }

    public double stddev() {
        return StdStats.stddev(samples);
    }

    public double confidenceLo() {
        return mean() - (1.96 * stddev()) / Math.sqrt(samples.length);
    }

    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(samples.length);
    }

    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(200, 100);

        System.out.println("mean: " + stats.mean());
        System.out.println("StdDev: " + stats.stddev());
        System.out.println("confidenceLo: " + stats.confidenceLo());
        System.out.println("confidenceHi: " + stats.confidenceHi());
    }
}
