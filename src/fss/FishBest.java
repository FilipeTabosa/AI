package fss;

import java.util.ArrayList;

public class FishBest {
	private ArrayList<Double> bestPos;
	private double fitness;
	private ArrayList<Double> weight;
	public FishBest() {
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
	
	public void addWeight(double weight){
		this.weight.add(weight);
	}
	public void setWeight(ArrayList<Double> weight){
		for (int i=0; i<weight.size(); i++){
			this.weight.add(i, weight.get(i));
		}
		
	}
	public double getWeight(int pos) {
		return weight.get(pos);
	}
	public ArrayList<Double> getWeight(){
		return weight;
	}
	public void setWeight(double weight, int pos) {
		this.weight.set(pos, weight);
	}
	
}
