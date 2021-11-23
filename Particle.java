/**
 * Particle.java
 * <p>
 * Represents a Particle (a point mass) and its position,
 * velocity, mass, color, and the net force acting upon it.
 *
 * @author chindesaurus
 * @version 1.00
 */

import edu.princeton.cs.algs4.StdRandom;

import java.awt.Color;

public class Particle {

    // gravitational constant
    private static final double G = 6.67e-11;

    private double rx, ry;       // position
    private double vx, vy;       // velocity
    private double fx, fy;       // force
    private double mass;         // mass
    private Color color;         // color
    private double r;            //radius


    //some arguments about Wall
    double half = CollisionSystem.wall.getLength() / 2.0;
    double Xmid = CollisionSystem.wall.getXmid();
    double Ymid = CollisionSystem.wall.getYmid();

    /**
     * Constructor: creates and initializes a new Particle.
     *
     * @param rx    the x-position of this new Particle
     * @param ry    the y-position of this new Particle
     * @param vx    the x-velocity of this new Particle
     * @param vy    the y-velocity of this new Particle
     * @param mass  the mass of this new Particle
     * @param color the color of this new Particle (RGB)
     */
    public Particle(double r, double rx, double ry, double vx, double vy, double mass, Color color) {
        this.rx = rx;
        this.ry = ry;
        this.vx = vx;
        this.vy = vy;
        this.mass = mass;
        this.color = color;
        this.r = r;
    }

    public Particle(double rx, double ry, double vx, double vy, double mass, Color color) {
        this.rx = rx;
        this.ry = ry;
        this.vx = vx;
        this.vy = vy;
        this.mass = mass;
        this.color = color;
    }

    public Particle() {
        rx = StdRandom.uniform(0.0, 1.0);
        ry = StdRandom.uniform(0.0, 1.0);
        vx = StdRandom.uniform(-0.005, 0.005);
        vy = StdRandom.uniform(-0.005, 0.005);
        r = 0.02;
        mass = 0.5;
        color = Color.BLACK;
    }


    /**
     * Updates the velocity and position of the invoking Particle
     * using leapfrom method, with timestep dt.
     *
     * @param dt the timestep for this simulation
     */
    public void update(double dt) {
        vx += dt * fx / mass;
        vy += dt * fy / mass;
        rx += dt * vx;
        ry += dt * vy;
    }

