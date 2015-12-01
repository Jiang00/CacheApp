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
    public int value ;
    public String id;
    public BloomFilter filter = null;
    public ArrayList<String> strings = null;
    
    public Payload(int v, String s, BloomFilter bloomFilter) {
        this.value=v;
        this.id=s;
        this.filter = bloomFilter;
    }
    
    public Payload(int v, String s, ArrayList list) {
        this.value = v;
        this.id = s;
        this.strings = list;
    }
}
