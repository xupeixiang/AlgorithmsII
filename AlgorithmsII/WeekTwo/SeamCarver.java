/************************************************************
 * 
 * Copyright (c) 2013, Peixiang Xu(peixiangxu@gmail.com)
 * 
 * This program is free software: you can redistribute it 
 * and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation.
 * 
 ************************************************************/

import java.awt.Color;
import java.util.Arrays;


public class SeamCarver {

	private Picture pic;
	private double[][] energys;
	
	//Tip:The data type may not mutate the Picture argument to the constructor.
	public SeamCarver(Picture picture){
		this.pic = new Picture(picture);
		this.energys = new double[pic.width()][pic.height()];
		this.calEnergy();
	}
	
	// current picture
	public Picture picture(){
		return this.pic;
	}
	
	// width of current picture
	public int width(){
		return this.pic.width();
	}
	
	// height of current picture
	public int height()
	{
		return this.pic.height();
	}
	
	// helper function do calculate the difference between two pixel colors
	private double diff(Color a, Color b)
	{
		return Math.pow(a.getBlue() - b.getBlue(),2) +
				Math.pow(a.getRed() - b.getRed(),2) + 
				Math.pow(a.getGreen() - b.getGreen(),2);
	}
	
	// energy of pixel at column x and row y
	public  double energy(int x, int y){
		if(x < 0 || x >= width() || y < 0 || y >= height()){
			throw new IndexOutOfBoundsException();
		}
		if(x == 0 || x == width() -1 || y == 0 || y == height() - 1){
			return 195075; //255^2 + 255^2 + 255^2
		}
		return diff(pic.get(x - 1, y),pic.get(x + 1, y)) + 
				diff(pic.get(x, y - 1),pic.get(x, y + 1));
	}
	
	// calculate all energy of current picture
	private void calEnergy(){
		for(int i = 0;i < pic.width();i++){
			for(int j = 0;j < pic.height();j++){
				energys[i][j] = energy(i, j);
			}
		}
	}
	
	// sequence of indices for vertical seam
	public int[] findVerticalSeam(){
		// distTo[i][j] = shortest path length to (j,i)
		double [][] distTo = new double[this.pic.height()][this.pic.width()];
		// initialize
		for(double[] distCol:distTo){
			Arrays.fill(distCol, Double.MAX_VALUE);
		}
		Arrays.fill(distTo[1], 0);
		// last pixel in the shortest path to(j,i),(j-1,i-1) or (j,i-1) or (j+1,i-1)
		int [][] pathTo = new int[this.pic.height()][this.pic.width()];
		
		// not including borders
		for(int i = 1;i < this.pic.height() - 1;i++){
			for(int j = 1;j < this.pic.width() - 1;j++){
				// distance of path through (j,i)
				double dist = energys[j][i] + distTo[i][j];
				
				//update three pixels
				if(dist < distTo[i+1][j-1]){
					distTo[i+1][j-1] = dist;
					pathTo[i+1][j-1] = j;
				}
				if(dist < distTo[i+1][j]){
					distTo[i+1][j] = dist;
					pathTo[i+1][j] = j;
				}
				if(dist < distTo[i+1][j+1]){
					distTo[i+1][j+1] = dist;
					pathTo[i+1][j+1] = j;
				}
			}
		}
		
		// find the shortest in last row
		int minIndex = 0;
		double minDis = Double.MAX_VALUE;
		for(int i = 0;i < pic.width();i++){
			if(distTo[pic.height() - 1][i] < minDis){
				minIndex = i;
				minDis = distTo[pic.height() - 1][i];
			}
		}
		
		// vertical seam
		int seam[] = new int[pic.height()];
		seam[pic.height() - 1] = minIndex;
		
		// find the path according to pathTo array
		for(int i = pic.height() - 2; i > 0;i--){
			seam[i] = pathTo[i+1][seam[i+1]];
		}
		seam[0] = seam[1];
		
		return seam;
	}
	
	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam(){
		
		// distTo[i][j] = shortest path length to (i,j)
		double [][] distTo = new double[this.pic.width()][this.pic.height()];
		// initialize
		for(double[] distCol:distTo){
			Arrays.fill(distCol, Double.MAX_VALUE);
		}
		Arrays.fill(distTo[1], 0);
		// last pixel in the shortest path to(i,j),(i+1,j-1) or (i+1,j) or (i+1,j+1)
		int [][] pathTo = new int[this.pic.width()][this.pic.height()];
				
		// not including borders
		for(int i = 1;i < this.pic.width() - 1;i++){
			for(int j = 1;j < this.pic.height() - 1;j++){
				// distance of path through (i,j)
				double dist = energys[i][j] + distTo[i][j];
						
				//update three pixels
				if(dist < distTo[i+1][j-1]){
					distTo[i+1][j-1] = dist;
					pathTo[i+1][j-1] = j;
				}
						
				if(dist < distTo[i+1][j]){
					distTo[i+1][j] = dist;
					pathTo[i+1][j] = j;
				}
				
				if(dist < distTo[i+1][j+1]){
					distTo[i+1][j+1] = dist;
					pathTo[i+1][j+1] = j;
				}
			}
		}
				
		// find the shortest in last row
		int minIndex = 0;
		double minDis = Double.MAX_VALUE;
		for(int i = 0;i < pic.height();i++){
			if(distTo[pic.width() - 1][i] < minDis){
				minIndex = i;
				minDis = distTo[pic.width() - 1][i];
			}
		}
				
		// horizontal seam
		int seam[] = new int[pic.width()];
		seam[pic.width() - 1] = minIndex;
				
		// find the path according to pathTo array
		for(int i = pic.width() - 2; i > 0;i--){
			seam[i] = pathTo[i+1][seam[i+1]];
		}
		seam[0] = seam[1];
				
		return seam;
	}
	
