package abc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import functions.Functions;
import main.MyResult;
import main.PBest;
import main.Particle;
import properties.ReadProperties;

public class ABC {
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
				file = new File(file, "abc");	
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
			PBest gBest = new PBest();
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
	private static MyResult iteration(String functionToUse, boolean firstIteration, Particle[] particles, PBest gBest, int iterationNumber){
		functionToUse = functionToUse.toLowerCase();
		ArrayList<PBest> results = new ArrayList<PBest>(properties.getParticlesNumber());
		for (int i=0; i<particles.length; i++){
			PBest particleResult = new PBest();
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
			particles[i].setPbest(updatePBest(results.get(i), particles[i], firstIteration));
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
		result.setGbest(gBest);
		return result;
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


