import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import java.util.*;

public class KdTree {
    private static class Node {
        private Point2D p;
        private Node lb;
        private Node rt;
        private RectHV rect;
        private boolean vertical;
        
        public Node(Point2D point, RectHV r, boolean v) {
            this.p = point;
            this.rect = r;
            this.vertical = v;
        }
    }
    private Node root;
    private int sz;
    public KdTree() {                              // construct an empty set of points 
        sz = 0;
        root = null;
    } 
    public boolean isEmpty() {                     // is the set empty? 
        return root == null;
    }
    public int size() {                         // number of points in the set 
        return sz;
    }
    public void insert(Point2D p) {              // add the point to the set (if it is not already in the set)        
        if(p == null) throwExceptions(1);
        if(contains(p)) return;
        root=insert(root, p, true, new RectHV(0,0,1,1));
        sz++;
    }    
    private Node insert(Node x, Point2D p, boolean compareX, RectHV rect) {

        if(x == null) { // add node at end of branch
            boolean vertical;
            if(compareX) {
                vertical = true;
                rect = new RectHV(p.x(), rect.ymin(), p.x(), rect.ymax());
            } else {
                vertical = false;
                rect = new RectHV(rect.xmin(), p.y(), rect.xmax(), p.y()); 
            }
            return new Node(p, rect, vertical);
        }

        int cmp;
        if(compareX) {
            cmp = lessX(p, x.p);              
            if(cmp < 0) {
                x.lb = insert(x.lb, p, !compareX, new RectHV(rect.xmin(), rect.ymin(), x.rect.xmax(), rect.ymax()));
            } else {
                x.rt = insert(x.rt, p, !compareX, new RectHV(x.rect.xmax(), rect.ymin(), rect.xmax(), rect.ymax()));
            }               
        } else {
            cmp = lessY(p, x.p);    
            if(cmp < 0) {
                x.lb = insert(x.lb, p, !compareX, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.rect.ymax()));
            } else {
                x.rt = insert(x.rt, p, !compareX, new RectHV(rect.xmin(), x.rect.ymax(), rect.xmax(), rect.ymax()));
            }               
        }
        return x;       
    }

    private int lessX(Point2D p, Point2D q) {
        if(p.x() < q.x()) return -1;
        if(p.x() > q.x()) return 1;
        return 0;
    }
    private int lessY(Point2D p, Point2D q) {
        if(p.y() < q.y()) return -1;
        if(p.y() > q.y()) return 1;
        return 0;
    }
    
    public boolean contains(Point2D p) {           // does the set contain point p? 
        if(p == null) throwExceptions(1);
        return contains(root, p, true);
    }
    private boolean contains(Node x, Point2D p, boolean compareX) {
        if(x == null) return false;
        int lessThanX = lessX(p, x.p);
        int lessThanY = lessY(p, x.p);
        if(lessThanX == 0 && lessThanY == 0) return true;
        int cmp;
        if(compareX) {
            cmp = lessThanX;
        } else {
            cmp = lessThanY;
        }        
        if(cmp < 0) return contains(x.lb, p, !compareX);
        else return contains(x.rt, p, !compareX);
    }
    public void draw() {                         // draw all points to standard draw 
        draw(root);
    }
    private void draw(Node x) {
        if(x == null) return;
        draw(x.lb);
        StdDraw.setPenRadius(0.015);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(x.p.x(), x.p.y());    
        StdDraw.setPenRadius(0.005);
        RectHV r = x.rect;
        if(x.vertical) {
            StdDraw.setPenColor(StdDraw.RED); 
        } else {
            StdDraw.setPenColor(StdDraw.BLUE); 
        }
       
        r.draw();
        draw(x.rt);
    }    
    public Iterable<Point2D> range(RectHV rect) {             // all points that are inside the rectangle 
        if(rect == null) throwExceptions(1);
        LinkedList<Point2D> q = new LinkedList<Point2D>();
        range(root, rect, q);
        return q;
    }
    private void range(Node x, RectHV rect, LinkedList<Point2D> q) {
        if(x == null) return;
        
        if(rect.intersects(x.rect)) {
            if(rect.contains(x.p)) q.add(x.p);
            range(x.lb, rect, q);
            range(x.rt, rect, q);
        } else {
            if(x.vertical) {
                if(x.rect.xmax() > rect.xmax()) {
                    range(x.lb, rect, q);
                } else {
                    range(x.rt, rect, q);
                }
            } else {
                if(x.rect.ymax() > rect.ymax()) {
                    range(x.lb, rect, q);
                } else {
                    range(x.rt, rect, q);
                }
            }
        }                
    }

    public Point2D nearest(Point2D p) {             // a nearest neighbor in the set to point p; null if the set is empty 
        if(p == null) throwExceptions(1);
        if(isEmpty()) return null;
        return nearest(root, p, root.p);
    }
    private Point2D nearest(Node x, Point2D p, Point2D champion) {
        if(x == null) return champion;

        Node nextNode;
        Node otherNode;
  
        if(x.vertical) {
            if(p.x() < x.p.x()) {
                nextNode = x.lb;
                otherNode = x.rt;
                                
            } else {
                nextNode = x.rt;
                otherNode = x.lb;
            }
        } else {
            if(p.y() < x.p.y()) {
                nextNode = x.lb;
                otherNode = x.rt;
            } else {
                nextNode = x.rt;
                otherNode = x.lb;
            }
        }
             
        if(x.p.distanceSquaredTo(p) < champion.distanceSquaredTo(p)) {
            champion = x.p;
        }
        
        champion = nearest(nextNode, p, champion);
        if(champion.distanceSquaredTo(p) > x.rect.distanceSquaredTo(p)) {
            champion = nearest(otherNode, p, champion);
        }
        
        return champion;
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

        KdTree t = new KdTree();
        Point2D p1 = new Point2D(0.7,0.2);
        Point2D p2 = new Point2D(0.5,0.4);
        Point2D p3 = new Point2D(0.2,0.3);
        Point2D p4 = new Point2D(0.4,0.7);
        Point2D p5 = new Point2D(0.9,0.6);
        StdOut.println("---Initial---");
        StdOut.println("Set is empty? "+t.isEmpty());
        StdOut.println("Set size "+t.size());
        
        Point2D testPoint = p4;
        StdOut.println("Set contains p1? "+t.contains(testPoint));
        
        t.insert(p1);
        t.insert(p2);
        t.insert(p3);
        t.insert(p4);
        t.insert(p5);
        
        StdOut.println("---After---");
        StdOut.println("Set is empty? "+t.isEmpty());
        StdOut.println("Set size "+t.size());
        StdOut.println("Set contains p1? "+t.contains(testPoint));
        t.draw();
        
        StdOut.println("---Point in Box---");
        RectHV rect = new RectHV(0.55,0.15,0.95,0.65);
        rect.draw();
        for(Point2D p : t.range(rect)){
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.point(p.x(), p.y());
            StdOut.println("("+p.x()+", "+p.y()+")");
        }
        
        Point2D queryPoint = new Point2D(0.62, 0.37);
        Point2D near = t.nearest(queryPoint);
        StdDraw.setPenRadius(0.015);
        StdDraw.setPenColor(StdDraw.ORANGE);
        queryPoint.draw();
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(StdDraw.GREEN);
        queryPoint.drawTo(near);
        StdOut.println("nearest neighbour to ("+p4.x()+", "+p4.y()+ ") is ("+ near.x()+", "+near.y()+")");
        /**/
    }
}