package physsim.global;

import physsim.graphics.Camera;
import java.awt.Color;

import java.io.Serializable;

// idea: cluster of entities -> if one addressed, all are taken into consideration.
// make wall and ball inherit from entity? package physsim.global.shapes;
// list of points which determine if visible (gridlike for wall?)
// also cuboid?
// move drawing to entity itself?
public class Entity implements Serializable{
	static final long serialVersionUID = 1L;
	protected static long id = 0;
	protected long myId;
	protected boolean visible;
	protected boolean active;
	protected Color color = Color.RED;

	public Entity(){
		visible = false;
		active = false;
		myId = id++;
	}

	public Entity(boolean visibleIn, boolean activeIn){
		visible = visibleIn;
		active = activeIn;
		myId = id++;
	}

	public Entity(boolean visibleIn, boolean activeIn, Color colorIn){
		visible = visibleIn;
		active = activeIn;
		setColor(colorIn);
		myId = id++;
	}

	public boolean isVisible(){
		return visible;
	}

	public boolean isActive(){
		return active;
	}

	public Color getColor(){
		return color;
	}

	public void setColor(Color colorIn){
		color = colorIn;
	}

	public boolean isMobile(){
		return false;
	}

	public String getType(){
		return "Entity";
	}

	public long getId(){
		return myId;
	}

	public boolean isInFOV(Camera camera){
		return true; // should be overridden by extending classes
	}

	public double farthestDistance(double[] point){
		return 0.0; // should be overridden by extending classes TODO
	}

	public double closestDistance(double[] point){
		return 0.0; // should be overridden by extending classes TODO
	}
}
