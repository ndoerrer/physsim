package physsim.global;

import physsim.graphics.Camera;
import java.awt.Color;

/**	Ball class
*	Extends MobileEntity. This implements a mobile sphere.
*/
public class Ball extends MobileEntity{
	private final boolean DEBUG = true;
	private double pos[];
	private double radius;

	/** Ball parameter constructor
	*	This constructor sets position and radius according to parameters given.
	*	@param posIn: Position to set the Ball instance to
	*	@param radiusIn: Radius of the Ball
	*/
	public Ball(double[] posIn, double radiusIn){
		pos = new double[] {posIn[0], posIn[1], posIn[2]};
		vel = new double[] {0,0,0};
		radius = radiusIn;
		mass = 1.0;
		//myId = id++;		//already handled in Entity Constructor
	}

	/** Ball extended parameter constructor
	*	This constructor sets position and radius according to parameters given.
	*	Additionally the velocity is set.
	*	@param posIn: Position to set the Ball instance to
	*	@param radiusIn: Radius of the Ball
	*	@param velIn: Velocity of the Ball
	*/
	public Ball(double[] posIn, double[] velIn, double radiusIn){
		this(posIn, radiusIn);
		vel = new double[] {velIn[0], velIn[1], velIn[2]};
	}

	/** Ball parameter constructor with color
	*	This constructor sets position and radius according to parameters given.
	*	Additionally the color is set.
	*	@param posIn: Position to set the Ball instance to
	*	@param radiusIn: Radius of the Ball
	*	@param colorIn: Color to set the Ball instance to
	*/
	public Ball(double[] posIn, double radiusIn, Color colorIn){
		this(posIn, radiusIn);
		setColor(colorIn);
	}

	/** Ball extended parameter constructor with color
	*	This constructor sets position and radius according to parameters given.
	*	Additionally the color and velocity are set.
	*	@param posIn: Position to set the Ball instance to
	*	@param radiusIn: Radius of the Ball
	*	@param velIn: Velocity to set for the Ball
	*	@param colorIn: Color to set the Ball instance to
	*/
	public Ball(double[] posIn, double[] velIn, double radiusIn, Color colorIn){
		this(posIn, radiusIn, colorIn);
		vel = new double[] {velIn[0], velIn[1], velIn[2]};
	}

	/**	getPos method
	*	Getter method for the position of the Ball.
	*	@returns x, y and z of the Ball instance.
	*/
	public double[] getPos(){
		return new double[] {pos[0], pos[1], pos[2]};
	}

	/**	getPosX method
	*	Getter method for the x-position of the Ball.
	*	@returns x of the Ball instance.
	*/
	public double getPosX(){
		return pos[0];
	}

	/**	getPosY method
	*	Getter method for the y-position of the Ball.
	*	@returns y of the Ball instance.
	*/
	public double getPosY(){
		return pos[1];
	}

	/**	getPosZ method
	*	Getter method for the z-position of the Ball.
	*	@returns z of the Ball instance.
	*/
	public double getPosZ(){
		return pos[2];
	}

	/**	getRadius method
	*	Getter method for the radius of the Ball.
	*	@returns radius of the Ball instance.
	*/
	public double getRadius(){
		return radius;
	}

	/**	setPos method
	*	Setter method for the position of the Ball.
	*	@param posIn: x, y and z to set the Ball position to.
	*/
	public void setPos(double[] posIn){
		pos = posIn;	//check if 3 components?
	}

	/**	move method
	*	This method performs a movement for the Ball. According to the velocity
	*	and the time interval provided, the translation is computed.
	*	@param dt: time interval for the movement.
	*/
@Override
	public void move(long dt){
		pos = MyMath.add(pos, MyMath.scalarProd(((double)dt)/1000, vel));
		return;
	}

