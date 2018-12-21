import edu.princeton.cs.algs4.Queue;

public class FastTST<Value> {

    private int N;

    private Node root;

    private class Node{
        private char c;
        private Node left,mid,right;
        private Value v;
    }

    public int size(){
        return N;
    }

    //is string in symbol table?

    public boolean contains(String word){
        return get(word)!=null;
    }


    public Value get(String word){
        if(word==null||word.length()==0)
            throw new IllegalArgumentException();
        Node x = get(root,word,0);
        if(x==null)
            return null;
        return x.v;
    }

    private Node get(Node root,String word,int d){
        if(word==null||word.length()==0)
            throw new IllegalArgumentException();
        if(root==null)
            return null;

        char c = word.charAt(d);
        if(c>root.c){
            return get(root.right,word,d);
        }
        else if(c<root.c){
            return get(root.left,word,d);
        }else if(d<word.length()-1){
            return get(root.mid,word,d+1);
        }
        else
            return root;
    }


    //insert

    public void put(String s,Value v){
        if(!contains(s)){
            N++;
        }
        root = put(root,s,v,0);
    }

    private Node put(Node x,String s,Value v,int d){
        char c = s.charAt(d);
        if(x==null){
            x = new Node();
            x.c = c;
        }
        if(c>x.c){
            x.right = put(x.right,s,v,d);
        }
        else if(c<x.c){
            x.left = put(x.left,s,v,d);
        }
        else if(d<s.length()-1){
            x.mid = put(x.mid,s,v,d+1);
        }
        else
            x.v = v;
        return x;
    }


    //find and return longest prefix of

    public String longestPrefixOf(String s){
        if(s==null||s.length()==0)
            return null;
        int len = s.length();
        Node x = root;
        int i=0;
        while(x!=null&&i<s.length()){
            char c = s.charAt(i);
            if(c<x.c){
               x = x.left;
            }
            else if(c>x.c){
                x = x.right;
            }
            else{
                i++;
                if(x.v!=null)
                    len = i;
                x = x.mid;
            }
        }
        return s.substring(0,len);
    }


    public Iterable<String>keys(){
        Queue<String>queue = new Queue<>();
        collect(root,"",queue);
        return queue;
    }

    //all words starts with prefix
    public Iterable<String> prefixMatch(String prefix){
        //you need to first find the node where the the node points to the last char of the prefix
        Queue<String>queue = new Queue<>();
        Node x = get(root,prefix,0);
        if(x==null)return null;
        if(x.v!=null)queue.enqueue(prefix);
        collect(x.mid,prefix,queue);
        return queue;

    }

    //inorder traverse
    private void collect(Node x,String prefix,Queue<String>q){
        if(x==null)
            return;
        collect(x.left,prefix,q);
        if(x.v!=null)q.enqueue(prefix+x.c);
        collect(x.mid,prefix+x.c,q);
        collect(x.right,prefix,q);
    }

    public boolean hasPrefix(String prefix){
        Node prefixNode = get(root,prefix,0);
        if(prefixNode==null)
            return false;
        if(prefixNode.v!=null)
            return true;
        if(prefixNode.left==null&&prefixNode.right==null&&prefixNode.mid==null)
            return false;
        return true;
    }


}
