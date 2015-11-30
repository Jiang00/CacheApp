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
    
    public static ArrayList compare(ArrayList<Integer> myId, ArrayList<Integer> otherId) {
        ArrayList<Integer> discrepancies = new ArrayList();
        
        for (int ii = 0; ii < 26; ii++) {
            if (!Objects.equals(myId.get(ii), otherId.get(ii))) {
                discrepancies.add(ii);
            }
        }
        
        return discrepancies;
    }
    
}
