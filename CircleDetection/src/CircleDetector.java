import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
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
		
		EdgeDetector e = new EdgeDetector(350);
		CircleDetector c = new CircleDetector();
		double[][] sobelArray = e.getSobelFilteredArray(gs.getBufferedImage());
		houghArray = c.getHoughArray(sobelArray);
		GSImage gsout = new GSImage(e.getSobelFilteredArray(gs.getBufferedImage()),100);
		Graphics g = gs.getOriginalImage().getGraphics();
		g.setColor(Color.BLUE);
		g.drawOval((int) c.highestValue[1] - (int) c.highestValue[3], (int) c.highestValue[2] - (int) c.highestValue[3], (int) c.highestValue[3] * 2, (int) c.highestValue[3] * 2);
		gsout.getBufferedImage().getGraphics().setColor(Color.BLUE);
		gsout.getBufferedImage().getGraphics().drawOval((int) c.highestValue[1] - (int) c.highestValue[3], (int) c.highestValue[2] - (int) c.highestValue[3], (int) c.highestValue[3] * 2, (int) c.highestValue[3] * 2);
		ImageIcon img = new ImageIcon(gs.getOriginalImage());
		
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
	
		int smaller;
		
		if (sobelArray[0].length < sobelArray.length) {
			smaller = sobelArray[0].length;
		} else {
			smaller = sobelArray.length;
		}
		
		double[][][] houghArray = new double[sobelArray.length][sobelArray[0].length][smaller / 2];
		
		double a;
		double b;
		
		highestValue[0] = 0;
		
		System.out.println(sobelArray[0].length);
		System.out.println(sobelArray.length);
		for (double y = 0; y < (sobelArray[0].length); y=y+1) {
			for (double x = 0; x < sobelArray.length; x = x + 1) {
				
				if (sobelArray[(int) x][(int) y] != 0 ) {
				
					for (double r = smaller/20; r < smaller/2; r = r + 5) {
					
						//for (double rr = r - 5; rr < r + 6; rr++) {
						for (double t = 0; t < Math.PI * 2; t = t + 0.1) {
							a = x - r * Math.cos(t ); //polar coordinate for center * Math.PI / 180
							b = y - r * Math.sin(t );  //polar coordinate for center  * Math.PI /180
							if ( a < 0) {
								break;
							}
							if ( a > sobelArray.length - 1) {
								break;
								//a = sobelArray.length - 1;
							}
							if ( b < 0 ) {
								break;
							}
							if (b > sobelArray[0].length - 1) {
								break;
								//b = sobelArray[0].length - 1;
							}
							houghArray[(int) a][(int) b][(int) r] += 1; //voting
							if ( houghArray[(int) a][(int) b][(int) r] > highestValue[0] ) {
								highestValue[0] = houghArray[(int) a][(int) b][(int) r];
								//System.out.println(highestValue[0]);
								highestValue[1] = a;
								highestValue[2] = b;
								highestValue[3] = r;
							}
						}
						//}
					
					}
				
				}
				
			}
		}
		
		return houghArray;
		
	}
	
}
