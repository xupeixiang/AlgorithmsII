import java.util.Arrays;

/************************************************************
 * 
 * Copyright (c) 2013, Peixiang Xu(peixiangxu@gmail.com)
 * 
 * This program is free software: you can redistribute it 
 * and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation.
 * 
 ************************************************************/
public class BurrowsWheeler {
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode(){
        StringBuffer text = new StringBuffer();
        while(!BinaryStdIn.isEmpty()){
            text.append(BinaryStdIn.readChar());
        }  
        CircularSuffixArray suffixs = new CircularSuffixArray(text.toString());
        int start = -1;
        for(int i = 0;i < text.length();i++){
            if(suffixs.index(i) == 0){
                start = i;
                break;
            }
        }
        BinaryStdOut.write(start);
        for(int i = 0;i < text.length();i++){
            int index = suffixs.index(i);
            if(index > 0){
                BinaryStdOut.write(text.charAt(index - 1));
            }
            else
                BinaryStdOut.write(text.charAt(text.length() - 1));
        }
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode(){
        if(BinaryStdIn.isEmpty())
            return;
        int originalPos = BinaryStdIn.readInt();
        StringBuffer tail = new StringBuffer();
        while(!BinaryStdIn.isEmpty()){
            tail.append(BinaryStdIn.readChar());
        }
        char[] headChars = tail.toString().toCharArray();
        Arrays.sort(headChars);
        
        // construct next 
        int[] next = new int[headChars.length];
        boolean[] find = new boolean[headChars.length];
        Arrays.fill(find, false);
        for(int i = 0; i < headChars.length; i++){
            char headChar = headChars[i];
            for(int j = 0;j < headChars.length;j++){
                if(tail.charAt(j) == headChar && !find[j]){
                    next[i] = j;
                    find[j] = true;
                    break;
                }
            }
        }
        
        int pos = originalPos;
        BinaryStdOut.write(headChars[pos]);
        for(int i = 0; i < tail.length() - 1; i++){
            pos = next[pos];
            BinaryStdOut.write(headChars[pos]);
        }
        BinaryStdOut.flush();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args){
        if(args[0].equals("-")){
            encode();
        }
        else if(args[0].equals("+")){
            decode();
        }
    }
}
