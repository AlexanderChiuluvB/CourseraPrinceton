
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver
{

    //三路字典树
    private FastTST<Integer> dic;
    private int[]adjx = {0,0,1,1,1,-1,-1,-1};
    private int[]adjy = {1,-1,1,0,-1,1,0,-1};
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary){
        if(dictionary==null||dictionary.length==0)
            throw new IllegalArgumentException();
        this.dic = new FastTST<>();
        int [] points={0,0,0,1,1,2,3,5,11};
        for(String s:dictionary){
            if(s.length()>=points.length-1){
                this.dic.put(s,points[points.length-1]);
            }else{
                this.dic.put(s,points[s.length()]);
            }
        }

    }

    private boolean onBoard(BoggleBoard board,int i,int j){
        int rows = board.rows();
        int cols = board.cols();
        if(i<0||j<0||i>=rows||j>=cols){
            return false;
        }
        return true;
    }

    private boolean isValidWords(String word){
        if(word==null)return false;
        if(dic.contains(word)&&word.length()>2){
            return true;
        }
        return false;
    }


    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board){
        if(board==null)
            throw new IllegalArgumentException();
        SET<String> res = new SET<>();

        for(int i=0;i<board.rows();i++){
            for(int j=0;j<board.cols();j++){
                String temp = addLetter("",board.getLetter(i,j));
                boolean [][]visited = new boolean[board.rows()][board.cols()];
               // visited[i][j] = true;
                dfs(board,i,j,temp,visited,res);
            }
        }
        return res;
    }

    private String addLetter(String t,char c){
        if(c=='Q'){
            t+="QU";
        }
        else
            t+=c;
        return t;
    }

    private void dfs(BoggleBoard board,int i,int j,String temp,boolean [][]visited,SET<String>res){

        if(visited[i][j]||this.dic.hasPrefix(temp)==false){
            return;
        }

        if(isValidWords(temp)){
            res.add(temp);
        }

        visited[i][j] = true;
       for(int d=0;d<8;d++){
           int ii = i+adjx[d];
           int jj = j+adjy[d];
           if(onBoard(board,ii,jj)){
               dfs(board,ii,jj,addLetter(temp,board.getLetter(ii,jj)),visited,res);
           }
       }
       visited[i][j] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word){
        //find Qu
        if(word==null||word.length()==0)
            throw new IllegalArgumentException();

        Integer score = dic.get(word);
        if(score==null)
            return 0;
        return score;
    }


    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
