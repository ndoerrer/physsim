package physsim.global;

import physsim.graphics.Camera;
import java.awt.Color;
import java.util.Vector;
import java.lang.Math;

public class Polygon extends Entity{
	private final boolean DEBUG = false;
	private Vector<double []> points;
	private double [] normal;
	private int vertices;

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

	public Polygon(Vector<double []> pointsIn, Color colorIn){
		this(pointsIn);
		color = colorIn;
	}

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

	public Polygon(double [] a, double [] b, double [] c, double [] d, Color colorIn){
		this(a, b, c, d);
		color = colorIn;
	}

	public Vector<double[]> getPoints(){
		return new Vector<double []>(points);
	}

	public double[] getNormal(){
		return normal;
	}

	/**
	gets you the normal facing towards you //TODO: really needed?
	*/
	public double[] getNormal(double[] vec){
		if (MyMath.dot(vec,normal)<0)
			return MyMath.scalarProd(-1,normal);
		return normal;
	}

	public double[] getCenter(){
		double [] center = {0.0,0.0,0.0};
		for (double [] point : points){
			center[0] += point[0];
			center[1] += point[1];
			center[2] += point[2];
		}
		center[0] /= vertices;
		center[1] /= vertices;
		center[2] /= vertices;
		return center;
	}

@Override
	public boolean isInFOV(Camera camera){
		double alpha_r = camera.getAlphaR();
		//if (MyMath.angle(camera.getFocus(), normal) > Math.PI/2.0)
		if (MyMath.dot(normal, camera.getFocus()) > 0.0)
			return false;	//facing away
		for(double [] point : points){
			if(MyMath.dot(MyMath.norm(MyMath.sub(point, camera.getPos())),
									camera.getFocus()) < 0.0); // angle > pi/2
				return true;
		}
		return false;
	}

@Override
	public double closestDistance(double[] point){
		double dist = 0, measurement;
		for (double [] vertex : points){
			measurement = MyMath.abs(MyMath.sub(point, vertex));
			if (dist < measurement)
				dist = measurement;
		}
		System.out.println("DEBUG: point1 = " + points.elementAt(0)[0] + ","
						+ points.elementAt(0)[1] + "," + points.elementAt(0)[2]);
		System.out.println("DEBUG: point2 = " + points.elementAt(1)[0] + ","
						+ points.elementAt(1)[1] + "," + points.elementAt(1)[2]);
		System.out.println("DEBUG: point3 = " + points.elementAt(2)[0] + ","
						+ points.elementAt(2)[1] + "," + points.elementAt(2)[2]);
		System.out.println("DEBUG: normal = " + normal[0] + "," + normal[1] + "," + normal[2]);
		//TODO
		//System.out.println("DEBUG: closestDistance is " + dist);
		return dist;
	}

@Override		// min of distances to the nodes
	public double farthestDistance(double[] point){
		double dist = Double.MAX_VALUE, measurement;
		for (double [] vertex : points){
			measurement = MyMath.abs(MyMath.sub(point, vertex));
			if (dist > measurement)
				dist = measurement;
		}
		if (dist < 0.5 && DEBUG)
			System.out.println("DEBUG: farthestDistance is "+dist);
		return dist;
	}

@Override
	public String getType(){
		return "Polygon";
	}
}
