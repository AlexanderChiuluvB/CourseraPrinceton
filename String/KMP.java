import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/*
 *  % java KMP abracadabra abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:               abracadabra
 *    
 */


public class KMP {


    private final int R;//radix;
    private int [][]dfa;

    private String pat;

    public KMP(String pat){
        this.R = 256;
        this.pat = pat;

        int m = pat.length();
        dfa = new int [R][m];
        dfa[pat.charAt(0)][0]=1;

        int reStartPoint = 0;
        int j;

        // j=1 -> next state is bound to be 1
        for(reStartPoint = 0,j=1;j<m;j++){
            for(int c=0;c<R;c++){
                dfa[c][j] = dfa[c][reStartPoint]; //set mismatch case
            }
            dfa[pat.charAt(j)][j] = j+1; //set match case
            //next state's reStartPoint is current state's reStartPoint
            //不断迭代
            reStartPoint = dfa[pat.charAt(j)][reStartPoint];
        }
    }


    public int search(String text){
        int M = pat.length();
        int i,j,N = text.length();
        for(i=0,j=0;i<N&&j<M;i++){
            j = dfa[text.charAt(i)][j];
        }
        //M is the length of the pattern string
        //return the staring porint of the found pattern string
        if(j==M)return i-M;
        //else return N last pos
        else return N;
    }

    public static void main(String[] args) {
        String pat = args[0];
        String txt = args[1];

        KMP kmp1 = new KMP(pat);
        int offset1 = kmp1.search(txt);

        // print results
        StdOut.println("text:    " + txt);

        StdOut.print("pattern: ");
        for (int i = 0; i < offset1; i++)
            StdOut.print(" ");
        StdOut.println(pat);

    }



}
