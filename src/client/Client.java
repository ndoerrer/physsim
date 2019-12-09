package physsim.client;

import java.awt.Color;
import physsim.global.*;
import physsim.server.Engine;
import physsim.graphics.Display;
import physsim.graphics.Camera;
import java.util.Date;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Client{
	private static World findWorld(String host, String name, int port) {
		World w = null;
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry();
			//registry = LocateRegistry.getRegistry("rmi://" + host + ":" + port);

			//w = (World) registry.lookup("rmi://" + host + ":" + port + "/" + name);
			w = (World) registry.lookup(name);
			System.out.println ("World (" + name + ") found");
		} catch (Exception e) {
			System.out.println("Error in find method: World ("+name+") not found");
			System.exit(1);
		}
		return w;
	}

	public static void main(String [] args){
		World world = findWorld("localhost", "physsim-test", 1099);
		// hardcoded is bad!
		Camera camera = new Camera(new double[]{20,0,0}, new double[]{-1,0,0},
														new double[]{0,1,0});
		Display display = new Display(world, camera);

		new Engine(world, false).start();
		Date date = new Date();
		long init_time = date.getTime(), time = 0, time_new, iteration_counter = 0, dt;
		int second_counter = 0;
		final int MINFRAMETIME = 50;	//milliseconds
		while (true){
			iteration_counter ++;
			date = new Date();
			time_new = date.getTime() - init_time;
			dt = time_new - time;
			time = time_new;
			if (dt < MINFRAMETIME){
				try {
					Thread.sleep(MINFRAMETIME - dt);
				} catch (Exception e) {
					System.out.println(e);
				}
			}
			if (time >= second_counter*1000){
				if (second_counter > 0){
					System.out.println("time(c): "+time+"\t"+iteration_counter+" iterations");
				}
				else
					System.out.println("time(c): "+time);
				second_counter++;
				iteration_counter = 0;
			}
			display.repaint();
		}
	}
}
