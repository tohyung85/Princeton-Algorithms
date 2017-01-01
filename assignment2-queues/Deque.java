import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> { 
  private Node first, last; 
  private int size; 
 
 
  private class Node { 
    Item item; 
    Node next; 
    Node prev; 
  } 
 
  public Deque() { 
    size = 0;
  } 
 
  public boolean isEmpty() { 
    return first == null; 
  } 
 
  public int size() { 
    return size; 
  } 
 
  public void addFirst(Item item) { 
    int i = 0;
    if (item == null) { throwException(0); } 
    Node oldFirst = first; 
    first = new Node(); 
    first.item = item; 
    first.next = oldFirst; 
    if(oldFirst == null) { 
        last = first;
    } else {
        oldFirst.prev = first; 
    }
    size++;
  } 
 
  public void addLast(Item item) { 
    if (item == null) { throwException(0); } 
    Node oldLast = last; 
    last = new Node(); 
    last.item = item; 
    last.next = null; 
    last.prev = oldLast; 
    if(oldLast == null) { 
        first = last; 
    } else {
        oldLast.next = last;
    }
    size++;
  } 
 
  public Item removeFirst() { 
    if (isEmpty()) { throwException(1); } 
    Item item = first.item; 
    first = first.next;
    if(first != null) { 
        first.prev = null;
    } else {
        last = null;
    }
    size--;
    return item; 
  } 
 
  public Item removeLast() { 
    if (isEmpty()) { throwException(1); } 
    Item item = last.item; 
    last = last.prev; 
    if(last != null) {
        last.next = null;
    } else {
        first = null;
    }
    size--;
    return item; 
  } 
 
  public Iterator<Item> iterator() { 
    return new DequeIterator(); 
  } 
 
  private class DequeIterator implements Iterator<Item> { 
    private Node current = first; 
     
    public boolean hasNext(){ 
      return current != null; 
    } 
     
    public Item next() { 
      if (current == null) { throwException(1); } 
      Item item = current.item; 
      current = current.next; 
      return item; 
    } 
 
    public void remove() { 
      throwException(2); // no such method, throw exception 
    } 
  } 
 
 
  private void throwException(int i) { 
    switch (i) { 
      case 0: 
          throw new NullPointerException("Cannot add null item");
      case 1: 
          throw new java.util.NoSuchElementException("Deque is empty!");
      case 2: 
          throw new UnsupportedOperationException("Remove operation is not allowed");
      default: 
          throw new UnsupportedOperationException("Oops something went wrong"); 
    }  
  } 
 
  public static void main(String[] args) { 
// 
//    Deque dq = new Deque<Integer>();
//    dq.addFirst(2); 
//    dq.addFirst(1); 
//    dq.addLast(3); 
//    dq.addLast(null);
//    System.out.println(dq.removeFirst()); 
//    System.out.println(dq.removeLast()); 
//    dq.addFirst(4);
//    dq.addLast(5);
//    System.out.println(dq.removeLast()); 
//    System.out.println(dq.removeLast()); 
//    System.out.println(dq.removeFirst()); 
//    System.out.println(dq.removeFirst()); 
//    
//    Iterator<Integer> i = dq.iterator();
//    while(i.hasNext()){
//        int j = i.next();
//        System.out.println(j); 
//    }
  } 
}