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
	
	public static void main(String args[]) {
		
		//CircleDetector c = new CircleDetector();
		//c.loadImageFromFile("C:\\Users\\Oliver\\Desktop\\300px-Valve_original_(1).PNG");
		//c.loadImageFromFile("C:\\Users\\Oliver\\Desktop\\200px-Bikesgray.jpg");
		//c.loadImageFromFile("C:\\Users\\Oliver\\Desktop\\s-l300.jpg");
		
		JFileChooser jfc = new JFileChooser("C:\\Users\\Oliver\\Desktop");
		
		GSImage gs = null;
		jfc.showOpenDialog(null);
		try {
			gs = new GSImage(jfc.getSelectedFile().getAbsolutePath());
		} catch (IOException e) {
			System.err.println("Couldn't read file");
		}
		
		EdgeDetector e = new EdgeDetector(150);
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
	
}
