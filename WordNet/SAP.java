import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Objects;

public class SAP {
    private static final int INFINITY = 0x3f3f3f;
    private final Digraph G;


    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G){
       checkCondition(G==null);
        this.G = new Digraph(G);
    }

    private void checkCondition(boolean status){
        if(status){
            throw new IllegalArgumentException();
        }
    }


    private int getAncestor(int v,int w){
        checkCondition(v<0||v>=G.V());
        checkCondition(w<0||w>=G.V());
        DeluxeBFS vbfs = new DeluxeBFS(G,v);
        DeluxeBFS wbfs = new DeluxeBFS(G,w);
        int minAncestor=-1;
        int tempDis = INFINITY;
        int minDis = INFINITY;
        for(int i=0;i<G.V();i++){
            if(vbfs.hasPathTo(i)&&wbfs.hasPathTo(i)){
                tempDis = vbfs.distTo(i)+wbfs.distTo(i);
                if(tempDis<minDis){
                    minDis = tempDis;
                    minAncestor = i;
                }
            }
        }
        return minAncestor;
    }

    private int getLength(int v,int w){
        checkCondition(v<0||v>=G.V());
        checkCondition(w<0||w>=G.V());
        DeluxeBFS vbfs = new DeluxeBFS(G,v);
        DeluxeBFS wbfs = new DeluxeBFS(G,w);
        //int minAncestor=-1;
        int tempDis = INFINITY;
        int minDis = INFINITY;
        for(int i=0;i<G.V();i++){
            if(vbfs.hasPathTo(i)&&wbfs.hasPathTo(i)){
                tempDis = vbfs.distTo(i)+wbfs.distTo(i);
                if(tempDis<minDis){
                    minDis = tempDis;
                    //minAncestor = i;
                }
            }
        }
        return minDis==INFINITY?-1:minDis;
    }


    private int getAncestor(Iterable<Integer>v,Iterable<Integer> w){
        checkCondition(v==null);
        checkCondition(w==null);
        DeluxeBFS vbfs = new DeluxeBFS(G,v);
        DeluxeBFS wbfs = new DeluxeBFS(G,w);
        int minAncestor=-1;
        int tempDis = INFINITY;
        int minDis = INFINITY;
        for(int i=0;i<G.V();i++){
            if(vbfs.hasPathTo(i)&&wbfs.hasPathTo(i)){
                tempDis = vbfs.distTo(i)+wbfs.distTo(i);
                if(tempDis<minDis){
                    minDis = tempDis;
                    minAncestor = i;
                }
            }
        }
        return minAncestor;
    }



    private int getLength(Iterable<Integer>v,Iterable<Integer> w){
        checkCondition(v==null);
        checkCondition(w==null);
        DeluxeBFS vbfs = new DeluxeBFS(G,v);
        DeluxeBFS wbfs = new DeluxeBFS(G,w);
        //int minAncestor=-1;
        int tempDis = INFINITY;
        int minDis = INFINITY;
        for(int i=0;i<G.V();i++){
            if(vbfs.hasPathTo(i)&&wbfs.hasPathTo(i)){
                tempDis = vbfs.distTo(i)+wbfs.distTo(i);
                if(tempDis<minDis){
                    minDis = tempDis;
                    //minAncestor = i;
                }
            }
        }
        return minDis==INFINITY?-1:minDis;
    }


    public int length(int v,int w){
        checkCondition(v<0||v>=G.V());
        checkCondition(w<0||w>=G.V());
        return getLength(v,w);
    }

    public int length(Iterable<Integer> v,Iterable<Integer> w){
        checkCondition(v==null);
        checkCondition(w==null);
        for(int v1:v)
            checkCondition(v1<0||v1>=G.V());
        for(int w1:v)
            checkCondition(w1<0||w1>=G.V());
        return getLength(v,w);
    }

    public int ancestor(int v,int w){
        checkCondition(v<0||v>=G.V());
        checkCondition(w<0||w>=G.V());
        return getAncestor(v,w);
    }

    public int ancestor(Iterable<Integer> v,Iterable<Integer> w){
        checkCondition(v==null);
        checkCondition(w==null);
        for(int v1:v)
            checkCondition(v1<0||v1>=G.V());
        for(int w1:v)
            checkCondition(w1<0||w1>=G.V());
        return getAncestor(v,w);
    }


    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

