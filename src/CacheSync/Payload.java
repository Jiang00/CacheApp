/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CacheSync;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author shanliang
 */
public class Payload implements Serializable {
    
    public int id ;
    public byte[] filter = null;
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
