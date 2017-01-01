import java.util.Iterator;

public class Board {
    private final int[] board;
    private final int size;
    private final int blankIndex;
    private int manhattanNo; // cache?
    
    public Board(int[][] blocks) {// construct a board from an n-by-n array of blocks (where blocks[i][j] = block in row i, column j)
        this.size = blocks.length;
        this.board = new int[size * size];
        int boardCounter = 0;
        int blank = 0;
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                if(blocks[i][j] == 0) { blank = boardCounter; }
                board[boardCounter] = blocks[i][j];
                boardCounter++;
            }
        }
        this.blankIndex = blank;
    }
    
    public int dimension() {                // board dimension n
        return size;
    }
    
    public int hamming() {                   // number of blocks out of place
        int numOutOfPos = 0;
        for(int i=0; i<size*size; i++) {
            if(board[i] != i+1 && board[i] != 0) { numOutOfPos++; }
        }
        return numOutOfPos;
    }
    
    public int manhattan() {               // sum of Manhattan distances between blocks and goal
        int sum = 0;
        for(int i=0; i< size*size; i++) {
            if(board[i] == 0) { continue; }
            int reqRow = (board[i] - 1)/size;
            int reqCol = (board[i] - 1)%size;
            int currRow = i/size;
            int currCol = i%size;
            sum += Math.abs(reqRow - currRow) + Math.abs(reqCol - currCol);
        }
        return sum;
    }
    
    public boolean isGoal() {             // is this board the goal board?
        return hamming() == 0;
    }
    
    public Board twin() {                   // a board that is obtained by exchanging any pair of blocks
        int i = 0;
        while(board[i] == 0 || board[i+1] == 0) {
            i++;
        }        
        Board twinBoard = newBoardSwapping(i, i+1); // swap positions i and i+1 on existing board
        return twinBoard;
    }
    
    public boolean equals(Object y) {       // does this board equal y?
        if(y == null) { return false; }
        return toString().equals(y.toString());
    }
    
    public Iterable<Board> neighbors() {    // all neighboring boards
        Stack<Board> neighbourStack = new Stack<Board>();
        populateNeighbours(neighbourStack);
        return neighbourStack;
    }
    
    public String toString() {              // string representation of this board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        s.append(size);
        for (int i=0; i<size*size; i++) {
            if(i%size == 0) { s.append("\n"); }
            s.append(String.format("%2d ", board[i]));                       
        }
        return s.toString();
    }          
    
    private Board newBoardSwapping(int p, int q) {
        int[][] newArr = new int[size][size]; // create new array for board creation
        for(int j=0; j<size*size; j++){ //populate new array with changed board
            newArr[j/size][j%size] = board[j];
        }
        
        int temp = newArr[p/size][p%size];
        newArr[p/size][p%size] = newArr[q/size][q%size];
        newArr[q/size][q%size] = temp;
        
        Board newBoard = new Board(newArr);       
        return newBoard;
    }
    
    private void populateNeighbours(Stack<Board> neighbours) {
        int blankRow = (blankIndex)/size;
        int blankCol = (blankIndex)%size;
        
        if(blankRow != 0) { // if blank is not on top row
            int indexAboveBlank = blankIndex - size;
            Board neighbour = newBoardSwapping(indexAboveBlank, blankIndex);
            neighbours.push(neighbour);
        }
        
        if(blankRow != size-1) { // if blank is not on bottom row
            int indexBelowBlank = blankIndex + size;
            Board neighbour = newBoardSwapping(indexBelowBlank, blankIndex);
            neighbours.push(neighbour);
        }
        
        if(blankCol != 0) { // if blank is not on leftmost col
            int indexLeftOfBlank = blankIndex - 1;
            Board neighbour = newBoardSwapping(indexLeftOfBlank, blankIndex);
            neighbours.push(neighbour);
        }
        
        if(blankCol != size-1) { // if blank is not on rightmost col
            int indexRightOfBlank = blankIndex + 1;
            Board neighbour = newBoardSwapping(indexRightOfBlank, blankIndex);
            neighbours.push(neighbour);
        }
    }
        
    private class Stack<Board> implements Iterable<Board>{
        private Node first;
        
        private class Node {
            Board board;
            Node next; 
        }
        
        public Iterator<Board> iterator() { return new NeighbourIterator(); }
        
        public void push(Board b) {
            Node oldFirst = first;
            first = new Node();
            first.board = b;
            first.next = oldFirst;
        }
        
        public Board pop() {
            if(isEmpty()) { throwExceptions(1); }
            Board b = first.board;
            first = first.next;
            return b;
        }
        
        public boolean isEmpty() {
            return first == null;
        }
        
        public Board peek() {
            if(isEmpty()) { throwExceptions(1); }
            Board b = first.board;
            return b;
        }
        
        private class NeighbourIterator implements Iterator<Board> {                        
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
    
    public static void main(String[] args) {// unit tests (not graded)
        /*
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
        arrSolved[2][2] = 0;
                
        Board board = new Board(arrSolved);
    
        Board twinBoard = board.twin();
        System.out.println(board.toString());
        
        System.out.println("Is twin board equal to board? " + board.equals(board));
        
        System.out.println("hamming priority: " + board.hamming());
        System.out.println("manhattan priority: " + board.manhattan());
        System.out.println("board is goal: " + board.isGoal());
        
        System.out.println("---Twin---");
        System.out.println(twinBoard.toString());
        System.out.println("blank is at index: " + twinBoard.blankIndex);
        
        System.out.println("---Parent---");
        System.out.println(board.toString());
        System.out.println("blank is at index: " + board.blankIndex);
        
        Iterable<Board> stack = board.neighbors();
        Iterator<Board> neighbourIterator = stack.iterator();
        
        System.out.println("---Neighbours---");
        
        while(neighbourIterator.hasNext()){
            System.out.println(neighbourIterator.next().toString());
        }
        */
    }
}
