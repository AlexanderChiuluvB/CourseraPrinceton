import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

  private Item[]a;
  private int n;

  public RandomizedQueue() {
    a = (Item[])new Object[2];
    n=0;
  }                          // construct an empty deque

  private void resize(int capa){
    Item[]temp = (Item[])new Object[capa];
    for(int i=0;i<n;i++){
      temp[i] = a[i];
    }
    a = temp;
  }

  public boolean isEmpty(){
    return n==0;
  }// is the deque empty?


  public int size(){
    return n;
  }               // return the number of items on the deque

  public void enqueue(Item item){
    if(item==null){
      throw new IllegalArgumentException();
    }
    if(n==0){
      a[n++] = item;
      return;
    }
    if(n==a.length){
      resize(a.length*2);
    }
    int index = StdRandom.uniform(n);
    Item temp = a[index];
    a[index] = item;
    a[n++] = temp;

  }

  public Item dequeue(){
    if(isEmpty()){
      throw new NoSuchElementException();
    }
    if(n==a.length/4)
      resize(a.length/2);
    int index = StdRandom.uniform(n);
    Item temp = a[index];
    a[index] = a[--n];
    a[n] = null;
    return temp;
  }

  public Item sample(){

    if(isEmpty()){
      throw new NoSuchElementException();
    }
    int index = StdRandom.uniform(this.size());
    return a[index];

  }

  private class ArrayIterator implements Iterator<Item> {

    private int i;
    private int[] randomIndices;

    public ArrayIterator(){
      i = 0;
      randomIndices = new int[n];
      for(int j=0;j<n;j++){
        randomIndices[j] = j;
      }
      StdRandom.shuffle(randomIndices);
    }


    public boolean hasNext(){
      return i<n;
    }
    public Item next(){
      if(!hasNext())
        throw new NoSuchElementException();
      return a[randomIndices[i++]];
    }
    public void remove(){
      throw new UnsupportedOperationException();
    }
  }

  public Iterator<Item> iterator()       {
    return new ArrayIterator();
  }  // return an iterator over items in order from front to end
  public static void main(String[] args){

  }   // unit testing (optional)
}