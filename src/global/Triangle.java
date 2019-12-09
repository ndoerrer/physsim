package physsim.global;

import java.awt.Color;

public class Triangle extends Entity{
	private double [] A;
	private double [] B;
	private double [] C;

	public Triangle(double [] AIn, double [] BIn, double [] CIn){
		A = new double [] {AIn[0], AIn[1], AIn[2]};
		B = new double [] {BIn[0], BIn[1], BIn[2]};
		C = new double [] {CIn[0], CIn[1], CIn[2]};
	}

@Override
	public String getType(){
		return "Triangle";
	}
}
