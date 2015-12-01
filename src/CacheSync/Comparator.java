/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CacheSync;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @title ENG EC 504 Final PRoject
 * @author shantanubobhate
 * @date November 30th, 2015
 * 
 */

public class Comparator {
    
    public static void compare(byte[] myFilter, byte[] otherFilter, 
            ArrayList<Integer> toGet, ArrayList<Integer> toSend) {
        
        for (int ii = 0; ii < myFilter.length; ii++) {
            // They have something we don't
            if (myFilter[ii] == 0 && otherFilter[ii] == 1) {
                toGet.add(ii);
            } else if (myFilter[ii] == 1 && otherFilter[ii] == 0) {
                toSend.add(ii);
            }
        }
    }
    
    public static ArrayList compareHashTables(ArrayList<Integer> myId, ArrayList<Integer> otherId) {
        ArrayList<Integer> toGet = new ArrayList();
        
        for (Integer id : otherId) {
            if (!myId.contains(id)) {
                toGet.add(id);
            }
        }
        
        return toGet;
    }
    
}
