package physsim.global;

import java.awt.Color;

//IDEA: maximal velocity? neccessary to not jump out of world

/**	MobileEntity class
*	This class inherits from Entity and implements methods for dealing with mobile objects
*/
public class MobileEntity extends Entity{
	protected double vel [];	//in units of distance per second
	protected double mass;

	/**	MobileEntity default constructor
	*	This constructor sets visible and active to false
	*/
	public MobileEntity(){
		visible = false;
		active = false;
	}

	/**	MobileEntity parameter constructor
	*	This constructor sets visible, active and color to values given as parameter
	*	@param visibleIn: value to set visible to
	*	@param activeIn: value to set active to
	*	@param colorIn: value to set color to
	*/
	public MobileEntity(boolean visibleIn, boolean activeIn, Color colorIn){
		visible = visibleIn;
		active = activeIn;
		setColor(colorIn);
	}

	/**	setVelocity method
	*	This sets the velocity to given value.
	*	@param newVel: value to set velocity to.
	*/
	public void setVelocity(double[] newVel){
		vel = newVel;
	}

	/**	increaseVelocity method (additive)
	*	This adds given value to the velocity.
	*	@param addVel: value to offset velocity by.
	*/
	public void increaseVelocity(double[] addVel){
		vel = MyMath.add(vel, addVel);
	}

	/**	increaseVelocity method (multiplicative)
	*	This scales the velocity by given factor.
	*	@param velFactor: value to scale velocity by.
	*/
	public void increaseVelocity(double velFactor){
		vel = MyMath.scalarProd(velFactor, vel);
	}

	/**	getVelocity method
	*	This is a getter method for velocity.
	*	@returns velocity of given instance
	*/
	public double[] getVelocity(){
		return vel;
	}

	/**	getMass method
	*	This is a getter method for mass.
	*	@returns mass of given instance
	*/
	public double getMass(){
		return mass;
	}

	/**	move method
	*	This method should be overridden to implement a movement over time given as dt.
	*	@param dt: time for which the movement is computed
	*/
	public void move(long dt){
		return;
	}

	/**	checkCollision method
	*	This method should be overridden to implement a colision detection.
	*	The MobileEntity instance is checked against another Entity (mobile or not)
	*	@param ent: Entity to check against
	*	@param minframetime: time to check the collision in (movement)
	*/
	public void checkCollision(Entity ent, long minframetime){
		return;
	}

	/**	getImpulse method
	*	This method returns the current impulse (velocity * mass)
	*	@returns impulse of given instance
	*/
	public double getImpulse(){
		return MyMath.abs(MyMath.scalarProd(mass, vel));
	}

	/**	isMobile method
	*	This method returns true (overridden from Entity)
	*	@returns always true
	*/
@Override
	public boolean isMobile(){
		return true;
	}
}
