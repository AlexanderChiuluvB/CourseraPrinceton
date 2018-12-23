import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private boolean[]grid;
  private int gridNum;
  private int N;
  private WeightedQuickUnionUF uf;
  private WeightedQuickUnionUF ufNoBottom;
  public Percolation(int n){
    if(n<=0)
      throw new IllegalArgumentException();
    gridNum = n*n;
    this.N = n;
    grid = new boolean[gridNum+2];
    //parent = new int[gridNum+1];
    uf = new WeightedQuickUnionUF(gridNum+2);
    ufNoBottom = new WeightedQuickUnionUF(gridNum+1);
    for(int i=0;i<gridNum;i++){
      this.grid[i] = false;
    }
  }                // create n-by-n grid, with all sites blocked

  private void validate(int row,int col){
      if(row<=0||col<=0||row>N||col>N){
        throw new IllegalArgumentException();
      }
  }

  private int gridIndex(int row,int col){
    return col+(row-1)*N;
  }
  public    void open(int row, int col){
    validate(row,col);
    this.grid[gridIndex(row,col)] = true;
    if(row==1) {
      uf.union(gridIndex(row, col), 0);
      ufNoBottom.union(gridIndex(row, col), 0);
    }
    if(row==N)
        uf.union(gridIndex(row,col),gridNum+1);
    if(row>1&&isOpen(row-1,col)){
      uf.union(gridIndex(row-1,col),gridIndex(row, col));
      ufNoBottom.union(gridIndex(row-1,col),gridIndex(row, col));
    }
    if(row+1<=N&&isOpen(row+1,col)){
      uf.union(gridIndex(row+1,col),gridIndex(row, col));
      ufNoBottom.union(gridIndex(row+1,col),gridIndex(row, col));
    }
    if(col>1&&isOpen(row,col-1)){
      uf.union(gridIndex(row,col-1),gridIndex(row, col));
      ufNoBottom.union(gridIndex(row,col-1),gridIndex(row, col));
    }
    if(col+1<=N&&isOpen(row,col+1)){
      uf.union(gridIndex(row,col+1),gridIndex(row, col));
      ufNoBottom.union(gridIndex(row,col+1),gridIndex(row, col));
    }

  }    // open site (row, col) if it is not open already

  public boolean isOpen(int row, int col){
    validate(row,col);
    return grid[gridIndex(row,col)];
  }

  // is site (row, col) open?
  public boolean isFull(int row, int col){
    validate(row,col);
    return ufNoBottom.connected(0,gridIndex(row,col));

  }  // is site (row, col) full?
  public int numberOfOpenSites(){
    int res = 0;
    for(int i=1;i<=gridNum;i++){
      if(grid[i])
        res++;
    }
    return res;
  }       // number of open sites
  public boolean percolates(){
      return uf.connected(0,gridNum+1);
  }             // does the system percolate?

  public static void main(String[] args){

  }               // test client (optional)
}