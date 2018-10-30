package Models;

import Helpers.AnimationBuilder;
import Helpers.FileManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Room {
    private List<Wall> walls;
    private List<Particle> persons;
    private Vector escapePoint;
    private Double roomSize;
    private Double doorWidth;

    private Double Rmin = 0.2; // m
    private Double Rmax = 0.37; // m
    private Double maxSpeed = 1.0; // m/s
    private Double escapingSpeed = maxSpeed; // m/s
    private double beta = 1.0;
    private double tau = 0.50;

    private Double deltaT = 0.05; // s
    private Double simulationTime = 300.0; // s
    private Double animationFramePerSecond = 60.0; // fps
    private Integer animationCurrentFrame = 0;

    public Room(List<Particle> persons, Double roomSize, Double doorWidth) {
        this.persons = persons;
        this.roomSize = roomSize;
        this.doorWidth = doorWidth;

        this.walls = generateWalls(roomSize, doorWidth);
        this.escapePoint = new Vector(roomSize, roomSize/2);
    }

    private List<Wall> generateWalls(Double roomSize, Double doorWidth) {
        Wall top = new Wall(new Vector(roomSize, roomSize), new Vector(0.0, roomSize));
        Wall left = new Wall(new Vector(0.0, roomSize), new Vector(0.0, 0.0));
        Wall bottom = new Wall(new Vector(0.0, 0.0), new Vector(roomSize, 0.0));
        Wall right1 = new Wall(new Vector(roomSize, 0.0), new Vector(roomSize, (roomSize - doorWidth)/2));
        Wall right2 = new Wall(new Vector(roomSize, (roomSize + doorWidth)/2), new Vector(roomSize, roomSize));
        return Arrays.asList(top, left, bottom, right1, right2);
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

                Vector velocityVersor, position;
                Double speed, radius;
                if (personCollisions.size() == 0 && wallsCollisions.size() == 0) {
                    // Not colliding
                    if (person.getPosition().getX() > roomSize || person.getPosition().subtract(escapePoint).norm() < doorWidth/2) {
                        // Got out
                        velocityVersor = new Vector(100.0, escapePoint.getY()).subtract(person.getPosition()).normalize();
                    } else {
                        velocityVersor = escapePoint.subtract(person.getPosition()).normalize();
                    }
                    speed = maxSpeed * Math.pow((person.getRadius() - Rmin) / (Rmax - Rmin), beta);
                    radius = Math.min(person.getRadius() + Rmax / (tau / deltaT), Rmax);
                } else {
                    // Colliding
                    velocityVersor = new Vector();
                    for(Particle other: personCollisions) {
                        velocityVersor = velocityVersor.add(person.getPosition().subtract(other.getPosition()));
                    }
                    for(Wall wall: wallsCollisions) {
                        velocityVersor = velocityVersor.add(wall.getNormalVersor());
                    }
                    velocityVersor = velocityVersor.normalize();
                    speed = escapingSpeed;
                    radius = Rmin;
                }
                Vector velocity = velocityVersor.dot(speed);
                position = person.getPosition().add(velocity.dot(deltaT));
                return person.getCopyWithRadius(radius).getCopyWithPosition(position).getCopyWithVelocity(velocity);
            }).collect(Collectors.toList());


            deleteExitedPersons();
            generateAnimationFrame(ab, t);
        }

        fm.writeString("p5/frontend/output.txt", ab.getString());
    }

    private void deleteExitedPersons() {
        persons = persons
                .parallelStream()
                .filter(person -> person.getPosition().getX() < roomSize * 1.2)
                .collect(Collectors.toList());
    }

    private void generateAnimationFrame(AnimationBuilder ab, Double time) {
        if (animationCurrentFrame <= time * animationFramePerSecond) {
            ab.addParticlesforNextFrame(persons);
            animationCurrentFrame++;
        }
    }
}
