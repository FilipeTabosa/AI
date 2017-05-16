package fss;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import functions.Functions;
import properties.ReadProperties;
import pso.MyResult;
import pso.Particle;
/*
 * 
 * Currently not working. Needs to be modified.
 */
public class FSS {
	static ReadProperties properties = new ReadProperties();

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		for (int a=0; a<30; a++){
			System.out.println("Starting simulation number " + a);
			long startTime = System.currentTimeMillis();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			Date date = new Date();

			Particle[] particles = initialize();
			File file = new File("files");
			file = new File(file, "fss");	
			if(!file.isDirectory()){
				file.mkdirs();
			}

			file =  new File(file,properties.getFunctionName());
			if(!file.isDirectory()){
				file.mkdirs();
			}

			String fileName = properties.getMaxSpeed() +  ";" + dateFormat.format(date) +  ".csv";

			File file2 = new File(file,fileName );

			PrintWriter pw = new PrintWriter(file2);
			MyResult getResult;
			FishBest gBest = new FishBest();
			getResult = iteration(properties.getFunctionName(), true, particles, gBest, 0);
			particles = getResult.getParticles();
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
				gBest = new FishBest();
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
	private static MyResult iteration(String functionToUse, boolean firstIteration, Particle[] particles, FishBest gBest, int iterationNumber){
		functionToUse = functionToUse.toLowerCase();
		ArrayList<FishBest> results = new ArrayList<FishBest>(properties.getParticlesNumber());
		for (int i=0; i<particles.length; i++){
			FishBest particleResult = new FishBest();
			switch (functionToUse){
			case "sphere":
				particleResult.setFitness(Functions.sphere(particles[i].getX()));
				particleResult.setBestPos(particles[i].getX());
				results.add(i, particleResult);			
				break;
			case "rastrigin":
				particleResult.setFitness(Functions.rotatedRastrigin(particles[i].getX()));
				particleResult.setBestPos(particles[i].getX());
				results.add(i, particleResult);
				break;

			case "rosenbrock":
				particleResult.setFitness(Functions.rosenbrock(particles[i].getX()));
				particleResult.setBestPos(particles[i].getX());
				results.add(i, particleResult);
				break;
			}
			//particles[i].setPbest(updatePBest(results.get(i), particles[i], firstIteration));
			if(firstIteration && i==0){
			}
		}
		for (int i=0; i<results.size(); i++){
			if (results.get(i).getFitness() < gBest.getFitness()){
				//gBest = updateGBest(results.get(i));
			}
		}

		particles = updateSpeed(gBest,particles, iterationNumber);

		particles = updatePos(particles);
		MyResult result = new MyResult();
		result.setParticles(particles);
		return result;
	}


	private static double movimentoIndividual(double pos, double step){
		double randomNumber = Math.random();
		randomNumber *= Math.floor(Math.random()*2) == 1 ? 1 : -1;
		double newPos = pos + (step * randomNumber );
		return newPos;
	}
	/*
	private static double Alimentacao(double weight, double fitness){

	}*/
	private static double movimentoColetivoInstitivo(ArrayList<Fish> fish, int dimension){
		double mov;
		double firstPart = 0;
		double secondPart = 0;
		for (int i=0; i<fish.size(); i++){

			firstPart = firstPart + (fish.get(i).getFishBest().getFitness() * fish.get(i).getCurrentPos(dimension));
			secondPart = secondPart + fish.get(i).getFishBest().getFitness(); 
		}
		mov = (firstPart / secondPart);
		return mov;
	}
	private static double atualizaPosMovimentoColetivoInstitivo(double pos, double mov){
		pos = pos + mov;
		return pos;
	}
	private static double movimentoColetivoVolitivo(ArrayList<Fish> fish, int dimension){
		double mov;
		double firstPart = 0;
		double secondPart = 0;
		for (int i=0; i<fish.size(); i++){
			firstPart = firstPart + (fish.get(i).getWeight(dimension) * fish.get(i).getCurrentPos(dimension));
			secondPart = secondPart + fish.get(i).getWeight(dimension); 
		}
		mov = (firstPart / secondPart);
		return mov;
	}

	private static double atualizaPosMovimentoColetivoVolitivo(double pos, double mov, double stepVol, boolean gainedWeight){
		double firstPart;
		double secondPart;
		double result;
		firstPart = pos - mov;
		secondPart = distance(pos, mov);
		result = stepVol * (firstPart/secondPart);
		if (gainedWeight){
			result = pos - result;
		} else{
			result = pos + result;
		}
		return result;
	}
	private static double distance(double[] a, double[] b) {
		double diff_square_sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			diff_square_sum += (a[i] - b[i]) * (a[i] - b[i]);
		}
		return Math.sqrt(diff_square_sum);
	}
	private static double distance(double a, double b) {
		double diff_square_sum = 0.0;
		diff_square_sum += (a - b) * (a - b);
		return Math.sqrt(diff_square_sum);
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
	private static Particle[] updateSpeed(FishBest gBest, Particle[] particles, int iteration){
		/*
		 * this initialized value holds no importance. It has this value just because it is necessary to initialize
		 * this variable
		 */
		return particles;
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


