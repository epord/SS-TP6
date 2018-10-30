import Models.Particle;
import Models.Room;
import Models.Vector;
import Models.Wall;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Long startTime = System.currentTimeMillis();

        Wall top = new Wall(new Vector(20.0, 20.0), new Vector(0.0, 20.0));
        Wall left = new Wall(new Vector(0.0, 20.0), new Vector(0.0, 0.0));
        Wall bottom = new Wall(new Vector(0.0, 0.0), new Vector(20.0, 0.0));
        Wall right1 = new Wall(new Vector(20.0, 0.0), new Vector(20.0, 9.4));
        Wall right2 = new Wall(new Vector(20.0, 10.6), new Vector(20.0, 20.0));
        List<Wall> walls = Arrays.asList(top, left, bottom, right1, right2);

        Particle p1 = new Particle("1", new Vector(10.0, 15.0), 80.0, 0.25);
        Particle p2 = new Particle("2", new Vector(10.0, 10.0), 80.0, 0.25);
        Particle p3 = new Particle("3", new Vector(10.0, 5.0), 80.0, 0.5);
        Particle p4 = new Particle("4", new Vector(18.5, 7.3), 80.0, 0.5);
        List<Particle> persons = Arrays.asList(p1, p2, p3, p4);

        Vector escapePoint = new Vector(20.0, 10.0);

        Room room = new Room(walls, persons, escapePoint);
        room.simulateEscape();


        System.out.println("Time elapsed: " + ((System.currentTimeMillis() - startTime)/1000.0) + " s");
    }

}
