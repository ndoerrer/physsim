package physsim.graphics;

//TODO render distance
public class Camera{
	private double[] pos;
	private double[] focusvec;
	private double[] headvec;
	private double alpha_up;
	private double alpha_side;
	private double alpha_r;

	public Camera(){
		pos = new double[] {0,0,0};
	}

	public Camera(double[] posIn, double[] focusvecIn, double[] headvecIn){
		pos = new double [] {posIn[0], posIn[1], posIn[2]};
		focusvec = new double [] {focusvecIn[0], focusvecIn[1], focusvecIn[2]};
		headvec = new double [] {headvecIn[0], headvecIn[1], headvecIn[2]};
		alpha_up = Math.PI*3/16;
		alpha_side = Math.PI/4;
		alpha_r = alpha_side*Math.cos(Math.atan(alpha_up/alpha_side));
	}

	public Camera(double[] posIn, double[] focusvecIn, double[] headvecIn,
									double alpha_upIn, double alpha_sideIn){
		this(posIn, focusvecIn, headvecIn);
		alpha_up = alpha_upIn;
		alpha_side = alpha_sideIn;
		alpha_r = alpha_side*Math.cos(Math.atan(alpha_up/alpha_side));
	}

	public double getPosX(){
		return pos[0];
	}

	public double getPosY(){
		return pos[1];
	}

	public double getPosZ(){
		return pos[2];
	}

	public double[] getPos(){
		return pos.clone();
	}

	public double[] getFocus(){
		return focusvec.clone();
	}

	public double[] getHead(){
		return headvec.clone();
	}

	public double getAlphaUp(){
		return alpha_up;
	}

	public double getAlphaSide(){
		return alpha_side;
	}

	public double getAlphaR(){
		return alpha_r;
	}
}
