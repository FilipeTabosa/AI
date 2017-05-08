package main;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import properties.ReadProperties;

public class Pso {
	static ReadProperties properties = new ReadProperties();
	static int focalParticle; 
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		for (int a=0; a<30; a++){
			System.out.println("Starting simulation number " + a);
			long startTime = System.currentTimeMillis();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			Date date = new Date();

			Particle[] particles = initialize();
			File file = new File("files");
			if(properties.getTopology().equals("focal") || properties.getTopology().equals("local")){
				file = new File(file, properties.getTopology());
			} else {
				file = new File(file, "global");	
			}
			if(!file.isDirectory()){
				file.mkdirs();
			}

			file =  new File(file,properties.getFunctionName());
			if(!file.isDirectory()){
				file.mkdirs();
			}

			File fileWeight;
			if (!properties.getClerc()){
				if (properties.getDecay()){
					String weightRange = String.valueOf(properties.getInitWeight()) + "-" + String.valueOf(properties.getFinalWeight());
					fileWeight = new File(file, weightRange);
				} else{
					fileWeight = new File(file, String.valueOf(properties.getWeight())) ;
				}

			} else{
				fileWeight = new File(file, "clerc");
			}
			if(!fileWeight.isDirectory()){
				fileWeight.mkdirs();
			}
			String fileName = properties.getMaxSpeed() +  ";" + dateFormat.format(date) +  ".csv";

			File file2 = new File(fileWeight,fileName );

			PrintWriter pw = new PrintWriter(file2);
			MyResult getResult;
			PBest gBest = new PBest();
			if (properties.getTopology().equals("focal")){
				Random rand = new Random();
				focalParticle = rand.nextInt(properties.getParticlesNumber());
			}
			getResult = iteration(properties.getFunctionName(), true, particles, gBest, 0);
			particles = getResult.getParticles();
			gBest = getResult.getGbest();
			//pw.append("iteration: " + "0");
			//pw.append("\n");
			//pw.append(gBest.getBestPos().toString());
			//pw.append("\n");
			pw.append(String.valueOf(gBest.getFitness()));
			pw.append("\n");
			for (int i=1; i<properties.getIteration(); i++){
				getResult = new MyResult();
				getResult = iteration(properties.getFunctionName(), false, particles, gBest, i);
				particles = getResult.getParticles();
				gBest = new PBest();
				gBest = getResult.getGbest();
				//		pw.append("iteration: " + String.valueOf(i));
				//		pw.append("\n");
				//		pw.append(gBest.getBestPos().toString());
				//		pw.append("\n");
				pw.append(String.valueOf(gBest.getFitness()));
				pw.append("\n");

			}
			pw.append(gBest.getBestPos().toString());
			pw.close();
			long endTime = System.currentTimeMillis();
			System.out.println("simulation completed");
			System.out.println("That took: " + (endTime - startTime) + "ms");
			System.out.println(gBest.getBestPos().toString());

		}
	}
	//	}
	private static double sphere(ArrayList<Double> value){
		double fitness = 0;
		for (int i=0; i<value.size(); i++){
			fitness = fitness + (Math.pow(value.get(i), 2));
		}
		return fitness;
	}
	private static double rosenbrock(ArrayList<Double> value){
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

	public static double rotatedRastrigin(ArrayList<Double> value){
		double fitness = 0;
		for (int i=0; i<value.size(); i++){
			double cosineValue = Math.cos(2*Math.PI*value.get(i));
			double pow = (Math.pow(value.get(i), 2));
			fitness = fitness + (pow - (10*cosineValue) + 10);
		}
		return fitness;
	}
	private static Particle[] updatePos(Particle[] particles){
		for (int i=0; i<properties.getParticlesNumber(); i++){
			for (int j=0; j<properties.getDimension(); j++){
				double newPos = (particles[i].getX(j) + particles[i].getSpeed(j));
				if (newPos>properties.getMax()){
					newPos = properties.getMax();
				} else if(newPos < properties.getMin()){
					newPos = properties.getMin();
				}
				particles[i].setX(newPos, j);
			}
		}
		return particles;
	}
	private static Particle[] updateSpeed(PBest gBest, Particle[] particles, int iteration){
		/*
		 * this initialized value holds no importance. It has this value just because it is necessary to initialize
		 * this variable
		 */
		double weight =10000;
		if (!properties.getClerc()){
			if (properties.getDecay()){
				double decayValue;
				if (iteration > 0){
					decayValue = ((properties.getInitWeight() - properties.getFinalWeight())/properties.getIteration());
					decayValue = decayValue * iteration;
				} else{
					decayValue = 0;
				}
				weight = properties.getInitWeight() - decayValue;
			} else{
				weight = properties.getWeight();
			}
		}
		for (int i=0; i<properties.getParticlesNumber(); i++){
			PBest bestPos = new PBest();
			double clercConstriction = 0.729;
			if (properties.getTopology().equals("local")){

				bestPos = setBestPos(particles[i].getPbest());
				if(i>0){
					if (particles[i-1].getPbest().getFitness() < bestPos.getFitness()){
						bestPos = setBestPos(particles[i-1].getPbest());
					}
				}
				if (i<properties.getDimension()-1){
					if (particles[i+1].getPbest().getFitness() < bestPos.getFitness()){
						bestPos = setBestPos(particles[i+1].getPbest());
					}
				}
			} else if (properties.getTopology().equals("focal")){
				bestPos = particles[focalParticle].getPbest();
			} else{
				bestPos = gBest;
			}

			for (int d=0; d<properties.getDimension(); d++){
				double randNumber = Math.random();
				double randNumber2 = Math.random();

				double firstDifference = (particles[i].getPbest().getBestPos().get(d)) - particles[i].getX(d);
				double secondDifference;
				if (properties.getTopology().equals("focal") && (i==focalParticle)){	
					secondDifference = (gBest.getBestPos().get(d)) - particles[i].getX(d);
				}else{
					secondDifference = (bestPos.getBestPos().get(d)) - particles[i].getX(d);
				}
				double cognitiveImpact = properties.getCognitiveCoefficient()*randNumber;
				double socialImpact = properties.getSocialCoefficient()*randNumber2;
				double speed;
				if(!properties.getClerc()){
					speed = particles[i].getSpeed(d) * weight 
							+((cognitiveImpact)*(firstDifference))
							+((socialImpact)*(secondDifference));
				} else{
					speed = particles[i].getSpeed(d) 
							+((cognitiveImpact)*(firstDifference))
							+((socialImpact)*(secondDifference));
					speed = clercConstriction * speed;
				}
				if (speed > properties.getMaxSpeed()){
					speed = properties.getMaxSpeed();
				} else if(speed < -(properties.getMaxSpeed())){
					speed = -(properties.getMaxSpeed());
				}
				particles[i].setSpeed(speed, d);

			}
		}
		return particles;
	}
	public static PBest setBestPos(PBest pbest){
		PBest best = new PBest();
		best.setFitness(pbest.getFitness());
		best.setBestPos(pbest.getBestPos());
		return best;
	}
	public static double differenceBetweenArrays(ArrayList<Double> firstArray, ArrayList<Double> secondArray){
		double total = 0;
		for (int i=0; i<firstArray.size(); i++){
			double difference = firstArray.get(i) - secondArray.get(i);
			total = total + difference;
		}
		return total;
	}

	private static PBest updatePBest(PBest result, Particle particle, boolean firstIteration){
		if(firstIteration){
			particle.getPbest().setBestPos(result.getBestPos());
			particle.getPbest().setFitness(result.getFitness());
		} else{

			if( result.getFitness() < particle.getPbest().getFitness()){
				particle.getPbest().setBestPos(result.getBestPos());
				particle.getPbest().setFitness(result.getFitness());
			}
		}
		return particle.getPbest();
	}
	private static PBest updateGBest(PBest value){
		PBest gBestUpdated = new PBest();
		gBestUpdated.setFitness(value.getFitness());
		gBestUpdated.setBestPos(value.getBestPos());
		return gBestUpdated;
	}

	private static MyResult iteration(String functionToUse, boolean firstIteration, Particle[] particles, PBest gBest, int iterationNumber){
		functionToUse = functionToUse.toLowerCase();
		ArrayList<PBest> results = new ArrayList<PBest>(properties.getParticlesNumber());
		for (int i=0; i<particles.length; i++){
			PBest particleResult = new PBest();
			switch (functionToUse){
			case "sphere":
				particleResult.setFitness(sphere(particles[i].getX()));
				particleResult.setBestPos(particles[i].getX());
				results.add(i, particleResult);			
				break;
			case "rastrigin":
				particleResult.setFitness(rotatedRastrigin(particles[i].getX()));
				particleResult.setBestPos(particles[i].getX());
				results.add(i, particleResult);
				break;

			case "rosenbrock":
				particleResult.setFitness(rosenbrock(particles[i].getX()));
				particleResult.setBestPos(particles[i].getX());
				results.add(i, particleResult);
				break;
			}
			particles[i].setPbest(updatePBest(results.get(i), particles[i], firstIteration));
			if(firstIteration && i==0){
				gBest = updateGBest(particles[i].getPbest());
			}
		}
		for (int i=0; i<results.size(); i++){
			if (results.get(i).getFitness() < gBest.getFitness()){
				gBest = updateGBest(results.get(i));
			}
		}

		particles = updateSpeed(gBest,particles, iterationNumber);

		particles = updatePos(particles);
		MyResult result = new MyResult();
		result.setParticles(particles);
		result.setGbest(gBest);
		return result;
	}
	private static Particle[] initialize(){
		Particle[] particles = new Particle[properties.getParticlesNumber()];
		Random random = new Random();
		for (int i=0; i<properties.getParticlesNumber(); i++){
			particles[i] = new Particle();

			for (int f=0; f<properties.getDimension(); f++){
				double randomPos = properties.getMin() + (properties.getMax() - properties.getMin()) * random.nextDouble();
				randomPos = randomPos/2;
				particles[i].setX(randomPos);
				double randomSpeed = (-(properties.getMaxSpeed())) + (properties.getMaxSpeed() - (-(properties.getMaxSpeed()))) * random.nextDouble();
				particles[i].setSpeed(randomSpeed);
			}

		}

		return particles;
	}
}

