import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class threeWaySort {
    private static final int cutoff = 15;
    private static final int R = 256;

    private threeWaySort(){

    }

    public static void sort(String []a){
        StdRandom.shuffle(a);
        int n = a.length;
        sort(a,0,n-1,0);
    }

    private static int charAt(String s,int d){
        if(d==s.length())return -1;
        return s.charAt(d);
    }

    private static void sort(String[]a,int lo,int hi,int d){

        if(hi<=lo+cutoff){
            insertion(a,lo,hi,d);
            return;
        }
        int lt = lo;
        int gt = hi;
        int flag = charAt(a[lo],d);
        int i = lo+1;
        while(i<=gt){
            int tmp = charAt(a[i],d);
            if(tmp>flag){
                swap(a,i,gt--);
            }else if(tmp<flag){
                swap(a,lt++,i++);
            }
            else i++;
        }
        sort(a,lo,lt-1,d);
        if(flag>=0){
            sort(a,lt,gt,d+1);
        }
        sort(a,gt+1,hi,d);
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
