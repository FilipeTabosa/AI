package pso;


public class MyResult {
	private PBest gbest;
	private Particle[] particles;
	
	public Particle[] getParticles() {
		return particles;
	}
	public void setParticles(Particle[] particles) {
		this.particles = particles;
	}
	public PBest getGbest() {
		return gbest;
	}
	public void setGbest(PBest gbest) {
		this.gbest = gbest;
	}

}
