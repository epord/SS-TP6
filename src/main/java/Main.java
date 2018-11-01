import Models.Particle;
import Models.Room;
import Models.Vector;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    private static Double roomSize = 20.0;
    private static Integer maxPeopleCount = 300;

    public static void main(String[] args) {
        Long timeCheckpoint = System.currentTimeMillis();
        Long startTime = timeCheckpoint;

        System.out.println("Generating persons...");
        List<Particle> persons = generateParticles(0.1, 0.37, maxPeopleCount, roomSize);
        System.out.println(persons.size() + " persons generated in " + ((System.currentTimeMillis() - timeCheckpoint)/1000.0) + "s\n");
        timeCheckpoint = System.currentTimeMillis();

        Room room = new Room(persons, roomSize, 1.2);

        System.out.println("Escaping...");
        List<Double> escapeTimes = room.simulateEscape();
        System.out.println("The escape took " + escapeTimes.get(escapeTimes.size() - 1) + "s\n");
        System.out.println(escapeTimes);

        System.out.println("Escape simulated finished in " + ((System.currentTimeMillis() - timeCheckpoint)/1000.0) + "s");
        System.out.println("Total time elapsed: " + ((System.currentTimeMillis() - startTime)/1000.0) + "s");
    }

    private static List<Particle> generateParticles(Double Rmin, Double Rmax, Integer amount, Double roomSize) {
        Random r = new Random();
        List<Particle> particles = new ArrayList<>();
        Integer fails = 0;

        while(particles.size() < amount && fails < 1000) {
            Double x = r.nextDouble() * (roomSize - 2 * Rmax);
            Double y = r.nextDouble() * (roomSize - 2 * Rmax);
            Double radius = r.nextDouble() * (Rmax - Rmin) + Rmin;
            Particle p = new Particle(particles.size(), new Vector(x, y), 80.0, radius);
            if (particles.parallelStream().anyMatch(other -> p.isCollidingWith(other))) {
                fails++;
            } else {
                particles.add(p);
                fails = 0;
            }
        }

        return particles;
    }

}
