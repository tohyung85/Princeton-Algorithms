import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;

public class Percolation { 
 
  private boolean[][] board; 
  private int boardSize; 
  private WeightedQuickUnionUF uf; 
  private WeightedQuickUnionUF ufPerc;
 
  public Percolation(int n) { 
    if (n < 1) { 
      throw new java.lang.IllegalArgumentException("Invalid Arguments"); 
    }
    
    boardSize = n; 
    board = new boolean[n][n]; 
    uf = new WeightedQuickUnionUF(boardSize * boardSize +1); 
    ufPerc = new WeightedQuickUnionUF(boardSize * boardSize +2);
    
    for (int i = 0; i<n; i++) { 
      for (int j = 0; j<n; j++) { 
        board[i][j] = false; 
         
        // Linking top and bottom row to imaginary cell.     
        if (i == 0){ 
          int id = xyToId(i, j); 
          uf.union(0, id); 
          ufPerc.union(0,id);
        }  
        if (i == n-1){ 
          int id = xyToId(i, j); 
          ufPerc.union(n*n+1, id); 
        } 
 
      } 
    } 
  } 
 
  public void open(int row, int col) { 
    validateInputs(row, col); 
    int x = row - 1; 
    int y = col - 1;  
    int id = xyToId(x, y); 
 
    board[x][y] = true;     
 
    if (x-1 >= 0 && board[x-1][y]) { 
      int neighbourId = xyToId(x-1, y); 
      uf.union(id, neighbourId); 
      ufPerc.union(id, neighbourId);
    } 
    if (x+1 < boardSize && board[x+1][y]) { 
      int neighbourId = xyToId(x+1, y); 
      uf.union(id, neighbourId); 
      ufPerc.union(id, neighbourId);
    } 
    if (y-1 >= 0 && board[x][y-1]) { 
      int neighbourId = xyToId(x, y-1); 
      uf.union(id, neighbourId); 
      ufPerc.union(id, neighbourId);
    } 
    if (y+1 < boardSize && board[x][y+1]) { 
      int neighbourId = xyToId(x, y+1); 
      uf.union(id, neighbourId); 
      ufPerc.union(id, neighbourId);
    } 
  } 
 
  public boolean isOpen(int row, int col) { 
    validateInputs(row, col); 
    return board[row-1][col-1]; 
  } 
 
  public boolean isFull(int row, int col) { 
    validateInputs(row, col); 
    int id = xyToId(row-1, col-1); 
    return uf.connected(0, id) && isOpen(row,col); 
  } 
 
  public boolean percolates() { 
      if (boardSize == 1) {
          return isOpen(1, 1);
      }
    return ufPerc.connected(boardSize*boardSize+1, 0); 
  } 
 
  private int xyToId(int x, int y) { 
    // id displaced by 1 place for first cell connecting to all in top row 
    return x * boardSize + y + 1; 
  } 
 
  private void validateInputs(int row, int col) { 
    if (row < 1 || row > boardSize) { 
      throw new IndexOutOfBoundsException("row index is out of bounds"); 
    } 
    if (col < 1 || col > boardSize) { 
      throw new IndexOutOfBoundsException("col index is out of bounds"); 
    } 
  } 
 
  public static void main(String[] args) { 
    Percolation perc = new Percolation(3); 
    StdOut.println("1, 1 is open? " + perc.isOpen(1, 1)); 
    perc.open(1, 1); 
    StdOut.println("1, 1 is open? " + perc.isOpen(1, 1)); 
    StdOut.println("2, 1 is filled? " + perc.isFull(2, 1)); 
    perc.open(2, 1); 
    StdOut.println("2, 1 is filled? " + perc.isFull(2, 1)); 
    StdOut.println("percolate? " + perc.percolates()); 
    perc.open(3, 1); 
    StdOut.println("percolate? " + perc.percolates()); 
    Percolation perc1 = new Percolation(1);
    StdOut.println("1x1 percolate? " + perc1.percolates());
  } 
   
 }