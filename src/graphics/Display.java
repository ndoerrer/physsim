package physsim.graphics;

import physsim.global.*;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;

import java.util.Vector;

/**	Panel3d class
*	This class holds the properties of graphical output of physsim.
*	It handles camera views and translates coordinates according to object/camera positions.
*/
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

	/**	Panel3d default constructor
	*	This default constructor creates an instance for a default world.
	*/
	public Panel3d(){
		super();
		setPreferredSize(new Dimension(width, height));
		world = new World();
	}

	/**	Panel3d parameter constructor
	*	This constructor creates an instance for the world given as parameter.
	*	@param worldIn: world to create panel3d for (created in advance).
	*/
	public Panel3d(World worldIn){
		super();
		setPreferredSize(new Dimension(width, height));
		world = new World(worldIn);
		//TODO default camera?
	}

	/**	Panel3d extended parameter constructor
	*	This constructor creates an instance for the world given as parameter.
	*	It also initializes the camera to the given one.
	*	@param worldIn: world to create panel3d for (created in advance).
	*	@param cameraIn: camera instance to set the panel camera to.
	*/
	public Panel3d(World worldIn, Camera cameraIn){
		super();
		setPreferredSize(new Dimension(width, height));
		world = new World(worldIn);
		camera = cameraIn;
		double [] dims = world.getDimensions();
		stretchh_angle = height/2/camera.getAlphaUp();
		stretchw_angle = width/2/camera.getAlphaSide();
	}

	/**	paintComponent method
	*	This method paints the panel3d instance with all its contents.
	*	The contained entities are extracted from the world it views.
	*	@param g: graphics instance to draw into.
	*/
@Override
	protected void paintComponent(Graphics g){
		double pointh, pointw, dist;
		double[] tmp;
		int [] x, y;
		super.paintComponent(g);
		g.setColor(Color.WHITE);					//to clear panel
		g.fillRect(0, 0, width, height);
		for (Entity ent : world.getEntities(camera)){	//small world?
			if (DEBUG)
				System.out.print("\nFound entity in world:\t"+ ent.getType());
			// ===============  BALL  =============== //
			switch(ent.getType()){
				case "Ball":
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
							&& (pointw >-camera.getAlphaSide() &&
							pointw < camera.getAlphaSide()) ){
	
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
					break;
				case "Rectangle":
					Rectangle rect = (Rectangle) ent;
					x = new int[4];
					y = new int[4];
	
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
					break;
				case "Polygon":
					Polygon poly = (Polygon) ent;
					if (!poly.isInFOV(camera))
						break;
					int vertices = poly.getVertices();
					x = new int[vertices];
					y = new int[vertices];
					Vector<double []> points = poly.getPoints();
					for (int i=0; i<vertices; i++){
						tmp = MyMath.transform(points.get(i), camera);
						x[i] = (int)(width/2+tmp[0]*stretchw_angle);
						y[i] = (int)(height/2+tmp[1]*stretchh_angle);
					}
					g.setColor(poly.getColor());
					g.fillPolygon(x,y,vertices);
					//double [] center = poly.getCenter();
					//System.out.print("drew polygon at "+center[0]+" "+center[1]+" "+center[2]);
					//System.out.println(" having color "+poly.getColor());
					break;
				default:
					break;
			}
		}
	}
}

/**	Display class
*	This class holds a panel3d instance as well as camera, world as well as a frame to render
*	it all.
*/
public class Display{
	private Panel3d panel;
	private JFrame frame;
	private Camera camera;
	private World world;

	/**	Display parameter constructor
	*	This constructor creates a Display instance and initializes the world with the
	*	world given as paramter. For the camera, a default one is created.
	*/
	public Display(World world){
		this(world, new Camera(new double[]{1,0,0}, new double[]{-1,0,0}, new double[]{0,1,0}));
	}

	/**	Display extended parameter constructor
	*	This constructor creates a Display instance and initializes the world with the
	*	world given as paramter and the camera with the camera given as parameter.
	*/
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

	/**	repaint method
	*	This method calls the repaint method of the panel3d instance contained by it.
	*/
	public void repaint(){
		panel.repaint();
	}
}
