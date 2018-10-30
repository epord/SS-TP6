package Models;

public class Wall {
    private Vector start;
    private Vector end;

    public Wall(Vector start, Vector end) {
        this.start = start;
        this.end = end;
    }

    public Vector getStart() {
        return start;
    }

    public Vector getEnd() {
        return end;
    }

    public Double getLength() {
        return start.distance(end);
    }

    public Double getAngle() {
        return Math.acos((end.getX() - start.getX()) / getLength());
    }

    public Vector getNormalVersor() {
        Vector tangent = getTangentVersor();
        return new Vector(-tangent.getY(), tangent.getX());
    }

    public Vector getTangentVersor() {
        return end.subtract(start).normalize();
    }

    public Double distanceToParticle(Particle p) {
        Double particleAnglefromStart = Math.atan2((p.getPosition().getY()-start.getY()), (p.getPosition().getX()-start.getX()));
        Double particleAnglefromEnd = Math.atan2(-(p.getPosition().getY()-end.getY()), -(p.getPosition().getX()-end.getX()));

        Double wallAngle = Math.atan2((end.getY()-start.getY()), (end.getX()-start.getX()));

        Double relativeAngleFromStart = particleAnglefromStart - wallAngle;
        Double relativeAngleFromEnd = particleAnglefromEnd - wallAngle;


        Double distanceFromStart = start.distance(p.getPosition());
        Double distanceFromEnd = end.distance(p.getPosition());

        if (Math.abs(relativeAngleFromStart) > Math.PI/2 || Math.abs(relativeAngleFromEnd) > Math.PI/2) {
            return Double.POSITIVE_INFINITY; // the particle is not in a perpendicular distance
        }

        return Math.abs(Math.sin(relativeAngleFromStart) * distanceFromStart);
    }
}
