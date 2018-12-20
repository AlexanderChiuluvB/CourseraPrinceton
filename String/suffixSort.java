import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

public class suffixSort {

    public suffixSort(){

    }

    private static int lcp(String a,String b){
        int N = Math.min(a.length(),b.length());
        for(int i=0;i<N;i++){
            if(a.charAt(i)!=b.charAt(i))
                return i;
        }
        return N;
    }

    public static String lrs(String s){

        int n = s.length();
        String []suffix = new String[n];
        for(int i=0;i<n;i++){
            suffix[i] = s.substring(i,n);
        }

        Arrays.sort(suffix);

        String result="";
        for(int i=0;i<n-1;i++){
            //compare between neighbouring 2 suffix substr
            int len  = lcp(suffix[i],suffix[i+1]);
            if(len>result.length()){
                result = suffix[i].substring(0,len);
            }
        }
        return result;
    }


    public static void main(String[] args) {
        String a = StdIn.readString();
        StdOut.println(lrs(a));
    }
}
