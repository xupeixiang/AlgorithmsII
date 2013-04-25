/************************************************************
 * 
 * Copyright (c) 2013, Peixiang Xu(peixiangxu@gmail.com)
 * 
 * This program is free software: you can redistribute it 
 * and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation.
 * 
 ************************************************************/

public class Outcast {
	
	private WordNet wordNet = null;
	
	// constructor takes a WordNet object
	public Outcast(WordNet wordnet){
		wordNet = wordnet;
	}

	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns){
		int length = nouns.length;
		int[][] distances = new int[length][length];
		for(int i = 0;i < length; i++){
			for(int j = i + 1;j < length; j++){
				int distanceIJ = wordNet.distance(nouns[i], nouns[j]);
				distances[i][j] = distances[j][i] = distanceIJ;
			}
		}
		
		int maxDistance = 0;
		int outcastId = -1;
		
		for(int k = 0;k < length;k++){
			int distanceK = 0;
			for(int m = 0; m < length;m++){
				distanceK += distances[k][m];
			}
			if(distanceK > maxDistance){
				maxDistance = distanceK;
				outcastId = k;
			}
		}
		return nouns[outcastId];
	}

	// for unit testing of this class (such as the one below)
	public static void main(String[] args){
		
	}
}
