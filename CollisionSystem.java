import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;

import java.awt.*;

public class CollisionSystem {

    private Particle[] particles;

    public static Quad wall;

    public CollisionSystem(Particle[] particles) {
        this.particles = particles;
    }

    // dt:time quantum
    public void simulate(double dt, int n) {
        for (double t = 0.0; t< 10000; t = t + dt) {

            Quad quad = new Quad(0, 0, 300 * 2);
            BHTree tree = new BHTree(quad);



            // build the Barnes-Hut tree
            for (int i = 0; i < n; i++) {
                if (particles[i].in(quad))
                    tree.insert(particles[i]);
            }

            // update the forces, positions, velocities, and accelerations
            for (int i = 0; i < n; i++) {
                particles[i].resetForce();
                tree.updateForce(particles[i]);
                particles[i].update(dt); //Updates the velocity and position of the invoking Particle using leapfrom method, with timestep dt.
            }


            //TO CONSIDER COLLISION!
            for(int i = 0 ; i < n ;i++){
                for(int j = i+1 ; j < n ; j++){
                    if(particles[i].collideWithP(particles[j])){
                        particles[i].bounceOff(particles[j],Particle.alreadyCollideTimeWithP(particles[i],particles[j]));
                    }
                }
                if(particles[i].alreadyCollideTimeWithW()[0]==1){
                    particles[i].bounceOffVerticalWall(particles[i].alreadyCollideTimeWithW()[1]);
                } else if (particles[i].alreadyCollideTimeWithW()[0]==2){
                    particles[i].bounceOffHorizontalWall(particles[i].alreadyCollideTimeWithW()[1]);
                } else{
                    particles[i].bounceOffVerticalWall(particles[i].alreadyCollideTimeWithW()[1]);
                    particles[i].bounceOffHorizontalWall(particles[i].alreadyCollideTimeWithW()[1]);
                }
            }

            // draw the Particle
            StdDraw.clear();
            for (int i = 0; i < n; i++)
                particles[i].draw();

            StdDraw.show();
            StdDraw.pause(10);
        }
    }

    public static void main(String[] args) {

        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(600, 600);

        // the array of particles
        Particle[] particles;

        // create n random particles
        /*if (args.length == 1) {
            int n = Integer.parseInt(args[0]);
            particles = new Particle[n];
            for (int i = 0; i < n; i++)
                particles[i] = new Particle();
        }*/

        String type = StdIn.readString();
        int square = StdIn.readInt();
        wall = new Quad(square/2 , square/2 ,square);

        int n = StdIn.readInt();
        particles = new Particle[n];
        for (int i = 0; i < n; i++) {
            double rx = StdIn.readDouble();
            double ry = StdIn.readDouble();
            double vx = StdIn.readDouble();
            double vy = StdIn.readDouble();
            double radius = StdIn.readDouble();
            double mass = StdIn.readDouble();
            int r = StdIn.readInt();
            int g = StdIn.readInt();
            int b = StdIn.readInt();
            Color color = new Color(r, g, b);
            particles[i] = new Particle(radius, rx, ry, vx, vy, mass, color);
        }


        CollisionSystem system = new CollisionSystem(particles);
        system.simulate(0.1,n);
    }
}
