import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Represents and stores a grey scale image
 * Can take an image, file path or array as the constructor
 * @author Oliver
 *
 */
public class GSImage {

	private BufferedImage image;
	private BufferedImage fullColour;
	
	public GSImage(BufferedImage image) {
		Image scaled = image.getScaledInstance(-1, 200, Image.SCALE_SMOOTH);
		  BufferedImage bufferedScaled = new BufferedImage(scaled.getWidth(null),  scaled.getHeight(null), BufferedImage.TYPE_INT_RGB);
		  bufferedScaled.getGraphics().drawImage(scaled, 0, 0, null);
		fullColour = bufferedScaled;
		convertToGreyScale(bufferedScaled);
	}
	public GSImage(String filePath) throws IOException {
		BufferedImage image = loadImageFromFile(filePath);
		Image scaled = image.getScaledInstance(-1, 200, Image.SCALE_SMOOTH);
		  BufferedImage bufferedScaled = new BufferedImage(scaled.getWidth(null),  scaled.getHeight(null), BufferedImage.TYPE_INT_RGB);
		  bufferedScaled.getGraphics().drawImage(scaled, 0, 0, null);
		fullColour = bufferedScaled;
		convertToGreyScale(bufferedScaled);
	}
	public GSImage(double[][] greyScaleArray, int contrastMultiplier) {
		BufferedImage image = new BufferedImage(greyScaleArray.length, greyScaleArray[0].length, BufferedImage.TYPE_BYTE_GRAY);
		for(int y = 0; y < image.getHeight(); y++){
		    for(int x = 0; x < image.getWidth(); x++){
		        int value = (int)Math.round(greyScaleArray[x][y])*contrastMultiplier;
		        image.setRGB(x, y, value);
		    }
		}
		this.image = image;
		this.fullColour = image;
	}
	
	private BufferedImage loadImageFromFile(String filePath) throws IOException {
		
		BufferedImage image;
		image = ImageIO.read(new File(filePath));
	    return image;
	    
	}
	
	private void convertToGreyScale(BufferedImage image) {
		this.image = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = this.image.getGraphics();
	    g.drawImage(image, 0, 0, null);
	    g.dispose();
	}
	
	public BufferedImage getBufferedImage() {
		return image;
	}
	
	public double[][] getArray() {
		
		Raster raster = image.getData();
		double[][] outputArray = new double[image.getWidth()][image.getHeight()];
		
		for (int j = 0; j < image.getWidth(); j++) {
		    for (int k = 0; k < image.getHeight(); k++) {
		        outputArray[j][k] = raster.getSample(j, k, 0);
		    }
		}
		
		return outputArray;
		
	}

	public BufferedImage getOriginalImage() {
		return fullColour;
	}
}