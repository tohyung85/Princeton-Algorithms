import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;
import java.util.*;

public class PointSET {
    private SET<Point2D> set;
    
    public PointSET() {                              // construct an empty set of points 
        set = new SET<Point2D>();
    } 
    public boolean isEmpty() {                     // is the set empty? 
        return set.isEmpty();
    }
    public int size() {                         // number of points in the set 
        return set.size();
    }
    public void insert(Point2D p) {              // add the point to the set (if it is not already in the set)
        if(p == null) throwExceptions(1);
        set.add(p);
    }
    public boolean contains(Point2D p) {           // does the set contain point p? 
        if(p == null) throwExceptions(1);        
        return set.contains(p);
    }
    public void draw() {                         // draw all points to standard draw 
        for(Point2D p : set) {
            StdDraw.setPenRadius(0.03);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.point(p.x(), p.y());
        }
    }
    public Iterable<Point2D> range(RectHV rect) {             // all points that are inside the rectangle 
       if(rect == null) throwExceptions(1);
        LinkedList<Point2D> q = new LinkedList<Point2D>();
        for(Point2D p : set) {
            if(rect.contains(p)) q.add(p);
        }
        return q;
    }
    public Point2D nearest(Point2D p) {             // a nearest neighbor in the set to point p; null if the set is empty 
       if(p == null) throwExceptions(1);
       if(isEmpty()) return null;
        Point2D nearestNeighbour = set.min();
        Double nearestNeighbourDist = p.distanceSquaredTo(nearestNeighbour);
        for(Point2D point : set) {
            Double distance = p.distanceSquaredTo(point);
            if(distance < nearestNeighbourDist) {
                nearestNeighbour = point;
                nearestNeighbourDist = distance;
            }
            //StdDraw.line(p.x(), p.y(), point.x(), point.y());
        }
        return nearestNeighbour;
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
    public static void main(String[] args) {                 // unit testing of the methods (optional) 
/*
        PointSET ps = new PointSET();
        Point2D p1 = new Point2D(0.5,0.5);
        Point2D p5 = new Point2D(0.3, 0.5);
        Point2D p2 = new Point2D(0.1,0.1);
        Point2D p3 = new Point2D(0.4,0.4);
        Point2D p4 = new Point2D(0.45,0.4);
        StdOut.println("---Initial---");
        StdOut.println("Set is empty? "+ps.isEmpty());
        StdOut.println("Set size "+ps.size());
        StdOut.println("Set contains p1? "+ps.contains(p1));
        
        ps.insert(p1);
        ps.insert(p2);
        ps.insert(p3);
        
        StdOut.println("---After---");
        StdOut.println("Set is empty? "+ps.isEmpty());
        StdOut.println("Set size "+ps.size());
        StdOut.println("Set contains p1? "+ps.contains(p1));
        
        RectHV rect = new RectHV(0.1,0.1,0.6,0.6);
        rect.draw();
        ps.draw();
        Iterable<Point2D> r = ps.range(rect);
//        StdOut.println("iterable size: "+ r.size());
        for(Point2D p : ps.range(rect)){
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.point(p.x(), p.y());
        }
        StdOut.println("comparing: " + p5.equals(p1));
        
        Point2D near = ps.nearest(p4);
        StdOut.println("nearest neighbour to ("+p4.x()+", "+p4.y()+ ") is ("+ near.x()+", "+near.y()+")"); 
        */
    }
}