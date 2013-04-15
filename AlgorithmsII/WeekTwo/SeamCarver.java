import java.awt.Color;


public class SeamCarver {

	private Picture pic;
	
	//Tip:The data type may not mutate the Picture argument to the constructor.
	public SeamCarver(Picture picture){
		this.pic = new Picture(picture);
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
		if(x < 0 || x > width() || y < 0 || y > height()){
			throw new IndexOutOfBoundsException();
		}
		if(x == 0 || x == width() || y == 0 || y == height()){
			return 195075; //255^2 + 255^2 + 255^2
		}
		return diff(pic.get(x - 1, y),pic.get(x + 1, y)) + 
				diff(pic.get(x, y - 1),pic.get(x, y + 1));
	}
	
	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam(){
		
	}
	
	// sequence of indices for vertical seam
	public int[] findVerticalSeam(){
		
	}
	
	// remove horizontal seam from picture	   
	public void removeHorizontalSeam(int[] a){
	
	}
	
	// remove vertical seam from picture
	public void removeVerticalSeam(int[] a){
		   
	}

}
