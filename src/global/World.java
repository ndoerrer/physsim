package physsim.global;

import physsim.graphics.Camera;
import java.util.HashMap;
import java.util.Vector;
import java.util.Comparator;
import java.util.LinkedList;
import java.io.Serializable;

import java.rmi.Remote;
import java.rmi.RemoteException;

// print for world entities hashMap
//TODO: access permissions
//idea: logfile!!
//name: revengin

/**	EntityDistComparator class
*	This class is a Comparator for Entity instances.
*	An Entity instance is 'smaller' if its farthest distance is is larger, so
*	if the object is further away from the camera.
*/
class EntityDistComparator implements Comparator<Entity>{
	private Camera camera;

	/**	EntityDistComparator parameter constructor
	*	This constructor creates a Comparator instance and initializes the Camera object.
	*	@param cameraIn: Camera to initialize the comparator
	*/
	public EntityDistComparator(Camera cameraIn){
		camera = cameraIn;
	}

	/**	compare method
	*	This method performs the comparisson of two entities.
	*	An Entity instance is 'smaller' if its farthest distance is is larger, so
	*	if the object is further away from the camera.
	*	@param first: first entity in comparisson
	*	@param second: second entity in comparisson
	*	@return 1 if greater, -1 if smaller, 0 if equal
	*/
@Override
	public int compare(Entity first, Entity second){
		if (first.farthestDistance(camera.getPos()) < second.farthestDistance(camera.getPos()))
			return 1;
		if (first.farthestDistance(camera.getPos()) > second.farthestDistance(camera.getPos()))
			return -1;
		return 0;
	}
}

/**	World class
*	This class holds all the properties necessary for a world in the engine.
*	It implements WorldInterface and Serializable.
*/
public class World implements WorldInterface, Serializable{
	static final long serialVersionUID = 1L;

	private final boolean DEBUG = false;
	private HashMap<Integer,Entity> entities;
	private double xmin;
	private double xmax;
	private double ymin;
	private double ymax;
	private double zmin;
	private double zmax;
	private double [] gravity;

	/**	World default constructor
	*	This constructor creates a world instance with default values of 0.
	*	World boundaries are set to be all 0.
	*/
	public World(){
		xmin = xmax = ymin = ymax = zmin = zmax = 0;
		entities = new HashMap<Integer,Entity>();
		gravity = new double[] {0, 0, 0};
	}

	/**	World parameter constructor
	*	This constructor initializes a world instance by setting x, y and z boundaries.
	*	@param xminIn: lower x boundary
	*	@param xmaxIn: upper x boundary
	*	@param yminIn: lower y boundary
	*	@param ymaxIn: upper y boundary
	*	@param zminIn: lower z boundary
	*	@param zmaxIn: upper z boundary
	*/
	public World(double xminIn, double xmaxIn, double yminIn, double ymaxIn,
												double zminIn, double zmaxIn){
		this();
		xmin = xminIn;
		xmax = xmaxIn;
		ymin = yminIn;
		ymax = ymaxIn;
		zmin = zminIn;
		zmax = xmaxIn;
	}

	/**	World copy constructor
	*	This constructor initializes a world instance by copying values from another
	*	world instance.
	*	@param worldIn: world instance to copy
	*/
	public World(World worldIn){
		this(worldIn.getDimensions("xmin"), worldIn.getDimensions("xmax"),	
				worldIn.getDimensions("ymin"),worldIn.getDimensions("ymax"),
				worldIn.getDimensions("zmin"),worldIn.getDimensions("zmax"));
		entities = new HashMap<Integer,Entity>(worldIn.entities);
	}

	/**	smallestFreeIndex method
	*	This method returns the next free id that would be used when adding a new entity
	*	to the world.
	*	@returns smallest free index.
	*/
	public int smallestFreeIndex(){	//TODO optimize! start at num? when?
		int i = 0;
		int num = entities.size();
		for (i=0; i<num; i++){
			if (!entities.containsKey(i))
				return i;
		}
		return i+1;
	}

	/**	setEntity method
	*	This method sets a new Entity instance to an index in the world.
	*	If force is true, it can overwrite entities
	*	previously inserted under this index.
	*	@param index: index to insert entity at
	*	@param entity: entity instance to insert
	*	@param force: if true, overwrites entities with given index
	*/
	public void setEntity(int index, Entity entity, boolean force){
		if (!force && entities.containsKey(index)){
			System.out.println("Overwriting of entities denied!");
			return;
		}
		entities.put(index,entity);
	}

