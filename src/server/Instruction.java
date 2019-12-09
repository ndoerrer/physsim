package physsim.server;

import java.io.Serializable;

public class Instruction implements Serializable{
    private static final long serialVersionUID = 1L;
	private String command;
	private String arg1;
	private String arg2;
	private String arg3;

	public Instruction(){
		command = "test";
		arg1 = "";
		arg2 = "";
		arg3 = "";
	}

	public Instruction(String commandIn){
		command = commandIn;
		arg1 = "";
		arg2 = "";
		arg3 = "";
	}

	public Instruction(String [] commandAndArgs){
		this();
		if (commandAndArgs.length > 0)
			command = commandAndArgs[0];
		if (commandAndArgs.length > 1)
			arg1 = commandAndArgs[1];
		if (commandAndArgs.length > 2)
			arg2 = commandAndArgs[2];
		if (commandAndArgs.length > 3)
			arg3 = commandAndArgs[3];
		if (commandAndArgs.length > 4)
			System.out.println("arg4, ... ignored [instruction]");	//exception?
	}

	public String getCommand(){
		return command;
	}

	public String getArg1(){
		return arg1;
	}

	public String getArg2(){
		return arg2;
	}

	public String getArg3(){
		return arg3;
	}

	public String toString(){
		return command + " " + arg1 + " " + arg2 + " " + arg3;
	}
}
