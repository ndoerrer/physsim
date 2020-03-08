package physsim.global;

import physsim.graphics.Camera;
import java.awt.Color;
import java.util.Vector;
import java.lang.Math;

/**	Polygon class
*	This class is used to represent any polygonal shaped 2d entities.
*	Vertex points are stored in a vector. A Normal vector is also stored.
*/
public class Polygon extends Entity{
	private final boolean DEBUG = false;
	private Vector<double []> points;
	private double [] normal;
	private int vertices;

	/**	Polygon parameter constructor
	*	This constructor creates a Polygon based of the points given as parameters.
	*	@param pointsIn: points to create Polygon from.
	*/
	public Polygon(Vector<double []> pointsIn){
		points = new Vector<double []>(pointsIn);
		vertices = points.size();
		//TODO: error if vertices < 3
		active = true;
		visible = true;
		// surface normal after right hand rule
		normal = MyMath.norm(MyMath.cross(MyMath.sub(points.get(2), points.get(0)),
						MyMath.sub(points.get(1), points.get(0))));
		myId = id++;
	}

	/**	Polygon extended parameter constructor
	*	This constructor creates a Polygon based of the points given as parameters.
	*	Additionally the color is set according to the parameter.
	*	@param pointsIn: points to create Polygon from.
	*	@param colorIn: color to set Polygon to.
	*/
	public Polygon(Vector<double []> pointsIn, Color colorIn){
		this(pointsIn);
		color = colorIn;
	}

	/**	Polygon quadrangle parameter constructor
	*	This constructor creates a quadrangle based of the 4 points given as parameters.
	*	@param a: first vertex of the quadrangle.
	*	@param b: second vertex of the quadrangle.
	*	@param c: third vertex of the quadrangle.
	*	@param d: fourth vertex of the quadrangle.
	*/
	public Polygon(double [] a, double [] b, double [] c, double [] d){
		points = new Vector<double []>();
		points.add(a);
		points.add(b);
		points.add(c);
		points.add(d);
		vertices = points.size();
		//TODO: error if vertices < 3
		active = true;
		visible = true;
		normal = MyMath.norm(MyMath.cross(MyMath.sub(points.get(0), points.get(2)),
						MyMath.sub(points.get(0), points.get(1))));
		myId = id++;
	}

	/**	Polygon quadrangle extended parameter constructor
	*	This constructor creates a quadrangle based of the 4 points given as parameters.
	*	Additionally the color is set according to the parameter.
	*	@param a: first vertex of the quadrangle.
	*	@param b: second vertex of the quadrangle.
	*	@param c: third vertex of the quadrangle.
	*	@param d: fourth vertex of the quadrangle.
	*	@param colorIn: color to set Polygon to.
	*/
	public Polygon(double [] a, double [] b, double [] c, double [] d, Color colorIn){
		this(a, b, c, d);
		color = colorIn;
	}

	/**	getPoints method
	*	This is a getter method for the points.
	*	@returns points of the Polygon as vector of double arrays.
	*/
	public Vector<double[]> getPoints(){
		return new Vector<double []>(points);
	}

	/**	getNormal method
	*	This is a getter method for the surface normal.
	*	@returns normal of the Polygon as double array.
	*/
	public double[] getNormal(){
		return normal;
	}

	/**	getNormal method
	*	This method returns the Polygon surface normal facing towards the provided point.
	*	TODO: really needed?
	*	@param vec: point to compute normal facing towards.
	*	@returns normal of the Polygon facing towards point as double array.
	*/
	public double[] getNormal(double[] vec){
		if (MyMath.dot(vec,normal)<0)
			return MyMath.scalarProd(-1,normal);
		return normal;
	}

	/**	getCenter method
	*	This method computes the Center of the Polygon.
	*	@returns double array representing the Polygon center.
	*/
	public double[] getCenter(){
		double [] center = {0.0,0.0,0.0};
		for (double [] point : points)
			center = MyMath.add(center, point);
		MyMath.scalarProd(center, 1.0/vertices);
		return center;
	}

	/**	getVertices method
	*	This is a getter method for the number of vertices of the Polygon instance.
	*	@returns number of vertices of the Polygon.
	*/
	public int getVertices(){
		return vertices;
	}

	/**	isInFOV method
	*	This method evaluates whether the Polygon is in the field of view of given
	*	camera instance.
	*	@param camera: camera instance to check fov of.
	*	@returns true, if Polygon instance is in FoV of the camera.
	*/
@Override
	public boolean isInFOV(Camera camera){
		double alpha_r = camera.getAlphaR();
		//if (MyMath.angle(camera.getFocus(), normal) > Math.PI/2.0)
		if (MyMath.dot(normal, camera.getFocus()) > 0.0)
			return false;	//facing away TODO test
		//System.out.println(camera);
		for(double [] point : points){
			//System.out.println(point[0]+","+point[1]+","+point[2]);
			if(MyMath.dot(MyMath.norm(MyMath.sub(point, camera.getPos())),
									camera.getFocus()) > 0.0) // angle < pi/2
				return true;
		}
		return false;
	}

	/**	closestDistance method
	*	This method computes and returns the closest distance of a given point to the
	*	Polygon. TODO: right now it only works if the projection of the point onto the
	*	Polygon plane is inside the Polygon.
	*	@param point: point to compute distance to.
	*	@returns distance from Polygon to given point.
	*/
@Override
	public double closestDistance(double[] point){
		// cos(alpha) = |a*normal| / (|a|*|n|)
		// only works if normal is pointing towards point to check (wall facing it)
		// only works if orthogonal projection of the point is inside polygon area TODO
		return Math.abs(MyMath.dot(MyMath.sub(points.get(0), point),
									MyMath.scalarProd(normal, -1.0)));
	}

	/**	farthestDistance method
	*	This method computes and returns the farthest distance of a given point to the
	*	Polygon.
	*	@param point: point to compute distance to.
	*	@returns farthest distance from Polygon to given point.
	*/
@Override		// min of distances to the nodes
	public double farthestDistance(double[] point){
		double dist = 0.0, measurement;
		for (double [] vertex : points){
			measurement = MyMath.abs(MyMath.sub(point, vertex));
			if (measurement > dist)
				dist = measurement;
		}
		if (dist < 0.5 && DEBUG)
			System.out.println("DEBUG: farthestDistance is "+dist);
		return dist;
	}

	/**	getType method
	*	This method always returns the string Polygon (overridden from MobileEntity).
	*	@returns: Polygon
	*/
@Override
	public String getType(){
		return "Polygon";
	}
}
