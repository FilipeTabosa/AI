package properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {

	private String TOPOLOGY_NAME = "topology";
	private String FUNCTION_NAME = "function";
	private String MIN_NAME = "min";
	private String MAX_NAME = "max";
	private String PARTICLE_NAME = "particle";
	private String ITERATION_NAME = "iteration";
	private String DIMENSION_NAME = "dimension";
	private String WEIGHT_NAME = "weight";
	private String COGNITIVE_COEFFICIENT_NAME = "c1";
	private String SOCIAL_COEFFICIENT_NAME = "c2";
	private String MAX_SPEED = "max-speed";
	private String DECAY_NAME = "decay";
	private String INIT_WEIGHT = "init-weight";
	private String FINAL_WEIGHT = "final-weight";
	private String CLERC_VALUE = "clerc";
	//public String functionName = "";
	public double weight = 0;
	public boolean clerc = false;
	public boolean decay = false;
	public String topology = "";
	public boolean readProperties = true;
	private String FILE_NAME = "pso.properties";	
	//private String FILE_NAME = "rastrigin.property/pso.properties";	
	//private String FILE_NAME = "rosenbrock.property/pso.properties";	
	//private String FILE_NAME = "sphere.property/pso.properties";	

	private Properties prop = new Properties();
	private InputStream is;

	{


		is = this.getClass().getClassLoader().getResourceAsStream(FILE_NAME);
		if (is != null){
			try {
				this.prop.load(is);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else{
			try {
				throw new FileNotFoundException("file not found");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public double getMin(){
		double min = Double.parseDouble((prop.getProperty(MIN_NAME)));
		return min;

	}
	public int getIteration(){
		int iteration = Integer.parseInt(prop.getProperty(ITERATION_NAME));
		return iteration;
	}
	public double getMax(){
		double max = Double.parseDouble(prop.getProperty(MAX_NAME));
		return max;
	}
	public int getParticlesNumber(){
		int particles = Integer.parseInt(prop.getProperty(PARTICLE_NAME));
		return particles;
	}
	public int getDimension(){
		int dimension = Integer.parseInt(prop.getProperty(DIMENSION_NAME));
		return dimension;
	}
	public double getCognitiveCoefficient(){
		double c1 = Double.parseDouble(prop.getProperty(COGNITIVE_COEFFICIENT_NAME));
		return c1;
	}
	public double getInitWeight(){
		double initWeight = Double.parseDouble(prop.getProperty(INIT_WEIGHT));
		return initWeight;
	}
	public double getFinalWeight(){
		double finalWeight = Double.parseDouble(prop.getProperty(FINAL_WEIGHT));
		return finalWeight;
	}
	public boolean getDecay(){
		if (!readProperties){
			return decay;
		} else{
			boolean decay = Boolean.parseBoolean(prop.getProperty(DECAY_NAME));
			return decay;
		}
	}
	public boolean getClerc(){
		if (!readProperties){
			return clerc;
		} else{
			boolean clerc = Boolean.parseBoolean(prop.getProperty(CLERC_VALUE));
			return clerc;
		}
	}

	public double getMaxSpeed(){
		double speed = Double.parseDouble(prop.getProperty(MAX_SPEED));
		return speed;
	}
	public double getSocialCoefficient(){
		double c2 = Double.parseDouble(prop.getProperty(SOCIAL_COEFFICIENT_NAME));
		return c2;
	}
	public double getWeight(){
		if (!readProperties){	
			return weight;
		}else{
			double weight = Double.parseDouble(prop.getProperty(WEIGHT_NAME));
			return weight;
		}
	}
	public String getFunctionName(){
		return prop.getProperty(FUNCTION_NAME);
	}
	public String getTopology(){
		if (!readProperties){
			return topology;
		}else{
			return prop.getProperty(TOPOLOGY_NAME);
		}
	}
	public void resetVar(){
		topology = "";
		weight = 0;
		clerc = false;
		decay = false;
	}
}