	private boolean isSeamValid(int[] a){
		for(int i = 1;i < a.length;i++){
			if(Math.abs(a[i] - a[i-1]) > 1){
				return false;
			}
		}
		return true;
	}
	
	// remove horizontal seam from picture	   
	public void removeHorizontalSeam(int[] a){
		if(a.length != pic.width() || !isSeamValid(a) || pic.height() <= 1){
			throw new IllegalArgumentException();
		}
		// new
		Picture picNew = new Picture(pic.width(), pic.height() - 1);
		double[][] energysNew = new double[pic.width()][pic.height() - 1];
		for(int i = 0;i < picNew.width();i++){
			// notice that a[i] may be the last one, so here is pic rather than picNew
			if(a[i] < 0 || a[i] >= pic.height()){
				throw new IndexOutOfBoundsException();
			}
			for(int j = 0;j < picNew.height();j++){
				if(j < a[i]){
					picNew.set(i, j, pic.get(i, j));
					energysNew[i][j] = energys[i][j];
				}
				else{
					picNew.set(i, j, pic.get(i, j + 1));
					energysNew[i][j] = energys[i][j + 1];
				}
			}
		}
		this.pic = picNew;
		
		// update energys
		for(int i = 0;i < picNew.width();i++){
			int energyChangeOne = a[i] - 1;
			int energyChangeTwo = a[i];
			if(energyChangeOne >= 0){
				energysNew[i][energyChangeOne] = energy(i, energyChangeOne);
			}
			if(energyChangeTwo < picNew.height()){
				energysNew[i][energyChangeTwo] = energy(i, energyChangeTwo);
			}
		}
		
		this.energys = energysNew;
	}
	
	// remove vertical seam from picture
	public void removeVerticalSeam(int[] a){
		if(a.length != pic.height() || !isSeamValid(a) || pic.width() <= 1){
			throw new IllegalArgumentException();
		}
		Picture picNew = new Picture(pic.width() - 1, pic.height());
		double[][] energysNew = new double[pic.width() - 1][pic.height()];
		for(int i = 0;i < picNew.height();i++){
			if(a[i] < 0 || a[i] >= pic.width()){
				throw new IndexOutOfBoundsException();
			}
			for(int j = 0;j < picNew.width();j++){
				if(j < a[i]){
					picNew.set(j, i, pic.get(j, i));
					energysNew[j][i] = energys[j][i];
				}
				else{
					picNew.set(j, i, pic.get(j + 1,i));
					energysNew[j][i] = energys[j + 1][i];
				}
					
			}
		}
		this.pic = picNew;
		// update energys
		for(int i = 0;i < picNew.height();i++){
			int energyChangeOne = a[i] - 1;
			int energyChangeTwo = a[i];
			if(energyChangeOne >= 0){
				energysNew[energyChangeOne][i] = energy(energyChangeOne,i);
			}
			if(energyChangeTwo < picNew.width()){
				energysNew[energyChangeTwo][i] = energy(energyChangeTwo,i);
		    }
		}
		this.energys = energysNew;
	}

}
