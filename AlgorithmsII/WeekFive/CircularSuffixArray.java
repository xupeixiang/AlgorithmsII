/************************************************************
 * 
 * Copyright (c) 2013, Peixiang Xu(peixiangxu@gmail.com)
 * 
 * This program is free software: you can redistribute it 
 * and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation.
 * 
 ************************************************************/
import java.util.Arrays;
import java.util.HashMap;


public class CircularSuffixArray {
    
    int index[];
    StringBuffer tail = null;
    // circular suffix array of s
    public CircularSuffixArray(String s){
        index = new int[s.length()];
        tail = new StringBuffer(s.length());
        HashMap<String, Integer> str2index = new HashMap<String, Integer>(); 
        for(int i = 0;i < s.length(); i++){
            str2index.put(s.substring(i) + s.substring(0,i), i);
        }
        String[] sorted =  (String[])str2index.keySet().toArray(new String[s.length()]);
        Arrays.sort(sorted);
        
        for(int i = 0; i < index.length; i++){
            tail.append(sorted[i].charAt(index.length - 1));
            index[i] = str2index.get(sorted[i]);
        }
    }
    
    // length of s
    public int length(){
        return index.length;
    }
    
    // returns index of ith sorted suffix
    public int index(int i){
        return index[i];
    }
    
    public String tail(){
        return tail.toString();
    }
}
