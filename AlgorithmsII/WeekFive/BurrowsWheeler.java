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
        String text = StdIn.readLine();
        CircularSuffixArray suffixs = new CircularSuffixArray(text);
        int i = 0;
        for(;i < text.length();i++){
            if(suffixs.index(i) == 0){
                break;
            }
        }
        BinaryStdOut.write(i);
        for(char c:suffixs.tail().toCharArray()){
            BinaryStdOut.write(c);
        }
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode(){
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
        
        StringBuffer text = new StringBuffer(tail.length());
        int pos = originalPos;
        text.append(headChars[pos]);
        for(int i = 0; i < tail.length() - 1; i++){
            pos = next[pos];
            text.append(headChars[pos]);
        }
        
        for(char c:text.toString().toCharArray()){
            BinaryStdOut.write(c);
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
