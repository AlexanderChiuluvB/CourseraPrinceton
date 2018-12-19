import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class lsd {
    private static final int BITS_PER_BYTE = 8;

    private lsd(){
    }

    private static void sort(String[]a,int w){
        int n = a.length;
        int R = 256;
        String[]temp = new String[n];

        for(int d=w-1;d>=0;d--){
            //count each ASCII char frequency
            int []count = new int[R+1];

            for(int i=0;i<n;i++){
                //a[i] the ith string
                //a[i].charAt(d) char at d position ,its count++
                //!attention with offset 1
                count[a[i].charAt(d)+1]++;
            }
            for(int i=0;i<R-1;i++){
                count[i+1]+=count[i];
            }

            //dont forget increment
            for(int i=0;i<n;i++){
                temp[count[a[i].charAt(d)]++] = a[i];
            }

            for(int i=0;i<n;i++){
                a[i] = temp[i];
            }

        }
    }

    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        int n = a.length;

        // check that strings have fixed length
        int w = a[0].length();
        for (int i = 0; i < n; i++)
            assert a[i].length() == w : "Strings must have fixed length";

        // sort the strings
        sort(a, w);

        // print results
        for (int i = 0; i < n; i++)
            StdOut.println(a[i]);
    }


}
