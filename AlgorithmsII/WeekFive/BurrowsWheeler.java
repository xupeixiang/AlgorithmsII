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
