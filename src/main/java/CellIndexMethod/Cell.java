package CellIndexMethod;


import Models.Particle;

import java.util.ArrayList;
import java.util.List;

public class Cell {

	public List<Particle> particles;

	public Cell() {
		this.particles = new ArrayList<>();
	}

	public void addParticle(Particle particle) {
		this.particles.add(particle);
	}

	public List<Particle> getParticles() {
		return particles;
	}

	public void clear() {
		particles.clear();
	}

	@Override
	public String toString() {
		return "Cell{" +
				"particles=" + particles +
				'}';
	}
}