import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;
import java.util.*;


public class FastCollinearPoints {
    private int numSegments;
    private LineSegment[] lineSegments;
    private Point[] points;
    
    public FastCollinearPoints(Point[] initPoints) {    // finds all line segments containing 4 or more points
        points = new Point[initPoints.length];
        for(int i=0; i< initPoints.length; i++) {
            points[i] = initPoints[i];
        }
        if(points == null) { throwException(0); }
        
        Arrays.sort(points);
        
        checkInput(points);
        
        numSegments = 0;
        lineSegments = new LineSegment[points.length];
        for(int i=0; i<points.length; i++) {
            if(points.length < 2) { break; }
            Arrays.sort(points, points[i].slopeOrder());
            int numberCollinear = 1;
            Point op = points[0];
            Comparator<Point> pc = op.slopeOrder();
            Point minPoint = points[1];
            
//            StdOut.println("---Points---");
//            for(int p=0; p<points.length; p++) {
//                StdOut.println("point " + p + ": "+points[p]);
//            }
            
            for(int j=1; j<points.length; j++) {
                if(op.slopeTo(points[j-1]) == op.slopeTo(points[j])) {
                    numberCollinear++;
//                    StdOut.println("point "+points[j] + " and " + points[j-1]+ "are equal order");
//                    StdOut.println("number collinear: "+numberCollinear);
                }
                if(op.slopeTo(points[j-1]) != op.slopeTo(points[j]) || j == points.length -1) {
                    if(numberCollinear > 2) {
                        if(op.compareTo(minPoint) < 0) {
                            Point end = points[j-1];
                            if( j == points.length -1 && op.slopeTo(points[j-1]) == op.slopeTo(points[j])) { end = points[j]; }
                            LineSegment line = new LineSegment(op, end);
                            if(numSegments == lineSegments.length - 1) {
                                resizeArray(lineSegments);
                            }
                            lineSegments[numSegments++] = line;
                        }
                    }
                    numberCollinear = 1;                    
                    minPoint = points[j];
                }
            }
            
            Arrays.sort(points); // reset to sort by position to maintain stability
        }
    }
    
    private void resizeArray(LineSegment[] ls) {
        LineSegment[] expandedArray = new LineSegment[ls.length * 2];
        for(int i = 0; i< ls.length; i++) {
            expandedArray[i] = ls[i];
        }
        lineSegments = expandedArray;
    }
    
    public int numberOfSegments() {        // the number of line segments
        return numSegments;
    }
    
    public LineSegment[] segments() {               // the line segments
        int num = numberOfSegments();
        LineSegment[] ls = new LineSegment[num];
        for(int i = 0; i< num; i++) {
            ls[i] = lineSegments[i];
        }
        return ls;
    }
    
    private void checkInput(Point[] points) {
        if(points[0] == null) { throwException(0); }
        for(int i=1; i<points.length; i++) {
            if(points[i].slopeTo(points[i-1]) == Double.NEGATIVE_INFINITY) { throwException(1); }
        }
    }        
    
    private void throwException(int i) { 
        switch (i) { 
            case 0: 
                throw new NullPointerException("null array or null points not allowed");
            case 1: 
                throw new IllegalArgumentException("No repeat points allowed!");
            default: 
                throw new UnsupportedOperationException("Oops something went wrong"); 
        }
    }    
    
    public static void main(String[] args) {
        
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        
        Arrays.sort(points);

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        
        /*
        Point p1 = new Point(0,1);
        Point p2 = new Point(2,5);
        Point op = new Point(1,1);
        Point p3 = new Point(5,2);
        Point p4 = new Point(2,7);
        Point[] points = new Point[5];
        points[0] = p1;
        points[1] = p2;
        points[2] = op;   
        points[3] = p3; 
        points[4] = p4; 
        
        for(int i=0; i<5; i++) {
            StdOut.println(points[i].toString());
        }
        
        Arrays.sort(points);
        StdOut.println("---Sorted---");
        for(int i=0; i<5; i++) {
            StdOut.println(points[i].toString());
        }
        
        FastCollinearPoints fcp = new FastCollinearPoints(points);
        StdOut.println("---Sorted By Order---");
        for(int i=0; i<5; i++) {
            StdOut.println(points[i].toString());
        }
        */
    }
}