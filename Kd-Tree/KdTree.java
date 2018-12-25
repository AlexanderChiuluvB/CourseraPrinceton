import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.util.IllformedLocaleException;
import java.util.TreeSet;

public class KdTree {


  private static class KdNode{

      private  KdNode leftNode;
      private  KdNode rightNode;
      private  final boolean isVertical;
      private  final double x;
      private  final double y;
      public KdNode(KdNode leftNode,KdNode rightNode,boolean isVertical,double x,double y){
          this.leftNode = leftNode;
          this.rightNode = rightNode;
          this.isVertical = isVertical;
          this.x = x;
          this.y = y;
      }
  }

  private KdNode rootNode;
  private int size;
  private static final RectHV CONTAINER = new RectHV(0,0,1,1);


  public         KdTree(){
      this.size =0;
      this.rootNode = null;
  }                               // construct an empty set of points
  public           boolean isEmpty(){
    return this.size==0;
  }                      // is the set empty?
  public               int size(){
    return this.size;
  }                         // number of points in the set
  public              void insert(Point2D p){
    if(p==null)
      throw new IllegalArgumentException();
    this.rootNode = insert(rootNode,p,true);
  }              // add the point to the set (if it is not already in the set)

  private KdNode insert(KdNode root,Point2D p,boolean isVertical){

    if(root==null){
        size++;
        return new KdNode(null,null,isVertical,p.x(),p.y());
    }

    //already contain
    if(root.x==p.x()&&root.y==p.y()){
      return root;
    }

    //如果是垂直的，比较x，如果是平行的，比较y
    if(root.isVertical&&p.x()<root.x||!root.isVertical&&p.y()<root.y){
      //奇数层都是vertical 偶数层都是horizontal
      root.leftNode = insert(root.leftNode,p,!isVertical);
    }else{
      root.rightNode = insert(root.rightNode,p,!isVertical);
    }
    return root;
  }

  public           boolean contains(Point2D p){
    if(p==null){
      throw new IllegalArgumentException();
    }
    return contains(rootNode,p.x(),p.y());
  }            // does the set contain point p?

  private boolean contains(KdNode root,double x,double y) {

    if (root == null)
      return false;
    if (root.x == x && root.y == y)
      return true;
    if (root.isVertical && x < root.x || !root.isVertical && y < root.y) {
      return contains(root.leftNode, x, y);
    } else
      return contains(root.rightNode, x, y);
  }

  // draw all of the points to standard draw
  public void draw() {
    StdDraw.setScale(0, 1);

    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius();
    CONTAINER.draw();

    draw(rootNode, CONTAINER);
  }
  private void draw(KdNode root,RectHV rect){

    if(root==null)
      return;
    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius(0.01);
    new Point2D(root.x,root.y).draw();

    Point2D min,max;
    if(root.isVertical){
      StdDraw.setPenColor(StdDraw.RED);
      min = new Point2D(root.x,rect.ymin());
      max = new Point2D(root.x,rect.ymax());
    }else{
      StdDraw.setPenColor(StdDraw.BLUE);
      min = new Point2D(rect.xmin(),root.y);
      max = new Point2D(rect.xmax(),root.y);
    }

    StdDraw.setPenRadius();;
    min.drawTo(max);

    draw(root.leftNode,leftRect(rect,root));
    draw(root.rightNode,rightRect(rect,root));
  }

  private RectHV leftRect(RectHV rect,KdNode root){

      //如果是垂直，返回左半部分
      if(root.isVertical){
        return new RectHV(rect.xmin(),rect.ymin(),root.x,rect.ymax());
      }
      //如果是平行，返回下半部分矩形
      else{
        return new RectHV(rect.xmin(),rect.ymin(),rect.xmax(),root.y);
      }
  }

  private RectHV rightRect(final RectHV rect,final KdNode root){

      if(root.isVertical){
        return new RectHV(root.x,rect.ymin(),rect.xmax(),rect.ymax());
      }
      //如果是平行，返回上半部分矩形
      else{
        return new RectHV(rect.xmin(),root.y,rect.xmax(),rect.ymax());
      }
  }


  // draw all points to standard draw
  public Iterable<Point2D> range(RectHV rect){

      final TreeSet<Point2D> rangeSet = new TreeSet<Point2D>();
      range(rootNode,CONTAINER,rect,rangeSet);
      return rangeSet;

  }             // all points that are inside the rectangle (or on the boundary)


  private void range(KdNode root,RectHV nrect,RectHV rect,TreeSet<Point2D> st){

      if(root==null)
        return;

      if(rect.intersects(nrect)){
          Point2D p = new Point2D(root.x,root.y);
          if(rect.contains(p)){
              st.add(p);
          }

          range(root.leftNode,leftRect(nrect,root),rect,st);
          range(root.rightNode,rightRect(nrect,root),rect,st);

      }
  }

  public Point2D nearest(Point2D p){
    return nearest(rootNode,CONTAINER,p.x(),p.y(),null);
  }


  private Point2D nearest(KdNode root,RectHV rect,double x,double y,Point2D npoint){

      if(root==null){
        return npoint;
      }

      Point2D query = new Point2D(x,y);
      Point2D nearest = npoint;
      double QueryPointToNearestPoint = 0;
      double RecToQueryPoint = 0;
      RectHV left = null;
      RectHV right = null;


      if(nearest!=null){
        QueryPointToNearestPoint = query.distanceSquaredTo(nearest);
        RecToQueryPoint = rect.distanceSquaredTo(query);
      }


      if(nearest==null||QueryPointToNearestPoint>RecToQueryPoint){
        Point2D p = new Point2D(root.x,root.y);
        if(nearest==null||QueryPointToNearestPoint>query.distanceSquaredTo(p)){
            nearest = p;
        }

        if(root.isVertical){
          left = new RectHV(rect.xmin(),rect.ymin(),root.x,rect.ymax());
          right = new RectHV(root.x,rect.ymin(),rect.xmax(),rect.ymax());
          if(x<root.x){
            nearest = nearest(root.leftNode,left,x,y,nearest);
            nearest = nearest(root.rightNode,right,x,y,nearest);
          }else{

            nearest = nearest(root.rightNode,right,x,y,nearest);
            nearest = nearest(root.leftNode,left,x,y,nearest);
          }

        }
        else{

          left = new RectHV(rect.xmin(),rect.ymin(),rect.xmax(),root.y);
          right = new RectHV(rect.xmin(),root.y,rect.xmax(),rect.ymax());

          if(y<root.y){
            nearest = nearest(root.leftNode,left,x,y,nearest);
            nearest = nearest(root.rightNode,right,x,y,nearest);
          }
          else{
            nearest = nearest(root.rightNode,right,x,y,nearest);
            nearest = nearest(root.leftNode,left,x,y,nearest);
          }
        }

      }
      return nearest;

  }


  //pblic static void main(String[] args)                  // unit testing of the methods (optional)
}
