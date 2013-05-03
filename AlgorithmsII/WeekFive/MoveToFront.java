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
    	
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode(){
    	
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
