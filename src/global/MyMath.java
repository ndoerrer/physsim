package physsim.global;

import physsim.graphics.Camera;

public class MyMath{
	public static void print(double [] vec){
		for (int i=0; i<vec.length; i++)
			System.out.print(vec[i]+"\t");
		System.out.println();
	}

	public static double[] scalarProd(double [] vec, double scalar){
		return scalarProd(scalar, vec);
	}

	public static double[] scalarProd(double scalar, double [] vec){
		double[] res = new double[vec.length];
		for (int i=0; i<vec.length; i++)
			res[i] = vec[i]*scalar;
		return res;
	}

	public static double dot(double[] vec1, double[] vec2){
		double res = 0;
		for (int i=0; i<vec1.length; i++)
			res += vec1[i]*vec2[i];
		return res;
	}

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

	public static double[] sub(double[] vec1, double[] vec2){
		double [] res = new double [vec1.length];
		for (int i=0; i<vec1.length; i++)
			res[i] = vec1[i] - vec2[i];
		return res;
	}

	public static double[] add(double[] vec1, double[] vec2){
		double [] res = new double [vec1.length];
		for (int i=0; i<vec1.length; i++)
			res[i] = vec1[i] + vec2[i];
		return res;
	}

	public static double[] cross(double[] vec1, double[] vec2){
		double [] res = new double [vec1.length];
		int n = vec1.length;
		for (int i=0; i<n; i++)
			res[i] = vec1[(i+1)%n]*vec2[(i+2)%n] - vec1[(i+2)%n]*vec2[(i+1)%n];
		return res;
	}

	public static double abs(double[] vec){
		double res = 0;
		for (int i=0; i<vec.length; i++)
			res += vec[i]*vec[i];
		res = Math.sqrt(res);
		return res;
	}

	public static double angle(double[] vec1, double[] vec2){
		double res = Math.acos(dot(vec1, vec2) / ( abs(vec1) * abs(vec2) ) );
		return res;
	}

	/**
	returns width, height and distance
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
