package physsim.global;

import physsim.graphics.Camera;
import java.awt.Color;

public class Ball extends MobileEntity{
	private final boolean DEBUG = false;
	private double pos[];
	private double radius;

	public Ball(double[] posIn, double radiusIn){
		pos = new double[] {posIn[0], posIn[1], posIn[2]};
		vel = new double[] {0,0,0};
		radius = radiusIn;
		mass = 1.0;
		//myId = id++;		//already handled in Entity Constructor
	}

	public Ball(double[] posIn, double[] velIn, double radiusIn){
		this(posIn, radiusIn);
		vel = new double[] {velIn[0], velIn[1], velIn[2]};
	}

	public Ball(double[] posIn, double radiusIn, Color colorIn){
		this(posIn, radiusIn);
		setColor(colorIn);
	}

	public Ball(double[] posIn, double[] velIn, double radiusIn, Color colorIn){
		this(posIn, radiusIn, colorIn);
		vel = new double[] {velIn[0], velIn[1], velIn[2]};
	}

	public Ball(double[] posIn, double[] velIn, double radiusIn, boolean visibleIn,
											boolean activeIn, Color colorIn){
		this(posIn, velIn, radiusIn, colorIn);
		visible = visibleIn;
		active = activeIn;
	}

	public double[] getPos(){
		return new double[] {pos[0], pos[1], pos[2]};
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

	public double getRadius(){
		return radius;
	}

	public void setPos(double[] posIn){
		pos = posIn;	//check if 3 components?
	}

@Override
	public void move(long dt){
		pos = MyMath.add(pos, MyMath.scalarProd(((double)dt)/1000, vel));
		return;
	}

@Override
	public void checkCollision(Entity ent, long t){
		if (ent.isMobile()){
			if (((MobileEntity)ent).getId() >= getId())	//prevents double collision handling
				return;
			if (ent.getType() == "Ball"){				// somehow still slightly changes impulse
				Ball ball = (Ball) ent;
				if (MyMath.abs(MyMath.sub(pos, ball.getPos())) < ball.getRadius() + radius){
					double [] normal1, normal2, dirp;
					double normangle1=0, normangle2=0, vn, bvn, vp, bvp, new_vn, new_bvn, tmp;
					double distance;

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
				}
			}
		}
		else{		//IDEA: move to Rectangle etc...
			if (ent.getType() == "Rectangle"){
				Rectangle rect = (Rectangle) ent;
				if (rect.closestDistance(pos) < radius){
					if (DEBUG){
						System.out.println("########## Collision of "+myId+" and "+rect.getId());
						System.out.print("== position of ball:\t\t");
						MyMath.print(pos);
						System.out.print("== initial velocity of ball:\t");
						MyMath.print(vel);
					}
					double [] normal, dirp;
					double normangle, vn, vp, distance;
					double impulse = MyMath.abs(MyMath.scalarProd(vel,mass));
					normal = rect.getNormal(pos);
					distance = radius-rect.closestDistance(pos);
					normangle = Math.acos(MyMath.dot(MyMath.norm(vel), normal));

					if (DEBUG)
						System.out.println("== angle of collision\n"+normangle);
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
				}
			}
			if (ent.getType() == "Polygon"){
				Polygon poly = (Polygon) ent;
				if (poly.closestDistance(pos) < radius){
					if (DEBUG){
						System.out.println("########## Collision of "+myId+" and "+poly.getId());
						System.out.print("== position of ball:\t\t");
						MyMath.print(pos);
						System.out.print("== initial velocity of ball:\t");
						MyMath.print(vel);
					}
					double [] normal, dirp;
					double normangle, vn, vp, distance;
					double impulse = MyMath.abs(MyMath.scalarProd(vel,mass));
					normal = poly.getNormal(pos);
					distance = radius-poly.closestDistance(pos);
					normangle = Math.acos(MyMath.dot(MyMath.norm(vel), normal));

					if (DEBUG)
						System.out.println("== angle of collision\n"+normangle);
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
				}
			}
		}
	}

@Override
	public boolean isInFOV(Camera camera){
		if(MyMath.dot(MyMath.norm(MyMath.sub(pos, camera.getPos())), camera.getFocus()) <= 0)
			return false;
		return true;
	}

//if negative object is inside ball
@Override
	public double closestDistance(double[] point){
		return Math.sqrt(MyMath.dot(MyMath.sub(point, pos), MyMath.sub(point, pos)))-radius;
	}

@Override
	public double farthestDistance(double[] point){
		return Math.sqrt(MyMath.dot(MyMath.sub(point, pos), MyMath.sub(point, pos)));
	}


@Override
	public String getType(){
		return "Ball";
	}
}
