
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;




import java.util.ArrayList;


public class WordNet {
    private final ST<String, Bag<Integer>> set = new ST<>();
    private final Digraph G;
    private final SAP sap;
    private final ArrayList<String> nounList;
    private class Noun implements Comparable<Noun>{
        private final String noun;
        private final  ArrayList<Integer> idList = new ArrayList<Integer>();

        public Noun(String noun){
            this.noun = noun;
        }

        public int compareTo(Noun noun2){
            return this.noun.compareTo(noun2.noun);
        }

        public void addId(Integer x){
            this.idList.add(x);
        }

        public ArrayList<Integer> getIdList(){
            return this.idList;
        }
    }

    private void validateParam(Object param) {
        if (param == null)
            throw new IllegalArgumentException("param must be not null");
    }


    public WordNet(String synsets,String hypernyms){
        validateParam(synsets);
        validateParam(hypernyms);
        In synsetsInput = new In(synsets);
        In hypernymsINput = new In(hypernyms);
        //used to construct the Graph
        int vertexNum = 0;

        nounList = new ArrayList<String>();


        String line =synsetsInput.readLine();
        while(line!=null){
            vertexNum++;
            String [] synLine = line.split(",");
            //synLine[0] is id;
            Integer id = Integer.parseInt(synLine[0]);
            //synLine[1] is noun set;
            String[] nounSet = synLine[1].split(" ");
            for(String nounName:nounSet){
                if(set.contains(nounName)){
                    //lower_bound
                    //因为一个noun可能被多个id指向，因此如果noun已经存在了，需要给他的idlist添加idd
                    set.get(nounName).add(id);
                }else{
                    Bag<Integer> bag = new Bag<Integer>();
                    bag.add(id);
                    set.put(nounName,bag);
                }
            }
            nounList.add(synLine[1]);
            line = synsetsInput.readLine();

        }


        G = new Digraph(vertexNum);

        //find invalid graph
        //root 就是出度=0，只要没有在hypernyms.txt出现过的，出都都为0
        boolean []notRoot = new boolean[G.V()];
        line = hypernymsINput.readLine();
        while(line!=null){
            String[] hyperLine = line.split(",");
            int id =Integer.parseInt(hyperLine[0]);
            notRoot[id] = true;

            for(int i=1;i<hyperLine.length;i++){
                G.addEdge(id,Integer.parseInt(hyperLine[i]));
            }
            line = hypernymsINput.readLine();
        }


        //if has cycle
        DirectedCycle directedCycle = new DirectedCycle(G);
        if(directedCycle.hasCycle()){
            throw new java.lang.IllegalArgumentException();
        }

        //or more than one root;
        int rootCount=0;

        for(int i=0;i<notRoot.length;i++){
            if(!notRoot[i]){
                rootCount++;
            }
        }

        if(rootCount>1)
            throw new java.lang.IllegalArgumentException();

        //only then the graph is valid;
        sap = new SAP(G);
    }


    public boolean isNoun(String word){
        validateParam(word);
        return set.contains(word);
    }

    public int distance(String nounA,String nounB){

        validateParam(nounA);
        validateParam(nounB);
        Bag<Integer>bagnounA = set.get(nounA);
        Bag<Integer>bagnounB = set.get(nounB);
        return sap.length(bagnounA,bagnounB);
        //return result;
    }

    public Iterable<String> nouns(){
        return set.keys();
    }

    public String sap(String nounA,String nounB){
        validateParam(nounA);
        validateParam(nounB);
        Bag<Integer>bagnounA = set.get(nounA);
        Bag<Integer>bagnounB = set.get(nounB);

        return nounList.get(sap.ancestor(bagnounA,bagnounB));
    }
}
