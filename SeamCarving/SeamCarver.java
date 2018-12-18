import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import java.awt.Color;


public class SeamCarver {
    private Picture p;
    private double energy[][];
    public SeamCarver(Picture picture){
        validateParam(picture==null);
        this.p = new Picture(picture);
        FillEnergy();
    }                                                   // create a seam carver object based on the given picture
    public Picture picture(){
        return this.p;
    }                                                 // current picture
    public     int width(){
        return energy.length;
    }                            // width of current picture
    public     int height(){
        return energy[0].length;
    }                           // height of current picture

    private double calDeltaX(int x,int y){
        Color colorleft = p.get(x-1,y);
        Color colorright = p.get(x+1,y);
        double rleft = colorleft.getRed();
        double gleft = colorleft.getGreen();
        double bleft = colorleft.getBlue();
        double rright = colorright.getRed();
        double gright = colorright.getGreen();
        double bright = colorright.getBlue();
        return (rleft-rright)*(rleft-rright)+(gleft-gright)*(gleft-gright)+(bleft-bright)*(bleft-bright);

    }

    private double calDeltay(int x,int y){
        Color colorleft = p.get(x,y-1);
        Color colorright = p.get(x,y+1);
        double rleft = colorleft.getRed();
        double gleft = colorleft.getGreen();
        double bleft = colorleft.getBlue();
        double rright = colorright.getRed();
        double gright = colorright.getGreen();
        double bright = colorright.getBlue();
        return (rleft-rright)*(rleft-rright)+(gleft-gright)*(gleft-gright)+(bleft-bright)*(bleft-bright);

    }

    public  double energy(int x, int y){
        //x-width y-height
        if(x<0||x+1>p.width()||y<0||y+1>p.height())
            throw new IllegalArgumentException();
        if(x==0||y==0||x==p.width()-1||y==p.height()-1)
            //255^2*3
            return 1000.0;

        double deltax = calDeltaX(x,y);
        double deltay = calDeltay(x,y);

        return Math.sqrt(deltax+deltay);
    }

