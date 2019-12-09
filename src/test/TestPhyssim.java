package physsim.exec;

import java.awt.Color;
import physsim.global.*;
import physsim.server.Engine;
import physsim.graphics.Display;
import physsim.graphics.Camera;
import java.util.Date;

public class TestPhyssim{
	public static World makeWorld(int N){
		World world = new World(-20,20,-20,20,-20,20);

		double maxspeed = 5;
		double size = 20.0;
		double radius = 1, x, y, z;//0.5
		Color color;
		double[] pos;
		double[] vel;
		int [] entities = new int[N];
		float rand;
		for (int i=0; i<N; i++){
			x = (double)((int)(Math.random()*2*(size-1)-size+1));
			y = (double)((int)(Math.random()*2*(size-1)-size+1));
			z = (double)((int)(Math.random()*2*(size-1)-size+1));
			pos = new double[] {x,y,z};
			x = Math.random()*maxspeed*2-maxspeed;
			y = Math.random()*maxspeed*2-maxspeed;
			z = Math.random()*maxspeed*2-maxspeed;
			vel = new double[] {x,y,z};
			rand = (float)Math.random();
			color = new Color(Color.HSBtoRGB(rand, 1.0f, 1.0f));
			entities[i] = world.addEntity(new Ball(pos, vel, radius, color));
		}

		//world.addEntity(new Ball(new double[]{0,0,0}, new double[]{1,1,1}, 0.5, Color.RED));
		//world.addEntity(new Ball(new double[]{-3,-3,-3}, new double[]{2,2,2}, 0.5, Color.BLUE));
		//world.addEntity(new Ball(new double[]{0,3,3}, new double[]{0,-2,-2}, 0.5, Color.BLUE));

		//world.addEntity(new Ball(new double[]{0,0,0}, new double[]{0,0,0}, 0.5, Color.RED));
		//world.addEntity(new Ball(new double[]{0,1,-1}, new double[]{0,-0.1,0.1}, 0.5, Color.BLUE));
		//world.addEntity(new Ball(new double[]{0,-1,-1}, new double[]{0,0.1,0.1}, 0.5, Color.BLUE));

/*		int wall_up = world.addEntity(new Rectangle(new double[]{-20,-20,20},
												new double[]{-20,20,20},
												new double[]{20,20,20},
												new double[]{20,-20,20}, Color.YELLOW));
		int wall_down = world.addEntity(new Rectangle(new double[]{-20,-20,-20},
												new double[]{-20,20,-20},
												new double[]{20,20,-20},
												new double[]{20,-20,-20}, Color.YELLOW));
		int wall_right = world.addEntity(new Rectangle(new double[]{-20,20,-20},
												new double[]{-20,20,20},
												new double[]{20,20,20},
												new double[]{20,20,-20}, Color.GREEN));
		int wall_left = world.addEntity(new Rectangle(new double[]{-20,-20,-20},
												new double[]{-20,-20,20},
												new double[]{20,-20,20},
												new double[]{20,-20,-20}, Color.GREEN));
		int wall_front = world.addEntity(new Rectangle(new double[]{-20,-20,-20},
												new double[]{-20,-20,20},
												new double[]{-20,20,20},
												new double[]{-20,20,-20}, Color.PINK));
		int wall_back = world.addEntity(new Rectangle(new double[]{20,-20,-20},
												new double[]{20,-20,20},
												new double[]{20,20,20},
												new double[]{20,20,-20}, Color.PINK));
*/

		int wall_up = world.addEntity(new Polygon(new double[]{-20,-20,20},
												new double[]{-20,20,20},
												new double[]{20,20,20},
												new double[]{20,-20,20}, Color.YELLOW));
		int wall_down = world.addEntity(new Polygon(new double[]{-20,-20,-20},
												new double[]{-20,20,-20},
												new double[]{20,20,-20},
												new double[]{20,-20,-20}, Color.YELLOW));
		int wall_right = world.addEntity(new Polygon(new double[]{-20,20,-20},
												new double[]{-20,20,20},
												new double[]{20,20,20},
												new double[]{20,20,-20}, Color.GREEN));
		int wall_left = world.addEntity(new Polygon(new double[]{-20,-20,-20},
												new double[]{-20,-20,20},
												new double[]{20,-20,20},
												new double[]{20,-20,-20}, Color.GREEN));
		int wall_front = world.addEntity(new Polygon(new double[]{-20,-20,-20},
												new double[]{-20,-20,20},
												new double[]{-20,20,20},
												new double[]{-20,20,-20}, Color.PINK));
		int wall_back = world.addEntity(new Polygon(new double[]{20,-20,-20},
												new double[]{20,-20,20},
												new double[]{20,20,20},
												new double[]{20,20,-20}, Color.PINK));

		return world;
	}

	public static void main(String [] args){
		World world = makeWorld(10);
		Camera camera = new Camera(new double[]{20,0,0}, new double[]{-1,0,0},
														new double[]{0,1,0});
		Display display = new Display(world, camera);

		new Engine(world).start();
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
					//System.out.println("time(c): "+time+"\t"+iteration_counter+" iterations");
				}
				else
					//System.out.println("time(c): "+time);
				second_counter++;
				iteration_counter = 0;
			}
			display.repaint();
		}
	}
}