	/**	checkCollision method
	*	This method performs a collision check and handles it.
	*	@param ent: entity to check collision against.
	*	@param t: time for the movement (XXX not used right now).
	*/
@Override
	public void checkCollision(Entity ent, long t){
		if (ent.isMobile()){
			if (((MobileEntity)ent).getId() >= getId())	//prevents double collision handling
				return;
		}
		double [] normal, normal1, normal2, dirp;
		double normangle1, normangle2, vn, bvn, vp, bvp, new_vn, new_bvn, tmp;
		double normangle, distance, impulse, a;

/*			case "Ball":
				normangle1=0; normangle2=0;
				Ball ball = (Ball) ent;
				if (MyMath.abs(MyMath.sub(pos, ball.getPos())) >= ball.getRadius() + radius)
					return;

				if (DEBUG){
					System.out.println("********** Collision of "+myId+" and "+ball.getId() +
													" at time "+t);
					System.out.print("== initial velocity1\t");
					MyMath.print(vel);
					System.out.print("== initial velocity2\t");
					MyMath.print(ball.getVelocity());
					System.out.print("== position of 1\t");
					MyMath.print(pos);
					System.out.print("== position of 2\t");
					MyMath.print(ball.getPos());
				}
				normal1 = MyMath.norm(MyMath.sub(ball.getPos(), pos));
				normal2 = MyMath.scalarProd(normal1,-1.0);
				distance = MyMath.abs(MyMath.sub(pos, ball.getPos()));

				if (DEBUG){
					System.out.print("== normal1\t");
					MyMath.print(normal1);
					System.out.print("== normal2\t");
					MyMath.print(normal2);
					System.out.println("== distance\t"+distance);
				}

				if (!(vel[0] == 0 && vel[1] == 0 && vel[2] == 0))
					normangle1 = Math.acos(MyMath.dot(MyMath.norm(vel), normal1));
				if (!(ball.getVelocity()[0] == 0 && ball.getVelocity()[1] == 0
												&& ball.getVelocity()[2] == 0))
					normangle2 = Math.acos(MyMath.dot(MyMath.norm(ball.getVelocity()),
															 normal2));
				if (Double.isNaN(normangle1))	//occurs at acos(1+epsilon) at vel_i = 1/3
					normangle1 = 0;				//and norm_i = 1/sqrt(3)
				if (Double.isNaN(normangle2))
					normangle2 = 0;
				System.out.println("DEBUG: normangle1:"+normangle1+"\tnormangle2:"+normangle2);
				vn = Math.cos(normangle1) * MyMath.abs(vel);
				bvn = Math.cos(normangle2) * MyMath.abs(ball.getVelocity());
				vp = Math.sin(normangle1) * MyMath.abs(vel);
				bvp = Math.sin(normangle2) * MyMath.abs(ball.getVelocity());
				//dirp = MyMath.norm(MyMath.scalarProd(-1, MyMath.cross(MyMath.cross(vel,
				//										normal1), normal1)));
				dirp = MyMath.norm(MyMath.cross(MyMath.cross(vel, normal1), normal2));
				if (DEBUG)
					System.out.println("vn="+vn+"\tvp="+vp+"\tbvn="+bvn+"\tbvp="+bvp);
				tmp = 2.0/(mass+ball.getMass()) * (mass * vn + ball.getMass() * bvn);
				setVelocity(MyMath.add(MyMath.scalarProd((tmp - vn), normal2), MyMath.scalarProd(vp, dirp)));
				ball.setVelocity(MyMath.add(MyMath.scalarProd((tmp - bvn), normal1), MyMath.scalarProd(bvp, dirp)));
				if (DEBUG){
					System.out.print("== final velocity1\t");
					MyMath.print(vel);
					System.out.print("== final velocity2\t");
					MyMath.print(ball.getVelocity());
				}
				// to move the balls away from each other.
				pos = MyMath.add(pos, MyMath.scalarProd((radius+
								ball.getRadius()-distance)/2.0, normal2));
				ball.setPos(MyMath.add(ball.getPos(), MyMath.scalarProd((radius+
								ball.getRadius()-distance)/2.0, normal1)));
				if (DEBUG){
					System.out.print("Final Position1:\t");
					MyMath.print(pos);
					System.out.print("Final Position2:\t");
					MyMath.print(ball.getPos());
					System.out.print("Final Distance:\t");
					System.out.println(MyMath.abs(MyMath.sub(pos, ball.getPos())));
				}
				//System.exit(0);	//debug shutdown
				break;*/

/*
			case "Ball":
				normangle1=0; normangle2=0;
				Ball ball = (Ball) ent;
				if (MyMath.abs(MyMath.sub(pos, ball.getPos())) >= ball.getRadius() + radius)
					return;

				if (DEBUG){
					System.out.println("********** Collision of "+myId+" and "+ball.getId() +
													" at time "+t);
					System.out.print("== initial velocity1\t");
					MyMath.print(vel);
					System.out.print("== initial velocity2\t");
					MyMath.print(ball.getVelocity());
					System.out.print("== position of 1\t");
					MyMath.print(pos);
					System.out.print("== position of 2\t");
					MyMath.print(ball.getPos());
				}
				normal = MyMath.norm(MyMath.sub(ball.getPos(), getPos()));
				distance = MyMath.abs(MyMath.sub(ball.getPos(), getPos()));

				if (DEBUG){
					System.out.print("== normal\t");
					MyMath.print(normal);
					System.out.println("== distance\t"+distance);
				}
				a = MyMath.dot(MyMath.scalarProd(2, normal), MyMath.sub(vel, ball.getVelocity()));
				a = a*(1.0/(1.0/getMass() + 1.0/ball.getMass()));
				System.out.println(a);
				setVelocity(MyMath.sub(vel, MyMath.scalarProd(a/mass, normal)));
				ball.setVelocity(MyMath.add(ball.getVelocity(), MyMath.scalarProd(a/ball.getMass(), normal)));
				if (DEBUG){
					System.out.print("== final velocity1\t");
					MyMath.print(vel);
					System.out.print("== final velocity2\t");
					MyMath.print(ball.getVelocity());
				}
				// to move the balls away from each other.
				setPos(MyMath.add(pos, MyMath.scalarProd(-(getRadius()+
								ball.getRadius()-distance)/2.0, normal)));
				ball.setPos(MyMath.add(ball.getPos(), MyMath.scalarProd((radius+
								ball.getRadius()-distance)/2.0, normal)));
				if (DEBUG){
					System.out.print("Final Position1:\t");
					MyMath.print(pos);
					System.out.print("Final Position2:\t");
					MyMath.print(ball.getPos());
					System.out.print("Final Distance:\t");
					System.out.println(MyMath.abs(MyMath.sub(pos, ball.getPos())));
				}
				//System.exit(0);	//debug shutdown
				break;*/

		switch(ent.getType()){				// somehow still slightly changes impulse
			case "Ball":
				normangle1=0; normangle2=0;
				Ball ball = (Ball) ent;
				if (MyMath.abs(MyMath.sub(pos, ball.getPos())) >= ball.getRadius() + radius)
					return;

				if (DEBUG){
					System.out.println("********** Collision of "+myId+" and "+ball.getId() +
													" at time "+t);
					System.out.print("== initial velocity1\t");
					MyMath.print(vel);
					System.out.print("== initial velocity2\t");
					MyMath.print(ball.getVelocity());
					System.out.print("== position of 1\t");
					MyMath.print(pos);
					System.out.print("== position of 2\t");
					MyMath.print(ball.getPos());
				}
				normal = MyMath.norm(MyMath.sub(ball.getPos(), getPos()));
				distance = MyMath.abs(MyMath.sub(ball.getPos(), getPos()));

				if (DEBUG){
					System.out.print("== normal\t");
					MyMath.print(normal);
					System.out.println("== distance\t"+distance);
				}
				
				vn = MyMath.dot(MyMath.sub(getVelocity(), ball.getVelocity()), normal);
				setVelocity(MyMath.sub(getVelocity(), MyMath.scalarProd(vn, normal)));
				ball.setVelocity(MyMath.add(ball.getVelocity(), MyMath.scalarProd(vn, normal)));
				if (DEBUG){
					System.out.print("== final velocity1\t");
					MyMath.print(vel);
					System.out.print("== final velocity2\t");
					MyMath.print(ball.getVelocity());
				}
				// to move the balls away from each other.
				setPos(MyMath.add(pos, MyMath.scalarProd(-(getRadius()+
								ball.getRadius()-distance)/2.0, normal)));
				ball.setPos(MyMath.add(ball.getPos(), MyMath.scalarProd((radius+
								ball.getRadius()-distance)/2.0, normal)));
				if (DEBUG){
					System.out.print("Final Position1:\t");
					MyMath.print(pos);
					System.out.print("Final Position2:\t");
					MyMath.print(ball.getPos());
					System.out.print("Final Distance:\t");
					System.out.println(MyMath.abs(MyMath.sub(pos, ball.getPos())));
				}
				//System.exit(0);	//debug shutdown
				break;

			case "Rectangle":
				Rectangle rect = (Rectangle) ent;
				if (rect.closestDistance(pos) >= radius)
					return;
				if (DEBUG){
					System.out.println("########## Collision of "+myId+" and "+rect.getId());
					System.out.print("== position of ball:\t\t");
					MyMath.print(pos);
					System.out.print("== initial velocity of ball:\t");
					MyMath.print(vel);
					System.out.print("== normal of rectangle:\t\t");
					MyMath.print(rect.getNormal());
				}
				impulse = MyMath.abs(MyMath.scalarProd(vel,mass));
				normal = rect.getNormal(pos);
				distance = radius-rect.closestDistance(pos);
				normangle = Math.acos(MyMath.dot(MyMath.norm(vel), normal));

				if (DEBUG)
					System.out.println("== angle of collision\t"+normangle);
				vn = Math.cos(normangle)* MyMath.abs(vel);
				dirp = MyMath.norm(MyMath.cross(MyMath.cross(vel, normal), normal));
				vp = Math.sin(normangle)* MyMath.abs(vel);
				setVelocity(MyMath.add(MyMath.scalarProd(-vp,dirp),
												MyMath.scalarProd(-vn,normal)));
				if (DEBUG){
					System.out.println("== vn of ball: "+vn);
					System.out.println("== vp of ball: "+vp);
					System.out.print("== final velocity of ball:\t");
					MyMath.print(vel);
				}
				//moving to valid state
				pos = MyMath.add(pos, MyMath.scalarProd(-distance, normal));
				if (DEBUG){
					System.out.print("== normal vector:\t\t");
					MyMath.print(normal);
					System.out.print("== final position of ball:\t");
					MyMath.print(pos);
				}
				break;

				case "Polygon":
				Polygon poly = (Polygon) ent;
				if (poly.closestDistance(pos) >= radius)
					return;
				if (DEBUG){
					System.out.println("########## Collision of "+myId+" and "+poly.getId());
					System.out.print("== position of ball:\t\t");
					MyMath.print(pos);
					System.out.print("== initial velocity of ball:\t");
					MyMath.print(vel);
					System.out.print("== normal of polygon:\t\t");
					MyMath.print(poly.getNormal());
				}
				impulse = MyMath.abs(MyMath.scalarProd(vel,mass));
				normal = poly.getNormal(pos);
				distance = radius-poly.closestDistance(pos);
				normangle = Math.acos(MyMath.dot(MyMath.norm(vel), normal));

				if (DEBUG)
					System.out.println("== angle of collision\t"+normangle);
				vn = Math.cos(normangle)* MyMath.abs(vel);
				dirp = MyMath.norm(MyMath.cross(MyMath.cross(vel, normal), normal));
				vp = Math.sin(normangle)* MyMath.abs(vel);
				setVelocity(MyMath.add(MyMath.scalarProd(-vp,dirp),
												MyMath.scalarProd(-vn,normal)));
				if (DEBUG){
					System.out.println("== vn of ball: "+vn);
					System.out.println("== vp of ball: "+vp);
					System.out.print("== final velocity of ball:\t");
					MyMath.print(vel);
				}
				//moving to valid state TODO: proper way: moving back to theoretical hitpoint
				pos = MyMath.add(pos, MyMath.scalarProd(-distance, normal));
				if (DEBUG){
					System.out.print("== normal vector:\t\t");
					MyMath.print(normal);
					System.out.print("== final position of ball:\t");
					MyMath.print(pos);
				}
				break;

			default:
				break;
		}
	}

