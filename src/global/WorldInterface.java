package physsim.global;

import physsim.graphics.Camera;
import java.util.LinkedList;

import java.rmi.Remote;
import java.rmi.RemoteException;

//TODO: access permissions

/**	WorldInterface interface
*	This interface provides methods to interact with World objects.
*	It extends Remote, so it can be offerend via rmi.
*/
public interface WorldInterface extends Remote {
	public int smallestFreeIndex() throws RemoteException;
	public void setEntity(int index, Entity entity, boolean force) throws RemoteException;
	public void unsetEntity(int index) throws RemoteException;
	public int addEntity(Entity entityIn) throws RemoteException;
	public Entity getEntity(int index) throws RemoteException;
	public double[] getGravity() throws RemoteException;
	public void setGravity(double [] gravityIn) throws RemoteException;
	public double[] getDimensions() throws RemoteException;
	public double getDimensions(String which) throws RemoteException;
	public LinkedList<Entity> getEntities() throws RemoteException;
	public LinkedList<Entity> getEntities(Camera camera) throws RemoteException;
	public LinkedList<MobileEntity> getMobileEntities() throws RemoteException;
	public boolean outsideBound(double[] pos) throws RemoteException;
	//really all of them needed?
}
