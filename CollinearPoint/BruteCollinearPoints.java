import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IllformedLocaleException;

public class BruteCollinearPoints {

  private LineSegment[]set;
  //private Point[] pointList;
  private void validate(Point[] points){
    if(points==null)
      throw new IllegalArgumentException();
    for(int i=0;i<points.length-1;i++){
      for(int j=i+1;j<points.length;j++){
        if(points[i].compareTo(points[j])==0){
          throw new IllegalArgumentException();
        }
      }
    }
  }

  public BruteCollinearPoints(Point[] points) {

    validate(points);
    for(int i=0;i<points.length;i++){
      if(points[i]==null)
        throw new IllegalArgumentException();
    }
    int pointsNum = points.length;
    Point[] pointsCopy = Arrays.copyOf(points,points.length);
    Arrays.sort(pointsCopy);
    ArrayList<LineSegment> foundSegments = new ArrayList<LineSegment>();
    for(int i=0;i<pointsNum-3;i++){
      for(int j=i+1;j<pointsNum-2;j++){
        for(int k = j+1;k<pointsNum-1;k++){
          for(int l=k+1;l<pointsNum;l++){
            if(pointsCopy[i].slopeTo(pointsCopy[j])==pointsCopy[i].slopeTo(pointsCopy[k])
            &&pointsCopy[i].slopeTo(pointsCopy[j])==pointsCopy[i].slopeTo(pointsCopy[l])){
                LineSegment temp = (new LineSegment(pointsCopy[i],pointsCopy[l]));
                if(!foundSegments.contains(temp)){
                  foundSegments.add(temp);
                }
            }
          }
        }
      }
    }
    this.set = foundSegments.toArray(new LineSegment[foundSegments.size()]);
  }   // finds all line segments containing 4 points
  public           int numberOfSegments()
  {
    return this.set.length;
  }        // the number of line segments
  public LineSegment[] segments(){
    return set;
  }                // the line segments

  public static void main(String[] args) {

    // read the n points from a file
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
