package Helpers;

import Models.Particle;
import Models.Wall;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class AnimationBuilder {

    private int precision = 8;
    private StringBuilder sb = new StringBuilder();

    public AnimationBuilder(List<Wall> walls) {
        for (Wall w: walls) {
            sb.append(w.getStart().getX() + " " + w.getStart().getY() + " " + w.getEnd().getX() + " " + w.getEnd().getY() + " ");
        }
        sb.append("\n");
    }

    public void addParticlesforNextFrame(Collection<Particle> particles) {
        for (Particle p : particles) {
            sb.append(String.format(new Locale("en", "us"),"%s ", p.getID()));
            sb.append(String.format(new Locale("en", "us"),"%."+precision+"f ", p.getPosition().getX()));
            sb.append(String.format(new Locale("en", "us"),"%."+precision+"f ", p.getPosition().getY()));
            sb.append(String.format(new Locale("en", "us"),"%."+precision+"f ", p.getRadius()));
        }
        sb.append("\n");
    }

    public String getString() {
        return sb.toString();
    }
}
