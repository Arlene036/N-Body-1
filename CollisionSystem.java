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
        for (double t = 0.0; t<60; t = t + dt) {

            Quad quad = new Quad(0, 0, 300 * 2);
            BHTree tree = new BHTree(quad);


            //TO CONSIDER COLLISION!

            for(int i = 0 ; i < n ;i++){
                double minT = -1;
                Particle that = null;
                for(int j = i+1 ; j < n ; j++){
                    /*if(particles[i].collideWithP(particles[j])){
                        if(minT == -1 ){
                            minT = Particle.alreadyCollideTimeWithP(particles[i],particles[j]);
                            that = particles[j];
                        } else if(Particle.alreadyCollideTimeWithP(particles[i],particles[j])<minT){
                            minT = Particle.alreadyCollideTimeWithP(particles[i],particles[j]);
                            that = particles[j];
                        }
                    }*/
                }

                if(particles[i].collideWithW() && particles[i].alreadyCollideTimeWithW()[1]< minT){
                    minT = particles[i].alreadyCollideTimeWithW()[1];
                    that = null;
                }

                if(minT==-1) continue;

                particles[i].reverse(minT);

                if(that!=null){
                    particles[i].bounceOff(that,minT);
                }else{
                    if(particles[i].alreadyCollideTimeWithW()[0]==1){
                        particles[i].bounceOffVerticalWall(minT);
                    } else if (particles[i].alreadyCollideTimeWithW()[0]==2){
                        particles[i].bounceOffHorizontalWall(minT);
                    } else{
                        particles[i].bounceOffVerticalWall(minT);
                        particles[i].bounceOffHorizontalWall(minT);
                    }
                }
            }

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


            // draw the Particle
            StdDraw.clear();
            for (int i = 0; i < n; i++)
                particles[i].draw();

            StdDraw.show();
            StdDraw.pause(10);
        }
    }

    public static void main(String[] args) {
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

        StdDraw.enableDoubleBuffering();
        //StdDraw.setCanvasSize(square, square);

        StdDraw.setXscale(0, square);
        StdDraw.setYscale(0, square);

        CollisionSystem system = new CollisionSystem(particles);
        system.simulate(0.1,n);
    }
}
