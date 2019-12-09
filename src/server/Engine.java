package physsim.server;

import java.util.Date;
import java.util.LinkedList;
import physsim.global.*;

public class Engine extends Thread{
	private final int MINFRAMETIME = 20;	//milliseconds - TODO: as parameter
	private boolean outputs;
	private World world;
	private CommandListener shell;
	private long time;
	private long init_time;

	public int init(){
		init_time = new Date().getTime();
		time = 0;
		shell = new CommandListener();
		shell.start();
		return 0;
	}

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

	public Engine(World worldIn){
		outputs = true;
		world = worldIn;
	}

	public Engine(World worldIn, boolean outputsIn){
		outputs = outputsIn;
		world = worldIn;
	}

@Override
	public void run(){//LOG
		Engine engine = new Engine(world);
		engine.init();
		engine.run_engine();
	}
}
