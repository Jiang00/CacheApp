package CacheSync;

import java.security.*;
import java.math.*;
import java.nio.*;

/**
 *
 * @title ENG EC 504 Final Project
 * @author shantanubobhate
 * @date December 1st, 2015
 * 
 */
public class BloomFilter {

    // The byte array
    private byte[] set;
    // The number of hash functions, the size of the byte array and the number of elements added
    private int keySize, setSize, size;
    // The MD5 hash
    private MessageDigest md;
    
    // Constructor
    public BloomFilter(int capacity, int k) {
        // Initialize sizes and byte array
        setSize = capacity;
        set = new byte[setSize];
        keySize = k;
        size = 0;
        
        // Try to create an instance of the MD5 hash
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Error: MD5 hash not found");
        }
    }
    
    public BloomFilter(byte[] filter, int k, int numberOfElements) {
        this(filter.length, k);
        set = filter;
        size = numberOfElements;
    }
    
    // Function to get the byte array
    public byte[] getFilter() {
        return set;
    }
    
    /* Function to get size of objects added */
    public int getSize()
    {
        return size;
    }
    
    /* Function to get the keySize */
    public int getKeySize()
    {
        return keySize;
    }
    
    /* Function to get hash - MD5 */
    private int getHash(int i)
    {
        md.reset();
        byte[] bytes = ByteBuffer.allocate(4).putInt(i).array();
        md.update(bytes, 0, bytes.length);
        return Math.abs(new BigInteger(1, md.digest()).intValue()) % (set.length - 1);
    }
    
    /* Function to add an object */
    public void add(Object obj)
    {
        int[] tmpset = getSetArray(obj);
        for (int i : tmpset)
            set[i] = 1;
        size++;
    }
    
    /* Function to check is an object is present */
    public boolean contains(Object obj) 
    {
        int[] tmpset = getSetArray(obj);
        for (int i : tmpset)
            if (set[i] != 1)
                return false;
        return true;
    }
    
    /* Function to get set array for an object */
    private int[] getSetArray(Object obj)
    {
        int[] tmpset = new int[keySize];
        tmpset[0] = getHash(obj.hashCode());
        for (int i = 1; i < keySize; i++)
            tmpset[i] = (getHash(tmpset[i - 1]));
        return tmpset;
    }   
}

