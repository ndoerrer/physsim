package physsim.server;

import java.util.Date;
import java.util.LinkedList;
import physsim.global.*;

/**	Engine class
*	This class handles physsim engine execution.
*	It connections CommandListener and world and computes changes of entities over time.
*	It is intended that this class and its run method are run as separate thread.
*/
public class Engine extends Thread{
	private final int MINFRAMETIME = 20;	//milliseconds - TODO: as parameter
	private boolean outputs;
	private World world;
	private CommandListener shell;
	private long time;
	private long init_time;

	/**	Engine parameter constructor
	*	This constructor sets world to the one provided as parameter.
	*	@param worldIn: world to set the engine to.
	*/
	public Engine(World worldIn){
		outputs = true;
		world = worldIn;
	}

	/**	Engine extended parameter constructor
	*	This constructor sets world to the one provided as parameter. Also sets
	*	outputs to control whether the Engine itself should print some outputs.
	*	@param worldIn: world to set the engine to.
	*	@param outputsIn: if true, sets outputs to true.
	*/
	public Engine(World worldIn, boolean outputsIn){
		outputs = outputsIn;
		world = worldIn;
	}

	/**	init method
	*	This method initializes the time variables and starts a CommandListener
	*	shell as separate thread.
	*	@returns: returncode (right now only )
	*/
	public int init(){
		init_time = new Date().getTime();
		time = 0;
		shell = new CommandListener();
		shell.start();
		return 0;
	}

	/**	run_engine method
	*	This is the method where all engine running stuff happens.
	*	It handles instructions originating from the CommandListener and takes care
	*	of events and movements happening in the simulation.
	*	@returns: returncode (right now only )
	*/
	public int run_engine(){
		LinkedList<Entity> all_ents;
		Instruction currentInstruction;
		Date date;
		long time_new, dt;
		int second_counter = 0;
		int iteration_counter = 0;
		boolean shutdown=false;
		double impulse = 0.0;
		while (!shutdown){
			iteration_counter ++;
			time_new = new Date().getTime() - init_time;
			dt = time_new - time;
			if (dt < MINFRAMETIME){
				try {
					Thread.sleep(MINFRAMETIME - dt);
				} catch (Exception e) {		//TODO: specify
					System.out.println(e);
				}
			}
			time_new = new Date().getTime() - init_time;
			dt = time_new - time;
			time = time_new;
			if (time >= second_counter*1000){
				if (outputs){
					if (second_counter > 0)
						System.out.println("time: " + time + "\t" +
									iteration_counter +
									" iterations\ttotal impulse: "+impulse);
					else
						System.out.println("time: "+time);
				}
				second_counter++;
				iteration_counter = 0;
			}

			currentInstruction = shell.dequeueInstruction();
			if (currentInstruction != null && outputs)
				System.out.println("fetched instruction: " + currentInstruction
								+ " [Engine]");

			impulse = 0;
			for(MobileEntity mob_ent : world.getMobileEntities()){
				mob_ent.move(dt);
				impulse += MyMath.abs(MyMath.scalarProd(mob_ent.getMass(),
														mob_ent.getVelocity()));
				if (mob_ent.getType() == "Ball"){
					Ball ball = (Ball) mob_ent;
					if(world.outsideBound(ball.getPos()) && outputs)
						System.out.println("warning!! entity leaving world");
				}
			}

			all_ents = world.getEntities();
			for(MobileEntity mob_ent : world.getMobileEntities()){
				for(Entity ent : all_ents)
					mob_ent.checkCollision(ent, time);
			}
		}
		return 0;
	}

	/**	run method
	*	This method creates an Engine object, initiates and calls run_engine()
	*/
@Override
	public void run(){//TODO: LOG
		Engine engine = new Engine(world);
		engine.init();
		engine.run_engine();
	}
}