	/**	isInFOV method
	*	This method checks whether the Ball is in field of view of given camera.
	*	@param camera: camera to check fov of.
	*	@returns: true, if Ball is in FoV of the camera.
	*/
@Override
	public boolean isInFOV(Camera camera){
		if(MyMath.dot(MyMath.norm(MyMath.sub(pos, camera.getPos())), camera.getFocus()) <= 0)
			return false;
		return true;
	}

	/**	closestDistance method
	*	This method computes the closest distance of any given Point to the Ball instance.
	*	If the point lies inside the Ball instance, a negative value is returned.
	*	@param point: coordinates of the point to check
	*	@returns euclidian distance of the point to the Ball instance.
	*/
@Override
	public double closestDistance(double[] point){
		return Math.sqrt(MyMath.dot(MyMath.sub(point, pos), MyMath.sub(point, pos)))-radius;
	}

	/**	farthestDistance method
	*	This method computes the farthest distance of any given Point to the Balls center.
	*	TODO: better name?
	*	@param point: coordinates of the point to check
	*	@returns euclidian distance of the point to the Ball instance center.
	*/
@Override
	public double farthestDistance(double[] point){
		return Math.sqrt(MyMath.dot(MyMath.sub(point, pos), MyMath.sub(point, pos)));
	}

	/**	getType method
	*	This method returns "Ball" as type string.
	*	@returns "Ball" as type string (overrides from MobileEntity).
	*/
@Override
	public String getType(){
		return "Ball";
	}
}