	/**	unsetEntity method
	*	This method removes an entity instance with given index from the world.
	*	@param index: index to free
	*/
	public void unsetEntity(int index){
		if (entities.containsKey(index))
			return;
		entities.remove(index);
	}

	/**	addEntity method
	*	This method adds a new Entity to the world, by inserting it at an unused index.
	*	@param entityIn: entity instance to add
	*	@returns index of the added Entity
	*/
	public int addEntity(Entity entityIn){
		if (DEBUG)
			System.out.print("adding Entity of type "+entityIn.getType());
		int index = smallestFreeIndex();
		if (DEBUG)
			System.out.println("\tat index"+index);
		setEntity(index, entityIn, false);
		return index;
	}

	/**	getEntity method
	*	This method returns the Entity to given index.
	*	@param index: index of the requested entity
	*	@returns Entity at given index
	*/
	public Entity getEntity(int index){
		return entities.get(index);
	}

	/**	getGravity method
	*	This method returns the gravity vector of the world instance.
	*	@returns vector being the gravity acceleration of the world
	*/
	public double[] getGravity(){
		return new double[] {gravity[0], gravity[1], gravity[2]};
	}

	/**	setGravity method
	*	This method sets the gravity acceleration vector for the world instance.
	*	@param gravityIn: gravity vector to set world gravity to
	*/
	public void setGravity(double [] gravityIn){
		gravity = new double [] {gravityIn[0], gravityIn[1], gravityIn[2]};
	}

	/**	getDimensions method
	*	This method returns all Dimensions of given world instance.
	*	@returns vector containing all world boundaries
	*/
	public double[] getDimensions(){
		double[] alldims = {xmin,xmax,ymin,ymax,zmin,zmax};
		return alldims;
	}

	/**	getDimensions method
	*	This method returns one specific world boundary.
	*	@param which: Name of the desired boundary
	*	@returns value of the desired boundary as double
	*/
	public double getDimensions(String which){
		switch(which){
			case "xmin":	return xmin;
			case "xmax":	return xmax;
			case "ymin":	return ymin;
			case "ymax":	return ymax;
			case "zmin":	return zmin;
			case "zmax":	return zmax;
			default:
				System.out.println("Specify xmin, xmax, ymin, ymax, zmin or zmax.");
				return 0;
		}
	}

	/**	getEntities method
	*	This method returns a linked list of all Entities in the world instance.
	*	@returns linked list of all Entities in the world instance
	*/
	public LinkedList<Entity> getEntities(){
		return new LinkedList<Entity>(entities.values());
	}

	//TODO: lazy evaluation?
	/**	getEntities method
	*	This method returns a linked list of all Entities in FoV of a camera object.
	*	It is sorted by distance (farthest first)
	*	@param camera: 
	*	@returns linked list of all Entities in the world instance
	*/
	public LinkedList<Entity> getEntities(Camera camera){
		LinkedList<Entity> result = new LinkedList<Entity>();
		for(Entity ent : entities.values()){
			if (ent.isInFOV(camera))
				result.add(ent);
		}
		result.sort(new EntityDistComparator(camera));
		return result;
	}

	/**	getMobileEntities method
	*	This returns a linked list of all mobile Entities in the world instance.
	*	@returns list of all mobile Entities
	*/
	public LinkedList<MobileEntity> getMobileEntities(){
		LinkedList<MobileEntity> result = new LinkedList<MobileEntity>();
		for(Entity ent : entities.values()){
			if (ent.isMobile())
				result.add((MobileEntity) ent);
		}
		return result;
	}

	/**	outsideBound method
	*	This method returns whether a point is inside of the world.
	*	@param pos: point to check
	*	@returns true, if point is inside world
	*/
	public boolean outsideBound(double[] pos){
		return (pos[0]<getDimensions("xmin") && pos[0]>getDimensions("xmax")
				&& pos[1]<getDimensions("ymin") && pos[1]>getDimensions("ymax")
				&& pos[2]<getDimensions("zmin") && pos[2]>getDimensions("zmax") );
	}
}
