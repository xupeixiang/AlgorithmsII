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


public class CircularSuffixArray {
    
    int index[];
    // circular suffix array of s
    public CircularSuffixArray(String s){
        index = new int[s.length()];
        char[] chars = s.toCharArray();
        RotateString[] rotateStrings = new RotateString[s.length()];
        for(int i = 0;i < s.length();i++){
            rotateStrings[i] = new RotateString(chars, i);
        }
        Arrays.sort(rotateStrings);
        for(int j = 0;j < s.length(); j++){
            index[j] = rotateStrings[j].getRotate();
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
    
    private class RotateString implements Comparable<RotateString>{
        private char[] originalString;
        private int rotate;
        
        public RotateString(char[] originalString,int rotate){
            this.originalString = originalString;
            this.rotate = rotate;
        }
        
        public int getRotate(){
            return this.rotate;
        }
        
        @Override
        public int compareTo(RotateString another){
            int thisRotate = rotate;
            int anotherRotate = another.getRotate();
            while(originalString[thisRotate] == originalString[anotherRotate]){
                if(thisRotate == originalString.length - 1){
                    thisRotate = 0;
                }else{
                    thisRotate ++;
                }
                if(anotherRotate == originalString.length - 1){
                    anotherRotate = 0;
                }else{
                    anotherRotate ++;
                }
                // all equals
                if(thisRotate == rotate){
                    return 0;
                }
            }
            if(originalString[thisRotate] < originalString[anotherRotate]){
                return -1;
            }
            else{
                return 1;
            }
            
        }
    }
}
