import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

  private int expTime;
  private double[] fraction;
  private Percolation p;


  public PercolationStats(int n, int trials){
    if(n<=0||trials<=0)
      throw new IllegalArgumentException();
    expTime = trials;
    fraction = new double[expTime];
    for(int i=0;i<expTime;i++){
      this.p = new Percolation(n);
      int openSites = 0;
      while(!p.percolates()){
        int u = StdRandom.uniform(1,n+1);
        int v = StdRandom.uniform(1,n+1);
        if(!p.isOpen(u,v)){
            p.open(u,v);
            openSites++;
        }
      }
      //StdOut.println(openSites);
      double fration = (double)openSites/(n*n);
      fraction[i] = fration;
    }
  }  // perform trials independent experiments on an n-by-n grid
  public double mean(){
    return StdStats.mean(fraction);
  }                          // sample mean of percolation threshold
  public double stddev()           {
    return StdStats.stddev(fraction);
  }             // sample standard deviation of percolation threshold
  public double confidenceLo()       {
      return mean()-(1.96*stddev())/Math.sqrt(expTime);
  }           // low  endpoint of 95% confidence interval
  public double confidenceHi()      {
    return mean()+(1.96*stddev())/Math.sqrt(expTime);
  }            // high endpoint of 95% confidence interval

  public static void main(String[] args){
    int N = Integer.parseInt(args[0]);
    int T = Integer.parseInt(args[1]);
    PercolationStats ps = new PercolationStats(N, T);

    String confidence = ps.confidenceLo() + ", " + ps.confidenceHi();
    StdOut.println("mean                    = " + ps.mean());
    StdOut.println("stddev                  = " + ps.stddev());
    StdOut.println("95% confidence interval = " + confidence);
  }        // test client (described below)
}
