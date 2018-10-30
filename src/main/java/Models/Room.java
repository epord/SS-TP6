package Models;

import java.util.List;
import java.util.stream.Collectors;

public class Room {
    private List<Wall> walls;
    private List<Particle> persons;
    private Vector escapePoint;

    private Double Rmin = 0.4; // m
    private Double Rmax = 0.8; // m
    private Double maxSpeed = 1.5; // m/s
    private Double deltaT = 0.1;

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
        persons = persons.parallelStream().map(person -> {
            Vector velocityVersor = escapePoint.subtract(person.getPosition()).normalize();
            Double speed = maxSpeed * (person.getRadius() - Rmin) / (Rmax - Rmin);
            Vector velocity = velocityVersor.dot(speed);
            Vector position = person.getPosition().add(velocity.dot(deltaT));
            return person.getCopyWithPosition(position).getCopyWithVelocity(velocity);
        }).collect(Collectors.toList());
    }
}
