import Models.Particle;
import Models.Room;
import Models.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Long timeCheckpoint = System.currentTimeMillis();
        Long startTime = timeCheckpoint;

//        Particle p1 = new Particle("1", new Vector(10.0, 15.0), 80.0, 0.25);
//        Particle p2 = new Particle("2", new Vector(10.0, 10.0), 80.0, 0.25);
//        Particle p3 = new Particle("3", new Vector(10.0, 5.0), 80.0, 0.5);
//        Particle p4 = new Particle("4", new Vector(18.5, 7.3), 80.0, 0.5);
//        List<Particle> persons = Arrays.asList(p1, p2, p3, p4);

        System.out.println("Generating persons...");
        List<Particle> persons = generateParticles(0.25, 0.29, 200, 20.0);
        System.out.println(persons.size() + " persons generated in " + ((System.currentTimeMillis() - timeCheckpoint)/1000.0) + "s\n");
        timeCheckpoint = System.currentTimeMillis();

        Room room = new Room(persons, 20.0, 1.2);

        System.out.println("Escaping...");
        room.simulateEscape();
        System.out.println("Escape simulated finished in " + ((System.currentTimeMillis() - timeCheckpoint)/1000.0) + "s\n");

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
