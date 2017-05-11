package fss;

import java.util.ArrayList;


public class Fish {
	
	private FishBest fishBest;
	public FishBest getFishBest() {
		return fishBest;
	}

	public void setFishBest(FishBest pbest) {
		this.fishBest = pbest;
	}
	private ArrayList<Double> currentPos;
	private ArrayList<Double> weight;
	public Fish(){
		this.fishBest = new FishBest();
		this.weight = new ArrayList<Double>();
		this.currentPos = new ArrayList<Double>();
	}
	
	public void addCurrentPos(double pbest){
		this.currentPos.add(pbest);
	}
	public void setCurrentPos(ArrayList<Double> pbest){
		for (int i=0; i<pbest.size(); i++){
			this.currentPos.add(i, pbest.get(i));
		}
		
	}
	public double getCurrentPos(int pos) {
		return currentPos.get(pos);
	}
	public ArrayList<Double> getcurrentPos(){
		return currentPos;
	}
	public void setCurrentPos(double pbest, int pos) {
		this.currentPos.set(pos, pbest);
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
		
	
	public String toString(){
		return "X: " + currentPos.toString() + ", Speed: " + weight.toString() +", Pbest: " + fishBest;
		
	}
}
