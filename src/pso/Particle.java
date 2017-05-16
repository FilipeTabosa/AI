package pso;

import java.util.ArrayList;

public class Particle {
	
	private PBest pbest;
	public PBest getPbest() {
		return pbest;
	}

	public void setPbest(PBest pbest) {
		this.pbest = pbest;
	}


	private ArrayList<Double> x;
	private ArrayList<Double> speed;
	public Particle(){
		this.pbest = new PBest();
		this.speed = new ArrayList<Double>();
		this.x = new ArrayList<Double>();
	}
	
	public double getX(int pos) {
		return x.get(pos);
	}
	public ArrayList<Double> getX(){
		return x;
	}
	public void setX(double x, int pos) {
		this.x.set(pos, x);
	}
	public void setX(double x){
		this.x.add(x);
	}
	public void setSpeed(double speed){
		this.speed.add(speed);
	}
	
	public double getSpeed(int pos) {
		return speed.get(pos);
	}
	public ArrayList<Double> getSpeed(){
		return speed;
	}
	public void setSpeed(double speed, int pos) {
		this.speed.set(pos, speed);
	}
	
	
	public String toString(){
		return "X: " + x.toString() + ", Speed: " + speed.toString() +", Pbest: " + pbest;
		
	}
}
