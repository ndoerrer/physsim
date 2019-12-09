package physsim.graphics;

import physsim.global.*;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;

class Panel3d extends JPanel{
	private final boolean DEBUG = false;
	private World world;
	private Camera camera;
	//TODO resizing window should not resize this panel!!
	private final int height = 1000;//1000;
	private final int width = 1600;
	private final double stretch = 2000;
	private double stretchh_angle;
	private double stretchw_angle;

	public Panel3d(){
		super();
		setPreferredSize(new Dimension(width, height));
		world = new World();
	}

	public Panel3d(World worldIn){
		super();
		setPreferredSize(new Dimension(width, height));
		world = new World(worldIn);
		//TODO default camera?
	}

	public Panel3d(World worldIn, Camera cameraIn){
		super();
		setPreferredSize(new Dimension(width, height));
		world = new World(worldIn);
		camera = cameraIn;
		double [] dims = world.getDimensions();
		stretchh_angle = height/2/camera.getAlphaUp();
		stretchw_angle = width/2/camera.getAlphaSide();
	}

@Override
	protected void paintComponent(Graphics g){
		double pointh, pointw, dist;
		double[] tmp;
		super.paintComponent(g);
		g.setColor(Color.WHITE);					//to clear panel
		g.fillRect(0, 0, width, height);
		for (Entity ent : world.getEntities(camera)){	//small world?
			if (DEBUG)
				System.out.print("\nFound entity in world:\t"+ ent.getType());
			// ===============  BALL  =============== //
			if (ent.getType() == "Ball"){
				Ball ball = (Ball) ent;
				if (DEBUG)
					System.out.println("\tX:"+ball.getPosX()+"\tY:"+ball.getPosY()+
													"\tZ:"+ball.getPosZ());
				tmp = MyMath.transform(ball.getPos(), camera);
				pointw = tmp[0];
				pointh = tmp[1];
				dist = tmp[2];
				if (DEBUG)
					System.out.println("pointw="+pointw+"\tpointh="+pointh);

				if ( (pointh > -camera.getAlphaUp() && pointh < camera.getAlphaUp())
						&& (pointw >-camera.getAlphaSide() && pointw < camera.getAlphaSide()) ){

					int diameterw = (int)(ball.getRadius()*2/dist*stretchw_angle);
					int diameterh = (int)(ball.getRadius()*2/dist*stretchh_angle);
					g.setColor(ball.getColor());	
					g.fillOval((int)(width/2+pointw*stretchw_angle-diameterw/2),
								(int)(height/2+pointh*stretchh_angle-diameterh/2),
								diameterw, diameterh );
					if (DEBUG){
						System.out.print("\tWidth:"+(int)(width/2+pointw*stretchw_angle));
						System.out.println("\tHeight:"+(int)(height/2+pointh*stretchh_angle));
					}
				}
			}
			// ===============  RECTANGLE  =============== //
			else if (ent.getType() == "Rectangle"){
				Rectangle rect = (Rectangle) ent;
				int [] x = new int[4];
				int [] y = new int[4];
				double [] center = rect.getCenter();
				if (DEBUG)
					System.out.println(" with center at:\tX:" + center[0] + "\tY:"+center[1]
																	+ "\tZ:"+center[2]);

				tmp = MyMath.transform(rect.getA(), camera);
				x[0] = (int)(width/2+tmp[0]*stretchw_angle);
				y[0] = (int)(height/2+tmp[1]*stretchh_angle);
				tmp = MyMath.transform(rect.getB(), camera);
				x[1] = (int)(width/2+tmp[0]*stretchw_angle);
				y[1] = (int)(height/2+tmp[1]*stretchh_angle);
				tmp = MyMath.transform(rect.getC(), camera);
				x[2] = (int)(width/2+tmp[0]*stretchw_angle);
				y[2] = (int)(height/2+tmp[1]*stretchh_angle);
				tmp = MyMath.transform(rect.getD(), camera);
				x[3] = (int)(width/2+tmp[0]*stretchw_angle);
				y[3] = (int)(height/2+tmp[1]*stretchh_angle);
				if (DEBUG)
					System.out.println(x[0]+","+y[0]+"\t"+x[1]+","+y[1]+"\t"+x[2]+","+y[2]+
										"\t"+x[3]+","+y[3]+"\t"+rect.getColor());
				//TODO: check if inside fov
				// ideas:  - if ABCD over top, under bottom, left, right... -> cont
				//		   - store center and smallest way to bound in rect and check
				//				cam diagonal < rect center - rect smallestToBound -> cont
				// 		also for balls!! (radius)
				// pass graphics to ent classes to make them draw? or compute width and height?
				g.setColor(rect.getColor());
				g.fillPolygon(x,y,4);
			}
		}
	}
}

public class Display{
	private Panel3d panel;
	private JFrame frame;
	private Camera camera;
	private World world;

	public void repaint(){
		panel.repaint();
	}

	public Display(World world){
		this(world, new Camera(new double[]{1,0,0}, new double[]{-1,0,0}, new double[]{0,1,0}));
	}

	public Display(World world, Camera camera){
		frame = new JFrame("physsim");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		panel = new Panel3d(world, camera);

		frame.add(panel, BorderLayout.CENTER);
		frame.setMinimumSize(new Dimension(800,800));
		frame.pack();
		frame.setVisible(true);
	}
}
