import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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
		
		JFileChooser jfc = new JFileChooser("C:\\Users\\Oliver\\Desktop");
		
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
		
		ArrayList<Circle> circles = c.getHoughCircles(sobelArray);
		Graphics g = gs.getOriginalImage().getGraphics();
		g.setColor(Color.BLUE);
		for (Circle circle : circles) {
			g.drawOval(circle.x - circle.r, circle.y - circle.r, circle.r * 2, circle.r * 2);
		}
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
	
	public ArrayList<Circle> getHoughCircles(double[][] sobelArray) {
		
		ArrayList<Circle> circles = new ArrayList<Circle>();
		
		int smaller;
		
		double high = 0;
		double highr = 0;
		
		int total = 0;
		
		if (sobelArray[0].length < sobelArray.length) {
			smaller = sobelArray[0].length;
		} else {
			smaller = sobelArray.length;
		}
		
		double[][][] houghArray = new double[sobelArray.length][sobelArray[0].length][smaller / 2];
		
		double a;
		double b;
		
		System.out.println(sobelArray[0].length);
		System.out.println(sobelArray.length);
		for (double y = 0; y < (sobelArray[0].length); y = y + 1) {
			for (double x = 0; x < sobelArray.length; x = x + 1) {
				
				if (sobelArray[(int) x][(int) y] != 0 ) {
				
					for (double r = smaller/2 - 1; r > smaller/12; r = r - 5) {
					
						for (double t = 0; t < Math.PI * 2; t = t + 0.1) {
							a = x - r * Math.cos(t ); //polar coordinate for center * Math.PI / 180
							b = y - r * Math.sin(t );  //polar coordinate for center  * Math.PI /180
							if ( a < 0) {
								continue;
							}
							if ( a > sobelArray.length - 1) {
								continue;
							}
							if ( b < 0 ) {
								continue;
							}
							if (b > sobelArray[0].length - 1) {
								continue;
							}
							houghArray[(int) a][(int) b][(int) r] += 1; //voting
							total = total + 1;
							ArrayList<Circle> toRemove = new ArrayList<Circle>();
							if (houghArray[(int) a][(int) b][(int) r] > 20 && houghArray[(int) a][(int) b][(int) r] > high/1.75 && r > highr/2) {
								int area =smaller/11;
								if (circles.size() == 0) {
									circles.add(new Circle((int)a,(int)b,(int)r,houghArray[(int) a][(int) b][(int) r]));
								} else {
									Iterator<Circle> it = circles.iterator();
									boolean broken = false;
									Circle c;
									while (it.hasNext()) {
										c = it.next();
										if (c.i >= high/1.75 && c.r >= r/2) {
											if (c.x <= a + area && c.x >= a - area  && c.y <= b + area && c.y >= b - area) {
												if (c.i < houghArray[(int) a][(int) b][(int) r] || (c.i/1.5 < houghArray[(int) a][(int) b][(int) r] && r > c.r)) {
													if (houghArray[(int) a][(int) b][(int) r] > high) {
														high = houghArray[(int) a][(int) b][(int) r];
													}
													if (r > highr) {
														highr = r;
													}
													circles.remove(c);
													circles.add(new Circle((int)a,(int)b,(int)r,houghArray[(int) a][(int) b][(int) r]));
													broken  =true;
													break;
												} else {
													broken = true;
													break;
												}
											}
										} else {
											toRemove.add(c);
										}
									}
									if (!broken) {
										circles.add(new Circle((int)a,(int)b,(int)r,houghArray[(int) a][(int) b][(int) r]));
										if (houghArray[(int) a][(int) b][(int) r] > high) {
											high = houghArray[(int) a][(int) b][(int) r];
										}
										if (r > highr) {
											highr = r;
										}
									} else {
										broken = false;
									}
									for (Circle cr : toRemove) {
										circles.remove(cr);
									}
								}
								r = smaller/12;
								break;
								
							}
							
						}
					
					}
				
				}
				
			}
		}
		
		return circles;
		
	}
	
}
