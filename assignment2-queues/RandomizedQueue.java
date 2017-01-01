import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> { 
  private Item[] items; 
  private int current = 0; 
  private int size;
 
  public RandomizedQueue() { 
    items = (Item[]) new Object[1]; 
    items[0] = null;
    size = 0;
  } 
 
  public boolean isEmpty() { 
    return current == 0;  
  } 
 
  public int size() { 
    return size; 
  } 
 
  public void enqueue(Item item) { 
    if(item == null) { throwExceptions(0); } 
    if(current == items.length ) { 
      resizeArray((current)*2); 
    } 
    items[current++] = item; 
    size ++;
  } 
 
  public Item dequeue() { 
    if (isEmpty()) { throwExceptions(1); } 
    int index = StdRandom.uniform(current); 
    Item item = items[index]; 
    items[index] = items[--current]; 
    items[current] = null;  
  
    if(current > 0 && current == items.length/4) { resizeArray(items.length/2); }     
    
    size--;
    return item; 
  } 
 
  public Item sample() { 
    if (isEmpty()) { throwExceptions(1); } 
    return items[StdRandom.uniform(current)]; 
  } 
 
  private void resizeArray(int size) { 
    Item[] newArray = (Item[]) new Object[size]; 
    for(int i = 0; i< current; i++) { 
      newArray[i] = items[i]; 
    } 
    items = newArray; 
  } 
 
  private void throwExceptions(int err) { 
    switch(err) { 
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
 
  public Iterator<Item> iterator() { return new RandomizedIterator(); } 
 
  private class RandomizedIterator implements Iterator<Item> { 
    private int n = 0; 
    private Item[] arr = (Item[])new Object[size];
    public RandomizedIterator() { 
        for(int i=0; i<size; i++) {
            arr[i] = items[i];
        }
      StdRandom.shuffle(arr); 
    } 
    public boolean hasNext() { 
      return n < current; 
    } 
 
    public Item next() { 
      if (n >= size) { throwExceptions(1); } 
//      if(n == size-1) { n=0; }
      return arr[n++]; 
    } 
 
    public void remove() { 
      throwExceptions(2); // remove operation not supported 
    } 
  }
  
  public static void main(String[] args) {
//      RandomizedQueue<String> rq = new RandomizedQueue<String>();
//      rq.enqueue("A");
//      rq.enqueue("B");
//      rq.enqueue("C");
//      System.out.println(rq.sample());
//      System.out.println(rq.sample());
//      System.out.println(rq.sample());
//      System.out.println(rq.size());
//      System.out.println(rq.dequeue());
//      System.out.println(rq.dequeue());
//      System.out.println(rq.dequeue());
//      System.out.println(rq.dequeue());
//      
      
//    String wanted ="";
//    
////    Iterator<String> i = rq.iterator();
//    
//    for(int r=0; r< 10; r++) {
//        while(wanted != "C"){
//            wanted = i.next();
//            System.out.println(wanted); 
//        }
//        wanted = "";
//    }
    
//    Iterator<String> p = rq.iterator();
//    while(p.hasNext()){
//        String q = p.next();
//        System.out.println(q); 
//    }
    
  }
}