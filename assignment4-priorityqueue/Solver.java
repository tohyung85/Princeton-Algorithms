import java.util.Iterator;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Comparator;

public class Solver {
    private final SearchNode initial;
    private final SearchNode twinInitial;
    private MinPQ<SearchNode> pq;
    private MinPQ<SearchNode> twinPq;
    private boolean solvable;
    private SearchNode solutionNode;
    private final Board solution;
    
    private class SearchNode implements Comparable<SearchNode>{
        Board board;
        SearchNode prev;
        int movesMade;       
        int manhattanPriority;
        int hammingNum;
        
        public int compareTo(SearchNode that) {
            //compare board manhattan number
            if(manhattanPriority < that.manhattanPriority) { return -1; }
            if(manhattanPriority > that.manhattanPriority) { return 1; }
            if(hammingNum < that.hammingNum) { return -1; }
            if(hammingNum > that.hammingNum) { return 1; }
            return 0;
        }
    }
    
    public Solver(Board initB) {          // find a solution to the initial board (using the A* algorithm)
        this.solution = generateSolution(initB.dimension());
        
        solvable = true;
        this.initial = new SearchNode();
        this.initial.board = initB;
        this.initial.prev = null;
        this.initial.movesMade = 0;
        this.initial.manhattanPriority = initial.board.manhattan();
        this.initial.hammingNum = initial.board.hamming();
        
        this.twinInitial = new SearchNode();
        this.twinInitial.board = initB.twin();
        this.twinInitial.prev = null;
        this.twinInitial.movesMade = 0;
        this.twinInitial.manhattanPriority = twinInitial.board.manhattan();
        this.twinInitial.hammingNum = twinInitial.board.hamming();
        
        pq = new MinPQ<SearchNode>();
        twinPq = new MinPQ<SearchNode>();
        pq.insert(initial);
        twinPq.insert(twinInitial);
        
        while(true) {            
            boolean solved = dequeueAndAddNeighbours(pq);
            boolean twinSolved = dequeueAndAddNeighbours(twinPq);
            
            if(solved) { break; }
            if(twinSolved) { 
                solvable = false;
                break;
            }
        }    
    }
    
    public boolean isSolvable() {           // is the initial board solvable?
        return solvable;
    }
    
    public int moves() {                    // min number of moves to solve initial board; -1 if unsolvable
        if(solvable) { return solutionNode.movesMade; }
        return -1;        
    }
    
    public Iterable<Board> solution() {     // sequence of boards in a shortest solution; null if unsolvable
        if(!solvable) { return null; }
        Stack<Board> stack = new Stack<Board>();
        populateSolutions(stack, solutionNode);
        return stack;
    }
    
    private void populateSolutions(Stack<Board> stack, SearchNode node) {
        while(node != null) {
            stack.push(node.board);
            node = node.prev;
        }       
    }
    
    private boolean dequeueAndAddNeighbours(MinPQ<SearchNode> queue) {
        SearchNode minNode = queue.delMin();
        Board b = minNode.board;
        if(checkSolved(b)) { 
            solutionNode = minNode;
            return true;
        } else {
            for(Board board : b.neighbors()) {
                if(minNode.prev == null || !board.equals(minNode.prev.board)){
                    SearchNode neighbour = new SearchNode();
                    neighbour.board = board;
                    neighbour.prev = minNode;
                    neighbour.movesMade = minNode.movesMade + 1;
                    neighbour.manhattanPriority = board.manhattan() + minNode.movesMade + 1;
                    neighbour.hammingNum = board.hamming();
                    queue.insert(neighbour);
                }
            }
        }
        return false;
    }
    
    private boolean checkSolved(Board b) {
        return b.equals(solution);
    }
    
    private Board generateSolution(int size) {
        int[][] solutionArr = new int[size][size];
        int counter=1;
        for(int i = 0 ; i<size; i++) {
            for(int j=0; j<size; j++) {
                solutionArr[i][j] = counter++;
            }
        }
        solutionArr[size -1][size -1] = 0;
        return new Board(solutionArr);
    }
    
    private class Stack<Board> implements Iterable<Board> {
        private Node first;
        private int count = 0;
        
        private class Node {
            Board board;
            Node next;
        }
        
        public void push(Board board) {
            Node oldFirst = first;
            first = new Node();
            first.board = board;
            first.next = oldFirst;
            count++;
        }
        
        public Board pop() {
            Board b = first.board;
            first = first.next;
            count--;
            return b;
        }
        
        public int size() {
            return count;
        }
        
        public boolean isEmpty() {
            return first == null;
        }
        
        public Iterator<Board> iterator() { return new SolutionIterator(); }
        
        private class SolutionIterator implements Iterator<Board> {
            private Node current = first;
            
            public boolean hasNext() {
                return current != null;
            }
            
            public Board next() {
                if(current == null) { throwExceptions(1); }
                Board b = current.board;
                current = current.next;
                return b;
            }
            
            public void remove() {
                throwExceptions(2); //remove not supported
            }
        }
    }
    
    private void throwExceptions(int err) { 
        switch(err) { 
            case 0:
                throw new NullPointerException("Cannot add null item"); 
            case 1: 
                throw new java.util.NoSuchElementException("No more neighbours!");
            case 2: 
                throw new UnsupportedOperationException("Remove operation is not allowed"); 
            default: 
                throw new UnsupportedOperationException("Oops something went wrong"); 
        } 
    }
    
    public static void main(String[] args) {// solve a slider puzzle (given below)
        /* my unit tests
        int[][] arr = new int[3][3];
        int[][] arrSolved = new int[3][3];
        for(int i=0; i<9; i++) {
            int j = i/3;
            arr[j][i%3] = i;
        }
        
        int num = 1;
        for(int i=0; i<9; i++) {
            int j = i/3;
            arrSolved[j][i%3] = num++;
        }
        arrSolved[2][2] = 8;
        arrSolved[2][1] = 5;
        arrSolved[1][1] = 0;
        
        Board board = new Board(arr);
        Solver s = new Solver(board);
        
        Iterable<Board> solIterator = s.solution();
        Iterator<Board> iterator = solIterator.iterator();
        
        while(iterator.hasNext()) {
            System.out.println(iterator.next().toString());
        }
        */
            // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
