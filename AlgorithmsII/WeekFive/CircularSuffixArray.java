/************************************************************
 * 
 * Copyright (c) 2013, Peixiang Xu(peixiangxu@gmail.com)
 * 
 * This program is free software: you can redistribute it 
 * and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation.
 * 
 ************************************************************/
import java.util.ArrayList;
import java.util.Arrays;


public class CircularSuffixArray {
    
    int index[];
    // circular suffix array of s
    public CircularSuffixArray(String s){
        index = new int[s.length()];
        int current = 0;
        //double s for sub and locate
        String doubleS = s + s;
        
        //sort by group
        for(int i = 0; i < 256; i++){
            ArrayList<String> groupStrings  = new ArrayList<String>();
            char groupChar = (char)i;
            int begin = 0;
            int found = s.indexOf(groupChar, begin);
            while(found >= 0){
                groupStrings.add(doubleS.substring(found,s.length() + found));
                found = s.indexOf(groupChar, found + 1);
            }
            if(groupStrings.size() > 0){
                String[] sorted = groupStrings.toArray(new String[0]);
                Arrays.sort(sorted);
                for(String groupString:sorted){
                    index[current++] = doubleS.indexOf(groupString);
                }
            }
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
}
