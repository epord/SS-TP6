package Models;

import java.util.Objects;

public class Particle {
    private String id;
    private Vector position;
    private Vector velocity;
    private Vector acceleration;
    private Double mass;
    private Double radius;
    private Double pressure = 0.0;

    public Particle(Integer id, Vector position, Vector velocity, Vector acceleration, Double mass, Double radius) {
        this(id.toString(),position,velocity,acceleration,mass, radius);
    }

    public Particle(String id, Vector position, Vector velocity, Vector acceleration, Double mass, Double radius) {
        this.id =id;
        this.velocity = velocity;
        this.position = position;
        this.acceleration = acceleration;
        this.mass = mass;
        this.radius = radius;
    }

    public Particle(String id, Vector position, Double mass, Double radius) {
        this(id,position,new Vector(0.0,0.0),new Vector(0.0,0.0),mass,radius);
    }


    public Particle getCopyWithPosition(Vector newPosition) {
        return new Particle(id, newPosition, velocity, acceleration, mass, radius);
    }

    public Particle getCopyWithPressure(Double pressure) {
        Particle newPart =  new Particle(id, position, velocity, acceleration, mass, radius);
        newPart.pressure = pressure;
        return newPart;
    }

    public Double getPressure() {
        return pressure;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public Particle getCopyWithVelocity(Vector newVelocity) {
        return new Particle(id, position, newVelocity, acceleration, mass, radius);
    }

    public Vector getAcceleration() {
        return acceleration;
    }

    public Particle getCopyWithAcceleration(Vector newAcceleration) {
        return new Particle(id, position, velocity, newAcceleration, mass, radius);
    }

    public Double getMass() {
        return mass;
    }

    public Double getRadius() {
    	return this.radius;
	}

    public String getID() {
        return id;
    }

    public Boolean isCollisioningWith(Particle particle) {
        return getPosition().distance(particle.getPosition()) < getRadius() + particle.getRadius();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return Objects.equals(id, particle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Models.Particle{" +
                "id='" + id + '\'' +
                ", position=" + position +
                ", velocity=" + velocity +
                ", acceleration=" + acceleration +
                ", mass=" + mass +
                '}';
    }
}
