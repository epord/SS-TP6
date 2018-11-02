import Helpers.OctaveBuilder;
import Models.Particle;
import Models.Room;
import Models.Vector;
import javafx.scene.control.RadioMenuItem;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {

    private static Double roomSize = 20.0;
    private static Integer maxPeopleCount = 300;
    private static Double rmin = 0.1;
    private static Double rmax = 0.37;

    public static void main(String[] args) {
        Integer iterations = 2;
        Integer flowHalfWindow = 10;
        Random random = new Random();
        List<List<Double>> escapeTimesList = new ArrayList<>();
        List<List<Pair<Double, Double>>> flows = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            List<Double> exitTimes = simulate(1.0,maxPeopleCount, random);
            escapeTimesList.add(exitTimes);
            flows.add(calculateFlow(exitTimes, flowHalfWindow));
        }
        System.out.println(OctaveBuilder.plot(escapeTimesList, "Cantidad de particulas evacuadas", "Tiempo [s]"));
        System.out.println(OctaveBuilder.plotMean(escapeTimesList, "Cantidad de particulas evacuadas", "Tiempo [s]"));
        System.out.println(OctaveBuilder.plotFlow(flows,"Tiempo [s]", "Caudal [particulas/s]"));
        System.out.println(OctaveBuilder.plotFlowMean(flows, "Tiempo [s]", "Caudal [particulas/s]"));

//        simulateSpeeds(1.0,4.0,0.5,3,random);
    }

    public static List<Pair<Double, Double>> calculateFlow(List<Double> times, Integer windowHalfSize) {
        List<Pair<Double, Double>> flow = new ArrayList<>();

        for (int i = windowHalfSize; i < times.size() - 1 - windowHalfSize; i++) {
            flow.add(new Pair(times.get(i), (2 * windowHalfSize + 1) / (times.get(i+windowHalfSize) - times.get(i-windowHalfSize))));
        }

        return flow;
    }

    private static void simulateSpeeds(Double start, Double end, Double inteval, Integer iterations, Random random){
        List<List<List<Double>>> escapeListVel = new ArrayList<>();
        List<Double> velocities = new ArrayList<>();
        for (double i = start; i <= end; i+=inteval) {
            List<List<Double>> escapeList = new ArrayList<>();
            for (int j = 0; j < iterations; j++) {
                escapeList.add(simulate(i,maxPeopleCount,random));
            }
            velocities.add(i);
            escapeListVel.add(escapeList);
        }
        System.out.println(OctaveBuilder.plotEscape(escapeListVel,velocities));
    }

    private static List<Double> simulate(Double speed, Integer amount, Random random){
        List<Particle> persons = generateParticles(rmin,amount, roomSize, random);
        Room room = new Room(persons, roomSize, 1.2, speed,rmin,rmax);
        List<Double> escapeTimes = room.simulateEscape();
        System.out.println("The escape took " + escapeTimes.get(escapeTimes.size() - 1) + "s");
        return escapeTimes;
    }

    private static List<Particle> generateParticles(Double rmin, Integer amount, Double roomSize, Random r) {
        List<Particle> particles = new ArrayList<>();
        Integer fails = 0;

        while(particles.size() < amount && fails < 1000) {
            Double x = r.nextDouble() * (roomSize - 2 * rmin);
            Double y = r.nextDouble() * (roomSize - 2 * rmin);
            Particle p = new Particle(particles.size(), new Vector(x, y), 80.0, rmin);
            if (particles.parallelStream().anyMatch(p::isCollidingWith)) {
                fails++;
            } else {
                particles.add(p);
                fails = 0;
            }
        }

        return particles;
    }

}
