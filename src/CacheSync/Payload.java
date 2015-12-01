/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CacheSync;

import java.io.Serializable;

/**
 *
 * @author shanliang
 */
public class Payload implements Serializable {
    public int value ;
    public String id;
    public byte[] filter = null;
    
    public Payload(int v, String s, byte[] bloomFilter) {
        this.value=v;
        this.id=s;
        this.filter = bloomFilter;
    }
}
