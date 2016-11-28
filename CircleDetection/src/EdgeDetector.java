import java.awt.image.BufferedImage;
import java.awt.image.Raster;

/**
 * Given an image or array of greyscale values this will perform
 * Sobel filtering on the input data in order to produce an output
 * where the edges have been highligted and ready for further processing
 * @author Oliver
 *
 */
public class EdgeDetector {

	private final double kernelx[][] = new double[][]{{-1, 0, 1},
		  {-2, 0, 2},
		  {-1, 0, 1}};
	private final double kernely[][] = new double[][]{{-1, -2, -1},
		   {0,  0,  0},
		   {1,  2,  1}};
	private int THRESHOLD;
	
	public EdgeDetector() {
		THRESHOLD = 350;
	}
	public EdgeDetector(int threshold) {
		THRESHOLD = threshold;
	}
	
	/**
	 * Converts a buffered image to an array of doubles
	 * Applies the Sobel Operator to all pixels in the array
	 * Then filters out all values below a threshold
	 * @param image
	 * @return double array after sobel filter has been applied
	 */
	public double[][] getSobelFilteredArray(BufferedImage image) {
		
		Raster raster = image.getData();
		double[][] inputArray = new double[image.getWidth()][image.getHeight()];
		
		for (int j = 0; j < image.getWidth(); j++) {
		    for (int k = 0; k < image.getHeight(); k++) {
		        inputArray[j][k] = raster.getSample(j, k, 0);
		    }
		}
		
		return getSobelFilteredArray(inputArray, image.getWidth(), image.getHeight());
		
	}
	
	/**
	 * Applies the Sobel Operator to all pixels in the array
	 * Then filters out all values below a threshold
	 * @param inputArray
	 * @return double array after sobel filter has been applied
	 */
	public double[][] getSobelFilteredArray(double[][] inputArray, int width, int height) {
		
		inputArray = getGaussianFilteredArray(inputArray, width, height);
		
		double[][] outputArray = new double[width][height];
		
		double xValue;
		double yValue;
		
		for (int x = 1 ; x < width - 1 ; x++) {
			
			for (int y = 1 ; y < height - 1; y++) {
				
				xValue = 0;
				yValue = 0;
				
				for (int xk = 2 ; xk >= 0 ; xk -- ) {
					
					for (int yk = 2 ; yk >= 0 ; yk -- ) {
						
						xValue = xValue + (kernelx[xk][yk] * inputArray[x + 1 - xk][y - 1 +yk]);
						yValue = yValue + (kernely[xk][yk] * inputArray[x + 1 - xk][y - 1 +yk]);
						
					}
					
				}
				
				outputArray[x][y] = Math.sqrt(xValue*xValue + yValue*yValue); //* Math.sqrt(xValue*xValue + yValue*yValue)/2 ;
				
				//ADDITIONAL FILTER to remove "noise"
				if (outputArray[x][y] < THRESHOLD) {
					outputArray[x][y] = 0;
				}
				
			}
			
		}
		
		return outputArray;
	}
	
public double[][] getGaussianFilteredArray(double[][] inputArray, int width, int height) {
		
	double gaussian[][] = {{0.0126,0.0252,0.0314,0.0252,0.0126},
		{0.0252,0.0566,0.0755,0.0566,0.0252},
		{0.0314,0.0755,0.0943,0.0755,0.0314},
		{0.0252,0.0566,0.0755,0.0566,0.0252},
		{0.0126,0.0252,0.0314,0.0252,0.0126}};
	
		double[][] outputArray = new double[width][height];
		
		double value;
		
		for (int x = 2; x < width - 2; x++) {
			
			for (int y = 2 ; y < height - 2; y++) {
				
				value = 0;
				
				for (int xk = 4 ; xk >= 0 ; xk -- ) {
					
					for (int yk = 4 ; yk >= 0 ; yk -- ) {
						
						value = value + (gaussian[xk][yk] * inputArray[x + 2 - xk][y - 2 +yk]);
						
					}
					
				}
				
				outputArray[x][y] = value; //* Math.sqrt(xValue*xValue + yValue*yValue)/2 ;
			}
			
		}
		
		return outputArray;
	}

}