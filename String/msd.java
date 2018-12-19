import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class msd {
    private static final int cutoff = 15;
    private static final int R = 256;

    private msd(){

    }



    public static void sort(String []a){
        int n = a.length;
        String [] temp = new String [n];
        sort(a,temp,0,n-1,0);
    }

    private static int charAt(String s,int d){
        if(d==s.length())return -1;
        return s.charAt(d);
    }

    private static void sort(String[]a,String []temp,int lo,int hi,int d){

        if(hi<=lo+cutoff){
            insertion(a,lo,hi,d);
            return;
        }

        //remember R+2 not n+2;
        int []count = new int[R+2];
        //count
        for(int i=lo;i<=hi;i++){
            count[a[i].charAt(d)+2]++;
        }

        //accumulate
        for(int i=0;i<R+1;i++){
            count[i+1]+=count[i];
        }

        //distribute
        for(int i=lo;i<=hi;i++){
            temp[count[a[i].charAt(d)]++] = a[i];
        }

        //i start from lo,so temp index start from i-lo;
        for(int i=lo;i<=hi;i++){
            a[i] = temp[i-lo];
        }

        //recursively sort for each character
        for(int r=0;r<R;r++){
            sort(a,temp,lo+count[r],lo+count[r+1]-1,d+1);
        }

    }


    private static void insertion(String[]a,int lo,int hi,int d){
        for(int i=lo;i<=hi;i++){
            //if string j < string j-1 ,swap!
            for(int j=i;j>lo&&compare(a[j],a[j-1],d);j--){
                swap(a,j,j-1);
            }
        }
    }

    //compare starts from d
    private static boolean compare(String v,String w,int d){
        for(int i=d;i<Math.min(v.length(),w.length());i++){
            if(v.charAt(i)<w.charAt(i))
                return true;
            if(v.charAt(i)>w.charAt(i))
                return false;
        }
        return v.length()<w.length();
    }

    private static void swap(String []a,int i,int j){
        String temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        int n = a.length;
        sort(a);
        for (int i = 0; i < n; i++)
            StdOut.println(a[i]);
    }

}
