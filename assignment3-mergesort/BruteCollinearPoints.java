import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;
import java.util.*;

public class BruteCollinearPoints {
    private int numSegments;
    private LineSegment[] lineSegments;
    private Point[] points;
    
    public BruteCollinearPoints(Point[] initPoints) {   // finds all line segments containing 4 points
        points = new Point[initPoints.length];
        for(int i=0; i< initPoints.length; i++) {
            points[i] = initPoints[i];
        }
        if(points == null) { throwException(0); }
        
        Arrays.sort(points);
        checkInput(points);
        this.numSegments = 0;
        this.lineSegments = new LineSegment[points.length - 1];
        for(int i=0; i < points.length - 3 ; i++) {
            for(int j=i+1; j<points.length -2; j++) {
                for(int k=j+1; k<points.length -1; k++) {
                    for(int l=k+1; l<points.length; l++) {
                        double firstSlope = points[i].slopeTo(points[j]);
                        if(points[i].slopeTo(points[k]) != firstSlope || points[i].slopeTo(points[l]) != firstSlope) {
                            continue;
                        }
                        LineSegment lineSeg = new LineSegment(points[i], points[l]);
                        this.lineSegments[numSegments++] = lineSeg;        
                    }
                }
            }            
        }
    }
    
    public int numberOfSegments() {       // the number of line segments
        return numSegments;
    }
    
    public LineSegment[] segments() {                // the line segments
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
    
    public static void main(String[] args) {    // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        
        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}