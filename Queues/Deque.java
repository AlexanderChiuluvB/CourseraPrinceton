import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

  private class Node{
      private Node prev;
      private Node next;
      Item item;

  }

  private Node first;
  private Node last;
  private int n;
  public Deque() {
    n=0;
  }                          // construct an empty deque
  public boolean isEmpty(){
      return n==0;
  }                 // is the deque empty?
  public int size()         {
    return n;
  }               // return the number of items on the deque

  public void addFirst(Item item){

    if(item==null)
      throw new IllegalArgumentException();

    Node oldFirst = first;
    first = new Node();
    first.item = item;
    first.next = oldFirst;
    if(last==null){
      last = first;
    }else
      first.next.prev = first;
    n++;

  }          // add the item to the front
  public void addLast(Item item){
    if(item==null)
      throw new IllegalArgumentException();
    Node oldLast = last;
    last = new Node();
    last.item = item;
    last.prev = oldLast;
    if(first==null){
      first = last;
    }else
      last.prev.next = last;
    n++;
  }           // add the item to the end
  public Item removeFirst()    {
    if(isEmpty()){
      throw new NoSuchElementException();
    }
    Item item = first.item;
    n--;
    if(isEmpty()){
      first = last = null;
    }else{
      first = first.next;
      first.prev = null;
    }
    return item;

  }            // remove and return the item from the front
  public Item removeLast(){
    if(isEmpty()){
      throw new NoSuchElementException();
    }
    Item item = last.item;
    n--;
    if(isEmpty()){
      first = last = null;
    }else{
      last = last.prev;
      last.next = null;
    }
    return item;
  }                 // remove and return the item from the end


  private class DequeIterator implements Iterator<Item> {
      private Node temp = first;
      public boolean hasNext(){
        return temp!=null;
      }
      public Item next(){
        if(!hasNext())
          throw new NoSuchElementException();
        Item item = temp.item;
        temp = temp.next;
        return item;
      }
      public void remove(){
        throw new UnsupportedOperationException();
      }
  }

  public Iterator<Item> iterator()       {
    return this.new DequeIterator();
  }  // return an iterator over items in order from front to end
  public static void main(String[] args){

  }   // unit testing (optional)
}