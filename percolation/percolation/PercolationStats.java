import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats { 
  private double[] runResults; 
  private int totalTrials; 
  private int gridSize; 
 
  public PercolationStats(int n, int trials) { 
    validateInputs(n, trials); 
    runResults = new double[trials]; 
    totalTrials = trials; 
    gridSize = n; 
    
    for(int i=0; i<trials; i++) { 
      Percolation perc = new Percolation(n); 
      int count = 0; 
      while(!perc.percolates()) { 
        count++; 
        int row = StdRandom.uniform(1,n+1); 
        int col = StdRandom.uniform(1,n+1); 
        while(perc.isOpen(row, col)){ 
          row = StdRandom.uniform(1,n + 1); 
          col = StdRandom.uniform(1,n + 1);         
        } 
        perc.open(row, col);
      } 
      double perc_threshold = (double)count / (gridSize * gridSize);
      runResults[i] = perc_threshold; 
    } 
  } 
 
  public double mean() { 
    return StdStats.mean(runResults); 
  } 
 
  public double stddev() { 
    return StdStats.stddev(runResults); 
  } 
 
  public double confidenceLo() { 
      if (totalTrials < 2) { return Double.NaN; } 
    return StdStats.mean(runResults) - 1.96*StdStats.stddev(runResults)/Math.sqrt(totalTrials); 
  } 
  public double confidenceHi() { 
      if (totalTrials < 2) { return Double.NaN; } 
    return StdStats.mean(runResults) + 1.96*StdStats.stddev(runResults)/Math.sqrt(totalTrials); 
  } 
 
  private void validateInputs(int n, int trials) { 
    if (n < 1 || trials < 1) { 
      throw new java.lang.IllegalArgumentException("Invalid Arguments"); 
    } 
  } 
 
  public static void main(String[] args) { 
    int gridSize = Integer.parseInt(args[0]); 
    int trials = Integer.parseInt(args[1]); 
 
    PercolationStats percStats = new PercolationStats(gridSize, trials); 
    StdOut.println("mean                    = "+ percStats.mean()); 
    StdOut.println("stdev                   = "+ percStats.stddev()); 
    StdOut.println("95% confidence interval = "+ percStats.confidenceLo() +", "+ percStats.confidenceHi()); 
  } 
}