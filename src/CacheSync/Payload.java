package CacheSync;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;

/**
 *
 * @author shanliang
 */
public class Payload implements Serializable {
    
    public int id ;
    public BitSet filter = null;
    public int keySize;
    public int numberOfElements;
    public ArrayList<String> strings = null;
    
    public Payload(int v, BloomFilter bloomFilter, ArrayList<String> list) {
        this.id = v;
        if (bloomFilter != null) {
            this.filter = bloomFilter.getFilter();
            this.keySize = bloomFilter.getKeySize();
            this.numberOfElements = bloomFilter.getSize();
        }
        this.strings = list;
    }
}
