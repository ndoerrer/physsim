package physsim.server;

import java.io.Serializable;

/**	Instruction class
*	This class holds one String for a command and three for arguments.
*/
public class Instruction implements Serializable{
    private static final long serialVersionUID = 1L;
	private String command;
	private String arg1;
	private String arg2;
	private String arg3;

	/**	Instruction default constructor
	*	This constructor initializes an Instruction to command test with no arguments.
	*/
	public Instruction(){
		command = "test";
		arg1 = "";
		arg2 = "";
		arg3 = "";
	}

	/**	Instruction no args constructor
	*	This constructor initializes an Instruction to the command specified
	*	as parameter with no arguments.
	*	@param commandIn: command to set
	*/
	public Instruction(String commandIn){
		command = commandIn;
		arg1 = "";
		arg2 = "";
		arg3 = "";
	}

	/**	Instruction parameter constructor
	*	This constructor initializes an Instruction to the command specified
	*	as parameter with specified arguments.
	*	@param command_and_args: String array holding first the command, then args
	*/
	public Instruction(String [] command_and_args){
		this();
		if (command_and_args.length > 0)
			command = command_and_args[0];
		if (command_and_args.length > 1)
			arg1 = command_and_args[1];
		if (command_and_args.length > 2)
			arg2 = command_and_args[2];
		if (command_and_args.length > 3)
			arg3 = command_and_args[3];
		if (command_and_args.length > 4)
			System.out.println("arg4, ... ignored [instruction]");	//exception?
	}

	/**	getCommand method
	*	This method returns the command of the instruction instance.
	*	@returns command of the instruction
	*/
	public String getCommand(){
		return command;
	}

	/**	getArg1 method
	*	This method returns the first argument of the instruction instance.
	*	@returns first argument of the instruction
	*/
	public String getArg1(){
		return arg1;
	}

	/**	getArg2 method
	*	This method returns the second argument of the instruction instance.
	*	@returns second argument of the instruction
	*/
	public String getArg2(){
		return arg2;
	}

	/**	getArg3 method
	*	This method returns the third argument of the instruction instance.
	*	@returns third argument of the instruction
	*/
	public String getArg3(){
		return arg3;
	}

	/**	toString method
	*	This method returns a string representation of the Instruction instance.
	*	@returns string representation of Instruction instance
	*/
	public String toString(){
		return command + " " + arg1 + " " + arg2 + " " + arg3;
	}
}
