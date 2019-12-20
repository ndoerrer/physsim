package physsim.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**	CommandListener class
*	This class implements the basic functionality of a shell being able to
*	inject instructions by enqueueing in a way that engine instances an read
*	handle them. It is meant to be run as a separate thread.
*/
public class CommandListener extends Thread{
	BufferedReader br = null;
	LinkedList<Instruction> instructions;
	private Lock mutex;

	/**	CommandListener default constructor
	*	This constructor initializes the mutex and creates a Buffered reader to
	*	receive instructions from. It also creates a List for the Instructions.
	*/
	CommandListener(){
		mutex = new ReentrantLock();
		System.out.println("shell created [CommandListener]");
		br = new BufferedReader(new InputStreamReader(System.in));
		instructions = new LinkedList<Instruction>();
	}

	/**	dequeueInstruction method
	*	This method removes the first Instruction from the queue and returns it.
	*	@returns: next Instruction
	*/
	protected Instruction dequeueInstruction(){
		mutex.lock();
		Instruction currentInstruction = instructions.pollFirst();
		mutex.unlock();
		return currentInstruction;
	}

	/**	parseInput method 
	*	This method processes a given input to create an Instruction from.
	*	TODO
	*	@param input: Input string to parse
	*	@returns Instruction instance or null if invalid string
	*/
	private Instruction parseInput(String input){
		if (input.charAt(0) != '!'){
			input = input.replace(" ","_");
			String [] parts = new String [] {"say", input};
			System.out.println("say: " + input);
			return new Instruction(parts);
		}
		else{
			input = input.replace("!","");
			String [] parts = input.split(" ");
			if (parts.length > 0 && parts.length < 5)
				return new Instruction(parts);
			else
				return null;
		}
	}

	/**	run method
	*	This method is the thread main to be run in parallel to the engine.
	*	It handles catching and parsing input as well as queueing.
	*/
@Override
	public void run(){
		String input;
		Instruction instruction;
		System.out.println("shell started [CommandListener]");
		try{
			while(true){
				input = br.readLine();
				instruction = parseInput(input);
				if (instruction != null){
					mutex.lock();
					instructions.add(instruction);
					mutex.unlock();
				}
				else
					System.out.println("instruction " + input +
										" ignored [CommandListener]");
			}
		} catch(IOException e){
			System.out.println("exception in readLine [CommandListener]");
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
