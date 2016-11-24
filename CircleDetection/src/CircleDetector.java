import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class is the main class for the circle detection algorithm
 * This uses the sobel operator and hough transform
 * 
 * @author Oliver Martin (ojm1g16@ecs.soton.ac.uk)
 *
 */
public class CircleDetector {
	
	double highestValue[]= new double[4];
	
	public static void main(String args[]) {
		
		//CircleDetector c = new CircleDetector();
		//c.loadImageFromFile("C:\\Users\\Oliver\\Desktop\\300px-Valve_original_(1).PNG");
		//c.loadImageFromFile("C:\\Users\\Oliver\\Desktop\\200px-Bikesgray.jpg");
		//c.loadImageFromFile("C:\\Users\\Oliver\\Desktop\\s-l300.jpg");
		
		JFileChooser jfc = new JFileChooser("C:\\Users\\Oliver\\Desktop");
		
		double[][][] houghArray;
		
		GSImage gs = null;
		jfc.showOpenDialog(null);
		try {
			gs = new GSImage(jfc.getSelectedFile().getAbsolutePath());
		} catch (IOException e) {
			System.err.println("Couldn't read file");
		}
		
		EdgeDetector e = new EdgeDetector(150);
		CircleDetector c = new CircleDetector();
		double[][] sobelArray = e.getSobelFilteredArray(gs.getBufferedImage());
		houghArray = c.getHoughArray(sobelArray);
		for (int i = 0; i < c.highestValue[3]; i++) {
			sobelArray[(int) c.highestValue[1] + i][(int) c.highestValue[2] + i] = sobelArray[(int) c.highestValue[1] + i][(int) c.highestValue[2] + i] * 10000000;
		}
		GSImage gsout = new GSImage(e.getSobelFilteredArray(gs.getBufferedImage()),100);
		
		ImageIcon img = new ImageIcon(gsout.getBufferedImage());
		
		JFrame frame = new JFrame("IMAGE");
	      frame.setVisible(true);
	      frame.setSize(500,500);
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      JLabel label = new JLabel("", img, JLabel.CENTER);
	      JPanel panel = new JPanel(new BorderLayout());
	      panel.add( label, BorderLayout.CENTER );
	      frame.add(panel);
		
	}
	
	public double[][][] getHoughArray(double[][] sobelArray) {
		
		double[][][] houghArray = new double[sobelArray.length][sobelArray[0].length][1000];
		
		double a;
		double b;
		
		highestValue[0] = 0;
		
		for (double y = 0; y < sobelArray[0].length - 10; y = y + 10) {
			for (double x = 0; x < sobelArray.length - 10; x = x + 10) {
				
				if (sobelArray[(int) x][(int) y] != 0 ) {
				
					for (double r = 100; r < 200; r++) {
					
						for (double t = 0; t < 360; t = t + 10) {
							a = x - r * Math.cos(t * Math.PI / 180); //polar coordinate for center
							b = y - r * Math.sin(t * Math.PI /180);  //polar coordinate for center 
							houghArray[(int) a][(int) b][(int) r] += 1; //voting
							if ( houghArray[(int) a][(int) b][(int) r] > highestValue[0] ) {
								highestValue[0] = houghArray[(int) a][(int) b][(int) r];
								highestValue[1] = a;
								highestValue[2] = b;
								highestValue[3] = r;
							}
						}
					
					}
				
				}
				
			}
		}
		
		return houghArray;
		
	}
	
}