    private void FillEnergy(){
        this.energy = new double[p.width()][p.height()];
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++)
                this.energy[col][row] = energy(col,row);
        }
    }

    private void transposeEnergy(){
        double [][] transposeEnergy = new double[p.height()][p.width()];
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++)
                transposeEnergy[row][col] = energy[col][row];
        }
        this.energy = transposeEnergy;

    }

    private int PixelNum(int col,int row){
        return row*width()+col;
    }

    private int getRowFromPixel(int PixelNum){
        return PixelNum/width();
    }

    private int getColFromPixel(int PixelNum){
        return PixelNum%width();
    }


    private void initList(double[]distTO,int[]edgeTo){
        validateParam(distTO==null);
        validateParam(edgeTo==null);
        for(int col=0;col<width();col++){
            for(int row=0;row<height();row++){
                if(col==0){
                    //starting point is 0
                    distTO[PixelNum(col,row)] = 0;
                }else{
                    distTO[PixelNum(col,row)] = Double.MAX_VALUE;
                }
                edgeTo[PixelNum(col,row)] = -1;
            }
        }

    }

    // energy of pixel at column x and row y
    public int[] findHorizontalSeam(){
        double []distTo = new double[width()*height()];
        int []edgeTo = new int[width()*height()];
        initList(distTo,edgeTo);

        //bellman-ford
        for(int col=0;col<width()-1;col++){
            for(int row=0;row<height();row++){
                if(row-1>=0){
                    relaxEdge(PixelNum(col,row),PixelNum(col+1,row-1),distTo,edgeTo);
                }
                relaxEdge(PixelNum(col,row),PixelNum(col+1,row),distTo,edgeTo);
                if(row+1<=height()-1){
                    relaxEdge(PixelNum(col,row),PixelNum(col+1,row+1),distTo,edgeTo);
                }
            }
        }

        int[] result = new int[width()];

        //find the Last Pixel(Pixel on the last col)
        double curMin = Double.MAX_VALUE;
        int curPixel = -1;
        for(int row=0;row<height();row++){
            int pixelNum = PixelNum(width()-1,row);
            if(curMin>distTo[pixelNum]){
                curMin = distTo[pixelNum];
                curPixel = pixelNum;
            }
        }

        //find the minimal (lastPixel)curPixel,use the edgeTo to trace back the path,and return it
        int index = width()-1;
        while(curPixel!=-1){
            //根据每个col找seam对应的row在哪里
            result[index] = getRowFromPixel(curPixel);
            curPixel = edgeTo[curPixel];
            index--;
        }
        // sequence of indices for horizontal seam
        return result;
    }

    private void relaxEdge(int fromPixel,int toPixel,double[]distTo,int[]edgeTo){

        //int rowFrom = getRowFromPixel(fromPixel);
        //int colFrom = getColFromPixel(fromPixel);
        validateParam(distTo==null||edgeTo==null);

        int rowTo = getRowFromPixel(toPixel);
        int colTo = getColFromPixel(toPixel);

        if(distTo[toPixel]>distTo[fromPixel]+energy[colTo][rowTo]){
          distTo[toPixel] = distTo[fromPixel]+energy[colTo][rowTo];
          edgeTo[toPixel] = fromPixel;
        }

    }

    private void validateParam(boolean status){
        if(status){
            throw new IllegalArgumentException();
        }
    }
    // sequence of indices for vertical seam
    public  int[] findVerticalSeam(){

        //should not change the original energy
        double [][]transposeEnergy =energy;
        int []transposeResult;
        transposeEnergy();
        transposeResult = findHorizontalSeam();
        energy = transposeEnergy;
        return transposeResult;
    }
    public    void removeHorizontalSeam(int[] seam){
        validateParam(seam==null);
        Picture newP = new Picture(width(),height()-1);
        double [][] newEnergy = new double[width()][height()-1];
        if(height()<=1||width()!=seam.length)
            throw new IllegalArgumentException();

        for(int i=0;i<seam.length;i++){
            if(seam[i]<0||seam[i]>=height()){
                throw new IllegalArgumentException();
            }
            if(i<seam.length-1){
                if(Math.abs(seam[i]-seam[i+1])>1)
                    throw new IllegalArgumentException();
            }
        }

        //traverse the col
        for(int col=0;col<width();col++){
            //each col has a rowNum,which is on the seam
            //when encounter the row,ignore it
            for(int row=0;row<seam[col];row++){
                newP.set(col,row,p.get(col,row));
                newEnergy[col][row] = energy[col][row];
            }
            for(int row = seam[col]+1;row<height();row++){
                newP.set(col,row-1,p.get(col,row));
                newEnergy[col][row-1] = energy[col][row];
            }
        }
        this.p = newP;
        this.energy = newEnergy;
    }


    public void removeVerticalSeam(int[] seam){
        validateParam(seam==null);
        Picture newP = new Picture(width()-1,height());
        double [][] newEnergy = new double[width()-1][height()];
        if(width()<=1||height()!=seam.length)
            throw new IllegalArgumentException();
        for(int i=0;i<seam.length;i++){
            if(seam[i]<0||seam[i]>=width()){
                throw new IllegalArgumentException();
            }
            if(i<seam.length-1){
                if(Math.abs(seam[i]-seam[i+1])>1)
                    throw new IllegalArgumentException();
            }
        }

        for(int row=0;row<height();row++){
            for(int col=0;col<seam[row];col++){
                newP.set(col,row,p.get(col,row));
                newEnergy[col][row] = energy[col][row];
            }
            for(int col = seam[row]+1;col<width();col++){
                newP.set(col-1,row,p.get(col,row));
                newEnergy[col-1][row] = energy[col][row];
            }
        }
        this.p = newP;
        this.energy = newEnergy;

    }     // remove vertical seam from current picture

}