    /**
     * Returns the Euclidean distance between the invoking Particle and b.
     *
     * @param b the Particle from which to determine the distance
     * @return the distance between this and Particle b
     */
    public double distanceTo(Particle b) {
        double dx = rx - b.rx;
        double dy = ry - b.ry;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double velocityTo(Particle b) {
        double dvx = b.vx - this.vx;
        double dvy = b.vy - this.vy;
        return Math.sqrt(dvx * dvx + dvy * dvy);
    }

    public boolean collideWithP(Particle b) {
        if(b == this) return false;
        else return distanceTo(b) <= this.r + b.r;
    }

    public boolean collideWithW() {
        return !(this.rx + this.r <= half + Xmid
                && this.rx - this.r >= 0
                && this.ry + this.r <= half + Ymid
                && this.ry - this.r >= 0);
    }


    public static double alreadyCollideTimeWithP(Particle a, Particle b) {
        assert a.collideWithP(b);
        try {
            return (a.r + b.r - a.distanceTo(b)) / a.velocityTo(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public double[] alreadyCollideTimeWithW() {
        assert this.collideWithW();
        double[] back = {0, 0};
        double TimeCollideVertical = -1;
        double TimeCollideHorizon = -1;
        if (rx + r - half - Xmid >= 0) {
            TimeCollideVertical = (rx + r - half - Xmid) / vx;
        }
        if (rx - r <= 0) {
            TimeCollideVertical = (-rx + r) / (-vx);
        }
        if (ry + r - half - Ymid >= 0) {
            TimeCollideHorizon = (ry + r - half - Ymid) / vy;
        }
        if (ry - r <= 0) {
            TimeCollideHorizon = (-ry + r) / (-vy);
        }

        if (TimeCollideVertical != -1 && TimeCollideHorizon != -1) {
            back[1] = TimeCollideHorizon <= TimeCollideVertical ? TimeCollideHorizon : TimeCollideVertical;
            back[0] = TimeCollideHorizon < TimeCollideVertical ? 2 : 1;
            if (TimeCollideHorizon == TimeCollideVertical) back[0] = 3;
        } else if (TimeCollideVertical != -1) {
            back[0] = 1;
            back[1] = TimeCollideVertical;
        } else {
            back[0] = 2;
            back[1] = TimeCollideHorizon;
        }

        return back;
    }

    /*public double alreadyCollideTimeWithW() {
        assert this.collideWithW();
        double TimeCollideVertical = -1;
        double TimeCollideHorizon = -1;
        if (rx + r - half - Xmid >= 0) {
            TimeCollideVertical = (rx + r - half - Xmid) / vx;
        }
        if (rx - r <= 0) {
            TimeCollideVertical = (-rx + r) / (-vx);
        }
        if (ry + r - half - Ymid >= 0) {
            TimeCollideHorizon = (ry + r - half - Ymid) / vy;
        }
        if (ry - r <= 0) {
            TimeCollideHorizon = (-ry + r) / (-vy);
        }

        if (TimeCollideVertical != -1 && TimeCollideHorizon != -1) {
            return TimeCollideHorizon <= TimeCollideVertical ? TimeCollideHorizon : TimeCollideVertical;
        } else if (TimeCollideVertical != -1) {
            return TimeCollideVertical;
        } else {
            return TimeCollideHorizon;
        }

    }*/
    
    public void reverse(double t) {
        this.rx -= t * vx;
        this.ry -= t * vy;
    }

    /**
     * Updates the velocities of this particle and the specified particle according
     * to the laws of elastic collision. Assumes that the particles are colliding
     * at this instant.
     *
     * @param  that the other particle
     */
    public void bounceOff(Particle that, double time) {
        double dx  = that.rx - this.rx;
        double dy  = that.ry - this.ry;
        double dvx = that.vx - this.vx;
        double dvy = that.vy - this.vy;
        double dvdr = dx*dvx + dy*dvy;             // dv dot dr
        double dist = this.r + that.r;   // distance between particle centers at collison

        // magnitude of normal force
        double magnitude = 2 * this.mass * that.mass * dvdr / ((this.mass + that.mass) * dist);

        // normal force, and in x and y directions
        double fx = magnitude * dx / dist;
        double fy = magnitude * dy / dist;

        // update velocities according to normal force
        this.vx += fx / this.mass;
        this.vy += fy / this.mass;
        that.vx -= fx / that.mass;
        that.vy -= fy / that.mass;

        this.rx += this.vx * time;
        this.ry += this.vy * time;
        that.rx += that.vx * time;
        that.ry += that.vy * time;
    }

    /**
     * Updates the velocity of this particle upon collision with a vertical
     * wall (by reflecting the velocity in the <em>x</em>-direction).
     * Assumes that the particle is colliding with a vertical wall at this instant.
     */
    public void bounceOffVerticalWall(double time) {
        vx = -vx;
        this.rx += this.vx * time;
        this.ry += this.vy * time;
    }

    /**
     * Updates the velocity of this particle upon collision with a horizontal
     * wall (by reflecting the velocity in the <em>y</em>-direction).
     * Assumes that the particle is colliding with a horizontal wall at this instant.
     */
    public void bounceOffHorizontalWall(double time) {
        vy = -vy;
        this.rx += this.vx * time;
        this.ry += this.vy * time;
    }


    /**
     * Resets the force (both x- and y-components) of the invoking Particle to 0.
     */
    public void resetForce() {
        fx = 0.0;
        fy = 0.0;
    }

    /**
     * Computes the net force acting between the invoking Particle and b, and
     * adds this to the net force acting on the invoking Particle.
     *
     * @param b the Particle whose net force on this Particle to calculate
     */
    public void addForce(Particle b) {
        Particle a = this;
        double EPS = 3E4;      // softening parameter
        double dx = b.rx - a.rx;
        double dy = b.ry - a.ry;
        double dist = Math.sqrt(dx * dx + dy * dy);
        double F = (G * a.mass * b.mass) / (dist * dist + EPS * EPS);
        a.fx += F * dx / dist;
        a.fy += F * dy / dist;
    }

    /**
     * Draws the invoking Particle.
     */
    public void draw() {
        StdDraw.setPenColor(color);
        StdDraw.point(rx, ry);
    }

    /**
     * Returns a string representation of this Particle formatted nicely.
     *
     * @return a formatted string containing this Particle's x- and y- positions,
     * velocities, and mass
     */
    public String toString() {
        return String.format("%10.3E %10.3E %10.3E %10.3E %10.3E", rx, ry, vx, vy, mass);
    }

    /**
     * Returns true if the Particle is in quadrant q, else false.
     *
     * @param q the Quad to check
     * @return true iff Particle is in Quad q, else false
     */
    public boolean in(Quad q) {
        return q.contains(this.rx, this.ry);
    }

    /**
     * Returns a new Particle object that represents the center-of-mass
     * of the invoking Particle and b.
     *
     * @param b the Particle to aggregate with this Particle
     * @return a Particle object representing an aggregate of this
     * and b, having this and b's center of gravity and
     * combined mass
     */
    public Particle plus(Particle b) {
        Particle a = this;

        double m = a.mass + b.mass;
        double x = (a.rx * a.mass + b.rx * b.mass) / m;
        double y = (a.ry * a.mass + b.ry * b.mass) / m;

        return new Particle(x, y, a.vx, b.vx, m, a.color);
    }
}
