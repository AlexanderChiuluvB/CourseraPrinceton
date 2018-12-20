import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Tries<Value> {

    private static final int R = 256;
    private int n;
    private Node root = new Node();
    private static class Node{
        private Object value;
        //each node has an array of links and a value
        private Node[] next = new Node [R];
    }

    public void put(String key,Value val){
        if(val==null) delete(key);
        root = put(root,key,val,0);
    }

    //val is corresponds to the last char of one specfic word
    private Node put(Node x,String key,Value val,int d){
        if(x==null)x = new Node();
        if(d==key.length()){
            if(x.value==null)n++;
            x.value = val;
            return x;
        }
        char c = key.charAt(d);
        x.next[c] = put(x.next[c],key,val,d+1);
        return x;
    }

    public boolean contains(String key){
        return get(key)!=null;
    }

    private Value get(String key){
        Node x = get(root,key,0);
        if(x==null)
            return null;
        //castring
        return (Value) x.value;
    }

    public int size(){
        return n;
    }

    public Iterable<String> keysWithPrefix(String prefix){
        Queue<String>q = new Queue<String>();
        Node x = get(root,prefix,0);
        collect(x,new StringBuilder(prefix),q);
        return q;
    }

    public Iterable<String> keys(){
        return keysWithPrefix("");
    }

    private void collect(Node x, StringBuilder prefix, Queue<String> result){
        if(x==null)return;
        if(x.value!=null)
            result.enqueue(prefix.toString());
        for(char c=0;c<R;c++){
            prefix.append(c);
            collect(x.next[c],prefix,result);
            prefix.deleteCharAt(prefix.length()-1);
        }
    }

    private Node get(Node root,String key,int d){
        if(root==null)
            return null;
        if(d==key.length())
            return root;
        char c = key.charAt(d);
        return  get(root.next[c],key,d+1);

    }

    public void delete(String key){
        if(key==null)
            throw new IllegalArgumentException();
        root = delete(root,key,0);
    }

    private Node delete(Node root,String key,int d){

        if(root==null)
            return null;
        //find it
        if(d==key.length()) {
            //if not yet deleted, then n--
            if (root.value != null) {
                n--;
            }
            root.value = null;
        }
        //recursively find it
        else{
            char c= key.charAt(d);
            root.next[c] = delete(root.next[c],key,d+1);
        }

        if(root.value!=null)
            return root;

        for(int c=0;c<R;c++){
            if(root.next[c]!=null){
                return root;
            }
        }

        return null;
    }

    public static void main(String[] args) {

        // build symbol table from standard input
        Tries<Integer> st = new Tries<Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }

        // print results
        if (st.size() < 100) {
            StdOut.println("keys(\"\"):");
            for (String key : st.keys()) {
                StdOut.println(key + " " + st.get(key));
            }
            StdOut.println();
        }

    }


}
