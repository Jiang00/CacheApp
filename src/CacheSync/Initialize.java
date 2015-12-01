package CacheSync;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author shantanubobhate
 */
public class Initialize {
    
    
    // n = 138,000, p = 0.0001 (1 in 10,000) → m = 2,645,477 (322.93KB), k = 13
    
    // Make sets of size below (avarage from statistics)
    private static final int SIZE = 2645477;
    // Decalare a FileReader and initialize it to null
    private static FileReader myFileReader = null;
    // The bloom filter
    public static final byte[] filter = new byte[SIZE];
    // Hashmap to store strings and their hash values
    private static HashMap data;
    // Hashmap to store strings and their popularities
    private static HashMap popularities;
    private static final int k = 13;
//    private static final int[] prime1 = {11, 17, 23, 31, 41, 47, 59, 67, 73, 83, 97, 103, 109};
//    private static final int[] prime2 = {13, 19, 29, 37, 43, 53, 61, 71, 79, 89, 101, 107, 113};
    
    private static ArrayList strings = new ArrayList();

    
    public static boolean buildStructure(String fileName) {
        // NOTE: Set boolean flag to see data to help debug
        boolean debug = false;
        
        for (int ii = 0; ii < SIZE; ii++) {
            filter[ii] = 0;
        }
        
        try {
            // Try to read from the file
            myFileReader = new FileReader(fileName);
            BufferedReader myBufferedReader = new BufferedReader(myFileReader);
            String line;
            while ((line = myBufferedReader.readLine()) != null) {
                // Split the line at any whitespace
                String[] split = line.split("\\s+");
                strings.add(split[0]);
                for (int ii = 0; ii < k; ii++) {
                    
//                    int index = Hash(split[0], prime1[ii], prime2[ii]) % SIZE;
                    filter[index] = 1;
                }
            }
        // Catch any exceptions
        } catch (IOException | NumberFormatException e) {
            System.out.println(e);
            return false;
        } finally {
            // Try to close the file
            try {
                if (myFileReader != null) myFileReader.close();
            // Catch any exceptions
            } catch (Exception e) {
                System.out.println(e);
                return false;
            }
        }

        // Print message if sets were built successfully
        System.out.println("Done adding elements.");
        
        for (byte b : filter) {
            System.out.println(b);
        }
        
        return true;
    }

    // Function to hash strings
//    private static int Hash(String key, int value1, int value2) {
//        int strlen = key.length();
//        int hashValue = value1;
//        for (int i = 0; i < strlen; i++) {
//            hashValue = hashValue*value2 + key.charAt(i);
//        }
//        return Math.abs(hashValue);
//    }
    
    public static long hash64(final String text) {
        final byte[] bytes = text.getBytes(); 
        return JenkinsHash. hash64(bytes, bytes.length);
    }
    
    public static ArrayList<String> getStrings(ArrayList toSend) {
        ArrayList<String> toSendStrings = new ArrayList();
        System.out.println("Processing: Please Wait...");
        for (int jj = 0; jj < strings.size(); jj++) {
            String currentString = (String) strings.get(jj);
            for (int ii = 0; ii < k; ii++) {
                int index = Hash( currentString, prime1[ii], prime2[ii]) % SIZE;
                if (toSend.contains(index)) {
                    toSendStrings.add(currentString);
                    // System.out.println(currentString);
                    break;
                }
            }
        }
        System.out.println("Processing done.");
        return toSendStrings;
    }
}
