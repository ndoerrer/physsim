package physsim.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CommandListener extends Thread{
	BufferedReader br = null;
	LinkedList<Instruction> instructions;
	private Lock mutex;

	CommandListener(){
		mutex = new ReentrantLock();
		System.out.println("shell created [CommandListener]");
		br = new BufferedReader(new InputStreamReader(System.in));
		instructions = new LinkedList<Instruction>();
	}

	protected Instruction dequeueInstruction(){
		mutex.lock();
		Instruction currentInstruction = instructions.pollFirst();
		mutex.unlock();
		return currentInstruction;
	}

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
