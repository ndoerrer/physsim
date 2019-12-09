package physsim.global;

import java.awt.Color;

//IDEA: maximal velocity? neccessary to not jump out of world
public class MobileEntity extends Entity{
	protected double vel [];	//in units of distance per second
	protected double mass;

	public MobileEntity(){
		visible = false;
		active = false;
	}

	public MobileEntity(boolean visibleIn, boolean activeIn, Color colorIn){
		visible = visibleIn;
		active = activeIn;
		setColor(colorIn);
	}

	public void setVelocity(double[] newVel){
		vel = newVel;
	}

	public void increaseVelocity(double[] newVel){
		vel = MyMath.add(vel, newVel);
	}

	public void increaseVelocity(double velFactor){
		vel = MyMath.scalarProd(velFactor, vel);
	}

	public double[] getVelocity(){
		return vel;
	}

	public double getMass(){
		return mass;
	}


	public void move(long dt){
		return;
	}

	public void checkCollision(Entity ent, long minframetime){
		return;
	}

	public double getImpulse(){
		return MyMath.abs(MyMath.scalarProd(mass, vel));
	}

@Override
	public boolean isMobile(){
		return true;
	}
}
