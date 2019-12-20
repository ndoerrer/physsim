package physsim.graphics;

//TODO render distance
/**	Camera class
*	This class implements the basic functionality of Cameras
*	It has a position, a focus and a head, all being vectors (focus and head orthogonal
*	normed vectors). In addition it has an opening angle for displaying things.
*/
public class Camera{
	private double[] pos;
	private double[] focusvec;
	private double[] headvec;
	private double alpha_up;
	private double alpha_side;
	private double alpha_r;

	/**	Camera default constructor
	*	This constructor initializes the camera object to position 0,0,0
	*	Focus becomes 1,0,0  and head is 0,1,0
	*/
	public Camera(){
		pos = new double[] {0,0,0};
		focusvec = new double[] {1,0,0};
		headvec = new double[] {0,1,0};
	}

	/**	Camera parameter constructor
	*	This constructor initializes a Camera object to have position, focus
	*	and head as given as parameters.
	*	@param posIn: position for the Camera
	*	@param focusvecIn: focus for the Camera
	*	@param headvecIn: head for the Camera
	*/
	public Camera(double[] posIn, double[] focusvecIn, double[] headvecIn){
		pos = new double [] {posIn[0], posIn[1], posIn[2]};
		focusvec = new double [] {focusvecIn[0], focusvecIn[1], focusvecIn[2]};
		headvec = new double [] {headvecIn[0], headvecIn[1], headvecIn[2]};
		alpha_up = Math.PI*3/16;
		alpha_side = Math.PI/4;
		alpha_r = alpha_side*Math.cos(Math.atan(alpha_up/alpha_side));
	}

	/**	Camera extended parameter constructor
	*	This constructor initializes a Camera object to have position, focus
	*	and head as given as parameters. In addition the opening vectors to up and
	*	side are given.
	*	@param posIn: position for the Camera
	*	@param focusvecIn: focus for the Camera
	*	@param headvecIn: head for the Camera
	*	@param alpha_upIn: alpha_up for the camera (opening angle)
	*	@param alpha_sideIn: alpha_side for the camera (opening angle)
	*/
	public Camera(double[] posIn, double[] focusvecIn, double[] headvecIn,
									double alpha_upIn, double alpha_sideIn){
		this(posIn, focusvecIn, headvecIn);
		alpha_up = alpha_upIn;
		alpha_side = alpha_sideIn;
		alpha_r = alpha_side*Math.cos(Math.atan(alpha_up/alpha_side));
	}

	/**	getPos method
	*	This method returns the current position of the Camera instance.
	*	@returns position of the camera
	*/
	public double[] getPos(){
		return pos.clone();
	}

	/**	getFocus method
	*	This method returns the current focus vector of the Camera instance.
	*	@returns focus vector of the camera
	*/
	public double[] getFocus(){
		return focusvec.clone();
	}

	/**	getHead method
	*	This method returns the current head vector of the Camera instance.
	*	@returns head vector of the camera
	*/
	public double[] getHead(){
		return headvec.clone();
	}

	/**	getAlphaUp method
	*	This method returns the current Alpha up opening angle of the Camera instance.
	*	@returns alpha_up of the Camera
	*/
	public double getAlphaUp(){
		return alpha_up;
	}

	/**	getAlphaSide method
	*	This method returns the current Alpha side opening angle of the Camera instance.
	*	@returns alpha_side of the Camera
	*/
	public double getAlphaSide(){
		return alpha_side;
	}

	/**	getAlphaR method
	*	This method returns the current Alpha r opening angle of the Camera instance.
	*	It should be used to give a maximal spanning angle of FOV (e.g. upper right corner)
	*	TODO
	*	@returns alpha_r of the Camera
	*/
	public double getAlphaR(){
		return alpha_r;
	}

	/**	toString method
	*	This method returns a string representation of the Camera object.
	*	@returns string representation of the Camera object
	*/
@Override
	public String toString(){
		String s = "Camera: pos (" + pos[0] + "," + pos[1] + "," + pos[2] + ")";
		s += "\tfocus (" + focusvec[0] + "," + focusvec[1] + "," + focusvec[2] + ")";
		s += "\thead (" + headvec[0] + "," + headvec[1] + "," + headvec[2] + ")";
		return s;
	}
}
