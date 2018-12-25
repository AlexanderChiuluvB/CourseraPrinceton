import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.IllformedLocaleException;
import java.util.Set;
import java.util.TreeSet;

public class PointSET {

  private static class Node {
    private Point2D p;      // the point
    private RectHV rect;    // the axis-aligned rectangle corresponding to this node
    private Node lb;        // the left/bottom subtree
    private Node rt;        // the right/top subtree
  }

  private TreeSet<Point2D> st;
  //private int size;
  public         PointSET(){
    st = new TreeSet<Point2D>();
    //size = 0;
  }                               // construct an empty set of points
  public           boolean isEmpty(){
    return st.isEmpty();
  }                      // is the set empty?
  public               int size(){
    return st.size();
  }                         // number of points in the set
  public              void insert(Point2D p){
    if(p==null)
      throw new IllegalArgumentException();
    st.add(p);
  }              // add the point to the set (if it is not already in the set)
  public           boolean contains(Point2D p){
    if(p==null){
      throw  new IllegalArgumentException();
    }
    return st.contains(p);
  }            // does the set contain point p?

  public              void draw(){
      for(Point2D p:this.st){
        StdDraw.point(p.x(),p.y());
      }
  }                         // draw all points to standard draw


  public Iterable<Point2D> range(RectHV rect){

    if(rect==null){
      throw new IllegalArgumentException();
    }

    Set<Point2D> res = new TreeSet<Point2D>();
    for(Point2D p:this.st){
      if(rect.contains(p)){
        res.add(p);
      }
    }
    return res;
  }                                                   // all points that are inside the rectangle (or on the boundary)


  public           Point2D nearest(Point2D p) {
    if(p==null){
      throw new IllegalArgumentException();
    }
    if(isEmpty())
      return null;
    Point2D res = null;
    double minDis = Double.POSITIVE_INFINITY;
    for(Point2D pp:st){
      if(pp.distanceTo(p)<minDis){
        minDis = pp.distanceTo(p);
        res = pp;
      }
    }
    return res;

  }            // a nearest neighbor in the set to point p; null if the set is empty

  public static void main(String[] args){




  }                  // unit testing of the methods (optional)
}