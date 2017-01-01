import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Subset {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        String[] strings = StdIn.readAllStrings();
        int n = strings.length;
        for(int i =0 ; i< k; i++) {
            int index;
            if(n==1) {
                index= 0;
            } else {
                index = StdRandom.uniform(n);
            }
            rq.enqueue(new String(strings[index]));
            strings[index] = strings[n-1];
            n--;
        }
        for(int i=0; i< k; i++) {
            StdOut.println(rq.dequeue());
        }
    }
}