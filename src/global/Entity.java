package physsim.global;

import physsim.graphics.Camera;
import java.awt.Color;

import java.io.Serializable;

// idea: cluster of entities -> if one addressed, all are taken into consideration.
// list of points which determine if visible (gridlike for wall?)
// move drawing to entity itself?

/**	Entity class
*	This class represents the top of engine-compatible objects inheritance structure.
*	It defines an Id and some basic status variables held by all objects in the engine.
*/
public class Entity implements Serializable{
	static final long serialVersionUID = 1L;
	protected static long id = 0;
	protected long myId;
	protected boolean visible;
	protected boolean active;
	protected Color color = Color.RED;

	/**	Entity default constructor
	*	This constructor gives an Id and sets visible and active to False
	*/
	public Entity(){
		visible = false;
		active = false;
		myId = id++;
	}

	/**	Entity parameter constructor
	*	This constructor gives an Id and sets visible and active to given paramters.
	*	@param visibleIn: value to set visible to
	*	@param activeIn: value to set active to
	*/
	public Entity(boolean visibleIn, boolean activeIn){
		visible = visibleIn;
		active = activeIn;
		myId = id++;
	}

	/**	Entity extended constructor
	*	This constructor gives an Id and sets visible and active to given paramters.
	*	Also sets a color (TODO: really needed?).
	*	@param visibleIn: value to set visible to
	*	@param activeIn: value to set active to
	*	@param colorIn: value to set color to
	*/
	public Entity(boolean visibleIn, boolean activeIn, Color colorIn){
		visible = visibleIn;
		active = activeIn;
		setColor(colorIn);
		myId = id++;
	}

	/**	isVisible method
	*	Returns value of visible variable.
	*	@returns true if visible is true, else false
	*/
	public boolean isVisible(){
		return visible;
	}

	/**	isActive method
	*	Returns value of active variable.
	*	@returns true if active is true, else false
	*/
	public boolean isActive(){
		return active;
	}

	/**	getColor method
	*	Returns value of color varibale.
	*	@returns color of the entity
	*/
	public Color getColor(){
		return color;
	}

	/**	setColor method
	*	Setter for the color variable.
	*	@param colorIn: value to set color to.
	*/
	public void setColor(Color colorIn){
		color = colorIn;
	}

	/**	isMobile method
	*	Returns false unless overridden (MobilieEntity).
	*	@returns false
	*/
	public boolean isMobile(){
		return false;
	}

	/**	getType method
	*	Returns the type of Entity as string. Should be overridden.
	*	@returns type of Entity
	*/
	public String getType(){
		return "Entity";
	}

	/**	getId method
	*	Returns the id of Entity instance.
	*	@returns id of Entity instance
	*/
	public long getId(){
		return myId;
	}

	/**	isInFOV method
	*	Returns whether Entity is in field of view of given camera instance.
	*	@param camera: Camera instance to check FoV of
	*	@returns true, if Entity instance is in FoV of the camera
	*/
	public boolean isInFOV(Camera camera){
		return true; // should be overridden by extending classes
	}

	/**	farthestDistance method
	*	Returns 0. Should be overridden by extending classes
	*	@param point: point to compute distance to
	*	@returns 0
	*/
	public double farthestDistance(double[] point){
		return 0.0; // should be overridden by extending classes TODO
	}

	/**	closestDistance method
	*	Returns 0. Should be overridden by extending classes
	*	@param point: point to compute distance to
	*	@returns 0
	*/
	public double closestDistance(double[] point){
		return 0.0; // should be overridden by extending classes TODO
	}
}
