package functions;

import java.util.ArrayList;

public class Functions {

	public static double rotatedRastrigin(ArrayList<Double> value){
		double fitness = 0;
		for (int i=0; i<value.size(); i++){
			double cosineValue = Math.cos(2*Math.PI*value.get(i));
			double pow = (Math.pow(value.get(i), 2));
			fitness = fitness + (pow - (10*cosineValue) + 10);
		}
		return fitness;
	}
	public static double sphere(ArrayList<Double> value){
		double fitness = 0;
		for (int i=0; i<value.size(); i++){
			fitness = fitness + (Math.pow(value.get(i), 2));
		}
		return fitness;
	}
	public static double rosenbrock(ArrayList<Double> value){
		double fitness = 0;
		for (int i=0; i<value.size()-1; i++){
			double firstNumberPow = Math.pow(value.get(i), 2);
			double numberToPow = firstNumberPow - value.get(i+1);
			numberToPow = (Math.pow(numberToPow, 2));
			double secondNumberToPow = value.get(i) - 1;
			secondNumberToPow = (Math.pow(secondNumberToPow, 2));
			double function = (100*numberToPow) + secondNumberToPow;
			fitness = fitness + function;
		}
		return fitness;
	}

}
