package Models;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class StaticParticlesManager {

	private Set<Particle> staticParticles;

	public StaticParticlesManager() {
		this.staticParticles = new HashSet<>();
	}

	public void addStaticParticle(Particle particle) {
		staticParticles.add(particle);
	}

	public void addStaticParticles(Collection<Particle> particles) {
		staticParticles.addAll(particles);
	}

	public boolean isStatic(Particle particle) {
		return staticParticles.contains(particle);
	}
}
