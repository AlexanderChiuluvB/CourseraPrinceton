import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;

    public Outcast(WordNet wordnet) {
        validateParam(wordnet);
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {
        validateParam(nouns);
        int []distance = new int[nouns.length];
        for(int i = 0; i < nouns.length; i++){
            for(int j = i; j < nouns.length; j++){
                int dis = wordnet.distance(nouns[i],nouns[j]);
                distance[i]+=dis;
                if(i!=j)
                    distance[j]+=dis;
            }
        }

        int maxDistance = 0;
        int maxIndex = 0;
        for(int i=0;i<distance.length;i++){
            if(distance[i]>maxDistance){
                maxDistance = distance[i];
                maxIndex = i;
            }
        }
        return nouns[maxIndex];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

    private void validateParam(Object param) {
        if (param == null)
            throw new IllegalArgumentException("param must be not null");
    }

}
