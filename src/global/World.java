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

class EntityDistComparator implements Comparator<Entity>{
	private Camera camera;

	public EntityDistComparator(Camera cameraIn){
		camera = cameraIn;
	}

@Override
	public int compare(Entity first, Entity second){
		if (first.farthestDistance(camera.getPos()) < second.farthestDistance(camera.getPos()))
			return 1;
		if (first.farthestDistance(camera.getPos()) > second.farthestDistance(camera.getPos()))
			return -1;
		return 0;
	}
}

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

	public World(){
		xmin = xmax = ymin = ymax = zmin = zmax = 0;
		entities = new HashMap<Integer,Entity>();
		gravity = new double[] {0, 0, 0};
	}

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

	public World(World worldIn){
		this(worldIn.getDimensions("xmin"), worldIn.getDimensions("xmax"),	
				worldIn.getDimensions("ymin"),worldIn.getDimensions("ymax"),
				worldIn.getDimensions("zmin"),worldIn.getDimensions("zmax"));
		entities = new HashMap<Integer,Entity>(worldIn.entities);
	}

	public int smallestFreeIndex(){	//TODO optimize! start at num? when?
		int i = 0;
		int num = entities.size();
		for (i=0; i<num; i++){
			if (!entities.containsKey(i))
				return i;
		}
		return i+1;
	}

	public void setEntity(int index, Entity entity, boolean force){
		if (!force && entities.containsKey(index)){
			System.out.println("Overwriting of entities denied!");
			return;
		}
		entities.put(index,entity);
	}

	public void unsetEntity(int index){
		if (entities.containsKey(index))
			return;
		entities.remove(index);
	}

	public int addEntity(Entity entityIn){
		if (DEBUG)
			System.out.print("adding Entity of type "+entityIn.getType());
		int index = smallestFreeIndex();
		if (DEBUG)
			System.out.println("\tat index"+index);
		setEntity(index, entityIn, false);
		return index;
	}

	public Entity getEntity(int index){
		return entities.get(index);
	}

	public double[] getGravity(){
		return new double[] {gravity[0], gravity[1], gravity[2]};
	}

	public void setGravity(double [] gravityIn){
		gravity = new double [] {gravityIn[0], gravityIn[1], gravityIn[2]};
	}

	public double[] getDimensions(){
		double[] alldims = {xmin,xmax,ymin,ymax,zmin,zmax};
		return alldims;
	}

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

	public LinkedList<Entity> getEntities(){
		return new LinkedList<Entity>(entities.values());
	}

	//TODO: lazy evaluation?
	// this is sorted by distance (farthest first)
	public LinkedList<Entity> getEntities(Camera camera){
		LinkedList<Entity> result = new LinkedList<Entity>();
		for(Entity ent : entities.values()){
			if (ent.isInFOV(camera))
				result.add(ent);
		}
		result.sort(new EntityDistComparator(camera));
		return result;
	}

	public LinkedList<MobileEntity> getMobileEntities(){
		LinkedList<MobileEntity> result = new LinkedList<MobileEntity>();
		for(Entity ent : entities.values()){
			if (ent.isMobile())
				result.add((MobileEntity) ent);
		}
		return result;
	}

	public boolean outsideBound(double[] pos){
		return (pos[0]<getDimensions("xmin") && pos[0]>getDimensions("xmax")
				&& pos[1]<getDimensions("ymin") && pos[1]>getDimensions("ymax")
				&& pos[2]<getDimensions("zmin") && pos[2]>getDimensions("zmax") );
	}
}
//idea: logfile!!
//name: revengin
