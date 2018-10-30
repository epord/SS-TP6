package Models;

import Helpers.AnimationBuilder;
import Helpers.FileManager;

import java.util.List;
import java.util.stream.Collectors;

public class Room {
    private List<Wall> walls;
    private List<Particle> persons;
    private Vector escapePoint;

    private Double Rmin = 0.2; // m
    private Double Rmax = 0.6; // m
    private Double maxSpeed = 3.2; // m/s
    private Double escapingSpeed = maxSpeed; // m/s
    private double beta = 1.0;
    private double tau = 1.0;

    private Double deltaT = 0.01; // s
    private Double simulationTime = 20.0; // s
    private Double animationFramePerSecond = 60.0; // fps
    private Integer animationCurrentFrame = 0;

    public Room(List<Wall> walls, List<Particle> persons, Vector escapePoint) {
        this.walls = walls;
        this.persons = persons;
        this.escapePoint = escapePoint;
    }

    public void addWall(Wall wall) {
        walls.add(wall);
    }

    public void addPerson(Particle person) {
        persons.add(person);
    }

    public void simulateEscape() {
        AnimationBuilder ab = new AnimationBuilder(walls);
        FileManager fm = new FileManager();

        for (Double t = 0.0; t < simulationTime; t += deltaT) {
            persons = persons.parallelStream().map(person -> {
                List<Particle> personCollisions = persons
                                            .parallelStream()
                                            .filter(other -> !person.equals(other) && person.isCollidingWith(other))
                                            .collect(Collectors.toList());

                List<Wall> wallsCollisions = walls
                                            .parallelStream()
                                            .filter(wall -> person.isCollidingWith(wall))
                                            .collect(Collectors.toList());

                if (personCollisions.size() == 0 && wallsCollisions.size() == 0) {
                    Vector velocityVersor = escapePoint.subtract(person.getPosition()).normalize();
                    Double speed = maxSpeed * Math.pow((person.getRadius() - Rmin) / (Rmax - Rmin), beta);
                    Vector velocity = velocityVersor.dot(speed);
                    Vector position = person.getPosition().add(velocity.dot(deltaT));
                    Double radius = Math.min(person.getRadius() + Rmax / (tau / deltaT), Rmax);
                    return person.getCopyWithRadius(radius).getCopyWithPosition(position).getCopyWithVelocity(velocity);
                } else {
                    Vector velocityVersor = new Vector();
                    for(Particle other: personCollisions) {
                        velocityVersor = velocityVersor.add(person.getPosition().subtract(other.getPosition()));
                    }
                    for(Wall wall: wallsCollisions) {
                        velocityVersor = velocityVersor.add(wall.getNormalVersor());
                    }
                    velocityVersor = velocityVersor.normalize();
                    Vector velocity = velocityVersor.dot(escapingSpeed);
                    Vector position = person.getPosition().add(velocity.dot(deltaT));
                    return person.getCopyWithRadius(Rmin).getCopyWithPosition(position).getCopyWithVelocity(velocity);
                }
            }).collect(Collectors.toList());

            if (animationCurrentFrame <= t * animationFramePerSecond) {
                ab.addParticlesforNextFrame(persons);
                animationCurrentFrame++;
            }
        }

        fm.writeString("p5/frontend/output.txt", ab.getString());
    }
}
