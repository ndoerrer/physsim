package physsim.global;

import physsim.graphics.Camera;

/**	MyMath class
*	Class containing basic math functions. Mostly handling Vectors.
*/
public class MyMath{
	/**	print method
	*	Prints a vector to stdout.
	*	@param vec: vector to print.
	*/
	public static void print(double [] vec){
		for (int i=0; i<vec.length; i++)
			System.out.print(vec[i]+"\t");
		System.out.println();
	}

	/**	scalarProd method
	*	Computes the product of vector and scalar.
	*	@param vec: vector to multiply
	*	@param scalar: scalar to mulitply with
	*	@returns vector being the product of vector and scalar
	*/
	public static double[] scalarProd(double [] vec, double scalar){
		return scalarProd(scalar, vec);
	}

	/**	scalarProd method
	*	Computes the product of vector and scalar.
	*	@param vec: vector to multiply
	*	@param scalar: scalar to mulitply with
	*	@returns vector being the product of vector and scalar
	*/
	public static double[] scalarProd(double scalar, double [] vec){
		double[] res = new double[vec.length];
		for (int i=0; i<vec.length; i++)
			res[i] = vec[i]*scalar;
		return res;
	}

	/**	dot method
	*	Computes the product of two vectors.
	*	@param vec1: vector to multiply
	*	@param vec2: vector to multiply
	*	@returns scalar being the product of the two vectors
	*/
	public static double dot(double[] vec1, double[] vec2){
		double res = 0;
		for (int i=0; i<vec1.length; i++)
			res += vec1[i]*vec2[i];
		return res;
	}

	/**	norm method
	*	Normalizes a vector.
	*	@param vec: vector to normalize
	*	@returns scalar being the product of the two vectors
	*/
	public static double[] norm(double[] vec){
		double [] res = new double[vec.length];
		double sum = 0;
		for (int i=0; i<vec.length; i++){
			res[i] = vec[i];
			sum += vec[i]*vec[i];
		}
		sum = Math.sqrt(sum);
		if (sum == 0)
			return res;
		for (int i=0; i<vec.length; i++)
			res[i] = vec[i] / sum;
		return res;
	}

	/**	sub method
	*	Subtract a vector from another.
	*	@param vec1: vector to subtract from
	*	@param vec2: vector to subtract
	*	@returns vector being the difference of the two vectors
	*/
	public static double[] sub(double[] vec1, double[] vec2){
		double [] res = new double [vec1.length];
		for (int i=0; i<vec1.length; i++)
			res[i] = vec1[i] - vec2[i];
		return res;
	}

	/**	add method
	*	Add a vector to another.
	*	@param vec1: vector to add
	*	@param vec2: vector to add
	*	@returns vector being the sum of the two vectors
	*/
	public static double[] add(double[] vec1, double[] vec2){
		double [] res = new double [vec1.length];
		for (int i=0; i<vec1.length; i++)
			res[i] = vec1[i] + vec2[i];
		return res;
	}

	/**	cross method
	*	Compute cross product of two vectors.
	*	@param vec1: first vector in cross product
	*	@param vec2: second vector in cross product
	*	@returns vector being the cross product of the two vectors
	*/
	public static double[] cross(double[] vec1, double[] vec2){
		double [] res = new double [vec1.length];
		int n = vec1.length;
		for (int i=0; i<n; i++)
			res[i] = vec1[(i+1)%n]*vec2[(i+2)%n] - vec1[(i+2)%n]*vec2[(i+1)%n];
		return res;
	}

	/**	abs method
	*	Compute the absolute of a vector.
	*	@param vec: vector to compute absolute from
	*	@returns scalar being the absolute value of the vector
	*/
	public static double abs(double[] vec){
		double res = 0;
		for (int i=0; i<vec.length; i++)
			res += vec[i]*vec[i];
		res = Math.sqrt(res);
		return res;
	}

	/**	angle method
	*	Compute the angle between two vectors.
	*	@param vec1: first vector
	*	@param vec2: second vector
	*	@returns scalar being the angle between the two vectors
	*/
	public static double angle(double[] vec1, double[] vec2){
		double res = Math.acos(dot(vec1, vec2) / ( abs(vec1) * abs(vec2) ) );
		return res;
	}

	/**	transform method
	*	Transforms a point to coordinates in a camera view.
	*	@param target: point to transform
	*	@param camera: Camera to transform the point to
	*	@returns vector consisting of height and width in the camera view and the distance
	*/
	public static double[] transform(double[] target, Camera camera){
		double tmp, dist, pointh, pointw;
		double[] vec;
		double[] right = MyMath.norm(MyMath.cross(camera.getHead(), camera.getFocus()));
		vec = MyMath.sub(target, camera.getPos());
		dist = MyMath.abs(vec);
		tmp = MyMath.dot(MyMath.norm(vec), MyMath.norm(camera.getHead()));
		pointh = Math.acos(tmp) - Math.PI/2;
		tmp = MyMath.dot(MyMath.norm(vec), right);
		pointw = Math.acos(tmp) - Math.PI/2;
		return new double[] {pointh, pointw, dist};
	}
}
