/************************************************************
 * 
 * Copyright (c) 2013, Peixiang Xu(peixiangxu@gmail.com)
 * 
 * This program is free software: you can redistribute it 
 * and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation.
 * 
 ************************************************************/

public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode(){
    
        //sequences intialize
        char[] seqs = new char[256];
        for(int i = 0;i < 256;i++){
            seqs[i] = (char)i;
        }
        
        while(!BinaryStdIn.isEmpty()){
            char c = BinaryStdIn.readChar();
            char replaceChar = seqs[0];
            for(int i = 0;i < seqs.length;i++){
                char temp = seqs[i];
                if(seqs[i] == c){
                    BinaryStdOut.write(i, 8);
                    BinaryStdOut.flush();
                    seqs[i] = replaceChar;
                    break;
                }else{
                    seqs[i] = replaceChar;
                    replaceChar = temp;
                }
            }   
            seqs[0] = c;
        }     
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode(){
        //sequences intialize
        char[] seqs = new char[256];
        for(int i = 0;i < 256;i++){
            seqs[i] = (char)i;
        }
        
        while(!BinaryStdIn.isEmpty()){
            char c = BinaryStdIn.readChar();
            int index = (int)c;
            char code = seqs[index];
            BinaryStdOut.write(code);
            BinaryStdOut.flush();
            for(int i = index;i > 0;i--){
                seqs[i] = seqs[ i - 1];
            }   
            seqs[0] = code;
        }
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args){
        if(args[0].equals("-")){
            encode();
        }
        else if(args[0].equals("+")){
            decode();
        }
    }
}
