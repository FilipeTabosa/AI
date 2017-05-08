package main;

import java.util.ArrayList;

public class PBest {
	private ArrayList<Double> bestPos;
	private double fitness;
	public PBest() {
		this.bestPos = new ArrayList<Double>();
	}
	public double getFitness() {
		return fitness;
	}
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	public void addPBestPos(double pbest){
		this.bestPos.add(pbest);
	}
	public void setBestPos(ArrayList<Double> pbest){
		for (int i=0; i<pbest.size(); i++){
			this.bestPos.add(i, pbest.get(i));
		}
		
	}
	public double getBestPos(int pos) {
		return bestPos.get(pos);
	}
	public ArrayList<Double> getBestPos(){
		return bestPos;
	}
	public void setBestPos(double pbest, int pos) {
		this.bestPos.set(pos, pbest);
	}
	
}
