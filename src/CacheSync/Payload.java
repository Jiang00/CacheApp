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
public class Payload implements Serializable{
    public int value ;
    public String id;
    public ArrayList<Integer> keyList;
    
public  Payload(int v, String s, ArrayList<Integer> keyList){
this.value=v;
this.id=s;
this.keyList=keyList;
}
}
