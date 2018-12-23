import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

  private List<LineSegment> segments = new ArrayList<>();
  //private ArrayList<Double> FoundSegments = new ArrayList<Double>();


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


  //https://github.com/jKard1210/Collinear-Points/blob/master/BruteCollinearPoints.java
  public FastCollinearPoints(Point[] points){
        if(points==null)
            throw new IllegalArgumentException();
        for(int i=0;i<points.length;i++){
          if(points[i]==null)
            throw new IllegalArgumentException();
        }
        validate(points);
        //t//his.FoundSegments = new HashMap<Double,List<Point>>();
        Arrays.sort(points);
        for(Point StartPoint:points){
            Point[] pointsCopy = Arrays.copyOf(points,points.length);
            Arrays.sort(pointsCopy,StartPoint.slopeOrder());
            int count = 1;
            Point newLineBegin = null;
            for(int j=0;j<pointsCopy.length-1;j++){
                if(pointsCopy[j].slopeTo(StartPoint)==pointsCopy[j+1].slopeTo(StartPoint)){
                  count++;
                  if(count==2){
                    newLineBegin = pointsCopy[j];
                    count++;
                  }
                  else if(count>=4 && j+1==pointsCopy.length-1){
                    if(newLineBegin.compareTo(StartPoint)>0){
                      this.segments.add(new LineSegment(StartPoint,pointsCopy[j+1]));
                    }
                    count=1;
                  }
                }
                else if(count>=4){

                    if(newLineBegin.compareTo(StartPoint)>0){
                      segments.add(new LineSegment(StartPoint,pointsCopy[j]));
                    }
                    count = 1;

                }else{
                  count = 1;
                }

            }

        }
  }     // finds all line segments containing 4 or more points

  public           int numberOfSegments(){
      return this.segments.size();
  }        // the number of line segments
  public LineSegment[] segments(){
      return this.segments.toArray(new LineSegment[segments.size()]);
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
    FastCollinearPoints collinear = new FastCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }

}