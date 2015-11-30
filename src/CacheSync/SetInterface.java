/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CacheSync;

/**
 *
 * @author shanliang
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @title ENG EC 504 Final Project
 * @author shantanubobhate
 * @data November 29th, 2015
 * 
 */
public class SetInterface {

    // Create an array of the alphabet
    private static final char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f',
                                            'g', 'h', 'i', 'j', 'k', 'l',
                                            'm', 'n', 'o', 'p', 'q', 'r',
                                            's', 't', 'u', 'v', 'w', 'x',
                                            'y', 'z'};
    
    // Make sets of size below (avarage from statistics)
    private static final int SIZE = 5326;
    
    // Decalare a FileReader and initialize it to null
    private static FileReader myFileReader = null;
    
    public static ArrayList<Integer> setKeyList = new ArrayList<Integer>();
    
    public static TrieST<Integer> st = new TrieST<Integer>();
    
    public static void buildStructure(String fileName) {
        // NOTE: Set boolean flag to see data to help debug
        boolean debug = false;
        
        // Create Sets and store them in an ArrayList
        ArrayList<MySet> sets = new ArrayList<>();
        for (char c : alphabet)
        {
            // Add set with the character as the identifier and a specified size
            sets.add(new MySet(c, SIZE));
        }
        
        try {
            // Try to read from the file
            myFileReader = new FileReader(fileName);
            BufferedReader myBufferedReader = new BufferedReader(myFileReader);
            String line;
            while ((line = myBufferedReader.readLine()) != null) {
                // Split the line at any whitespace
                String[] split = line.split("\\s+");
                // Get the first character to determine set
                char firstChar = line.charAt(0);
                // Iterate through the sets
                for (MySet s : sets)
                {
                    // If the first character matches the set identifier
                    // Add the string, its hash and its popularity to the set
                    if (s.getName() == firstChar)
                    {
                        s.addNewEntry(Hash(line), split[0], Integer.parseInt(split[1]));
                        // Build trie
                        st.put(split[0], Integer.parseInt(split[1]));
                    }
                    
                }
            }
        // Catch any exceptions
        } catch (IOException | NumberFormatException e) {
            System.out.println(e);
        } finally {
            // Try to close the file
            try {
                if (myFileReader != null) myFileReader.close();
            // Catch any exceptions
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        
        // NOTE: 1st structure to send over
        for (MySet s : sets)
        {
            setKeyList.add(s.getMyID());
        }
        
        // Print message if sets were built successfully
        System.out.println("Done adding elements.");
        
        if (debug)
        {
            // Print out the set properties
            sets.stream().forEach((s) -> {
                System.out.println("Set " + s.getName() + 
                                   " has a hash value of " + s.getMyID() +  
                                   " with " + s.getSize() + " elements");
            });

            // Print out the hash table in each set
            sets.stream().forEach((s) -> {
                s.printMap();
            });
        }
    }

    // Function to hash strings
    private static int Hash(String key) {
        int strlen = key.length();
        int hashValue = 11;
        for (int i = 0; i < strlen; i++) {
            hashValue = hashValue*13 + key.charAt(i);
        }
        return Math.abs(hashValue);
    }
    
    
}
