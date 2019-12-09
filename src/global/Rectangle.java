package physsim.global;

import physsim.graphics.Camera;
import java.awt.Color;

public class Rectangle extends Entity{
	private final boolean DEBUG = false;
	private double [] A;
	private double [] B;
	private double [] C;
	private double [] D;
	private double [] normal;

	public Rectangle(double [] AIn, double [] BIn, double [] CIn, double [] DIn){
		A = new double [] {AIn[0], AIn[1], AIn[2]};
		B = new double [] {BIn[0], BIn[1], BIn[2]};
		C = new double [] {CIn[0], CIn[1], CIn[2]};
		D = new double [] {DIn[0], DIn[1], DIn[2]};
		active = true;
		visible = true;
		normal = MyMath.norm(MyMath.cross(MyMath.sub(A, C),	MyMath.sub(A, B)));
		myId = id++;
	}

	public Rectangle(double [] AIn, double [] BIn, double [] CIn, double [] DIn, Color colorIn){
		this(AIn,BIn,CIn,DIn);
		color = colorIn;
	}

	public double[] getA(){
		return A;
	}

	public double[] getB(){
		return B;
	}

	public double[] getC(){
		return C;
	}

	public double[] getD(){
		return D;
	}

	public double[] getNormal(){
		return normal;
	}

	/**
	gets you the normal facing towards you
	*/
	public double[] getNormal(double[] vec){
		if (MyMath.dot(vec,normal)<0)
			return MyMath.scalarProd(-1,normal);
		return normal;
	}

	public double[] getCenter(){
		return new double[]{(A[0]+B[0]+C[0]+D[0])/4, (A[1]+B[1]+C[1]+D[1])/4,
												(A[2]+B[2]+C[2]+D[2])/4};
	}

@Override
	public boolean isInFOV(Camera camera){
		if (MyMath.dot(MyMath.norm(MyMath.sub(getA(), camera.getPos())),
					camera.getFocus()) <= 0 &&
					MyMath.dot(MyMath.norm(MyMath.sub(getB(), camera.getPos())),
					camera.getFocus()) <= 0 &&
					MyMath.dot(MyMath.norm(MyMath.sub(getC(), camera.getPos())),
					camera.getFocus()) <= 0 && 
					MyMath.dot(MyMath.norm(MyMath.sub(getD(), camera.getPos())),
					camera.getFocus()) <= 0)
			return false;	//TODO in focus? or not?
		return true;
	}

@Override
	public double closestDistance(double[] point){
		if (Math.abs(MyMath.dot(MyMath.sub(A, point), normal)) < 0.5 && DEBUG)
			System.out.println("DEBUG: closestDistance is "+Math.abs(MyMath.dot(MyMath.sub(A, point), normal)));
		return Math.abs(MyMath.dot(MyMath.sub(A, point), normal));

	}

@Override		// min of distances to the nodes
	public double farthestDistance(double[] point){
		double dist = Math.sqrt(Math.max(Math.max(
					MyMath.dot(MyMath.sub(point,A), MyMath.sub(point,A)),
					MyMath.dot(MyMath.sub(point,B), MyMath.sub(point,B))), Math.max(
					MyMath.dot(MyMath.sub(point,C), MyMath.sub(point,C)),
					MyMath.dot(MyMath.sub(point,D), MyMath.sub(point,D))) ));
		if (dist < 0.5 && DEBUG)
			System.out.println("DEBUG: farthestDistance is "+dist);
		return dist;
	}

@Override
	public String getType(){
		return "Rectangle";
	}
}
