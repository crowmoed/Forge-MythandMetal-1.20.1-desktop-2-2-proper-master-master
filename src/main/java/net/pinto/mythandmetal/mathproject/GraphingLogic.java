package net.pinto.mythandmetal.mathproject;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GraphingLogic {

    public static void plotStandardForm(Level world, Vec3 origin, double a, double b, double c, double stepSize) {
        if (world instanceof ServerLevel serverWorld) {
            // Send graph information to players
            String range1 = ("-inf");
            String range2 = ("inf");
            double vertexX2 = -b / (2 * a); // Calculate x-coordinate of vertex
            double vertexY = (a * Math.pow(vertexX2, 2)) + (b * vertexX2) + c; // Calculate y-coordinate

            if (a > 0){
                range1 = ""+vertexY ;}
            else if (a < 0){
                range2 = ""+vertexY ;}
            else {
                range1 = "-inf";
                range2 = "inf";}

                for (ServerPlayer player : serverWorld.players()) {
                player.sendSystemMessage(Component.literal("Standard Form: y = " + a + "x^2 + " + b + "x + " + c));
                player.sendSystemMessage(Component.literal("Domain: x from -" + "inf" + " to " + "inf" + "\nRange: y from "+ range1 + " to "+ range2));
                player.sendSystemMessage(Component.literal("Axis of Symmetry: x = " + (-b / (2 * a))));
            }

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(() -> {


                //  x-axis
                for (double x = -1000; x <= 1000; x += stepSize) {
                    serverWorld.sendParticles(ParticleTypes.END_ROD, origin.x + x, origin.y, origin.z, 1, 0, 0, 0, 0);
                }

                //  y-axis
                for (double y = -1000; y <= 1000; y += stepSize) {
                    serverWorld.sendParticles(ParticleTypes.END_ROD, origin.x, origin.y + y, origin.z, 1, 0, 0, 0, 0);
                }

                //  parabola
                    for (double x = -1000; x <= 1000; x += stepSize) {
                    double y = a * x * x + b * x + c;

                    double particleX = origin.x + x;
                    double particleY = origin.y + y;
                    double particleZ = origin.z;

                    serverWorld.sendParticles(ParticleTypes.FLAME, particleX, particleY, particleZ, 1, 0, 0, 0, 0);
                }

                double vertexX = -b / (2 * a);
                for (double y = origin.y; y <= origin.y + (a * vertexX * vertexX + b * vertexX + c); y += stepSize) {
                    serverWorld.sendParticles(ParticleTypes.END_ROD, origin.x + vertexX, y, origin.z, 1, 0, 0, 0, 0);
                }
            }, 0, 1, TimeUnit.SECONDS);

            executor.schedule(executor::shutdown, 60, TimeUnit.SECONDS);
        }
    }

    public static void plotVertexForm(Level world, Vec3 origin, double a, double h, double k, double stepSize) {
        if (world instanceof ServerLevel serverWorld) {
            String range1 = ("-inf");
            String range2 = ("inf");

            if (a > 0){
                range1 = ""+k ;}
            else if (a < 0){
                range2 = ""+k ;}
            else {
                range1 = "-inf";
                range2 = "inf";}
            for (ServerPlayer player : serverWorld.players()) {
                player.sendSystemMessage(Component.literal("Plotting Vertex Form: y = " + a + "(x - " + h + ")^2 + " + k));
                player.sendSystemMessage(Component.literal("Domain: x from -" + "inf" + " to " + "inf" + "\nRange: y from "+ range1 + " to "+ range2));
                player.sendSystemMessage(Component.literal("Axis of Symmetry: x = " + h));
            }

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(() -> {

                //  x-axis
                for (double x = -1000; x <= 1000; x += stepSize) {
                    serverWorld.sendParticles(ParticleTypes.END_ROD, origin.x + x, origin.y, origin.z, 1, 0, 0, 0, 0);
                }

                //  y-axis
                for (double y = -1000; y <= 1000; y += stepSize) {
                    serverWorld.sendParticles(ParticleTypes.END_ROD, origin.x, origin.y + y, origin.z, 1, 0, 0, 0, 0);
                }

                //  parabola
                for (double x = -1000; x <= 1000 ; x += stepSize) {
                    double y = a * Math.pow(x - h, 2) + k;

                    double particleX = origin.x + x;
                    double particleY = origin.y + y;
                    double particleZ = origin.z;

                    serverWorld.sendParticles(ParticleTypes.FLAME, particleX, particleY, particleZ, 3, 0, 0, 0, 0);
                }

                //  axis of symmetry
                for (double y = origin.y; y <= origin.y + k; y += stepSize) {
                    serverWorld.sendParticles(ParticleTypes.END_ROD, origin.x + h, y, origin.z, 1, 0, 0, 0, 0);
                }
            }, 0, 1, TimeUnit.SECONDS);

            executor.schedule(executor::shutdown, 60, TimeUnit.SECONDS);
        }
    }
    public static void plotInterceptForm(Level world, Vec3 origin, double a, double b, double c, double stepSize) {
        if (world instanceof ServerLevel serverWorld) {
            // Send graph information to players
            String range1 = ("-inf");
            String range2 = ("inf");
            double vertexX2 = (b+c)/2 ;
            double vertexY = a * (  vertexX2-b) * (vertexX2-c); // Calculate y-coordinate

            if (a > 0){
                range1 = ""+vertexY ;}
            else if (a < 0){
                range2 = ""+vertexY ;}
            else {
                range1 = "-inf";
                range2 = "inf";}

            for (ServerPlayer player : serverWorld.players()) {
                player.sendSystemMessage(Component.literal("Intercept Form: y = " + a + "(x + " + b + ") (x + " + c+")"));
                player.sendSystemMessage(Component.literal("Domain: x from -" + "inf" + " to " + "inf" + "\nRange: y from "+ range1 + " to "+ range2));
                player.sendSystemMessage(Component.literal("Axis of Symmetry: x = " + (-b-c)/(2)));
            }

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(() -> {


                //  x-axis
                for (double x = -1000; x <= 1000; x += stepSize) {
                    serverWorld.sendParticles(ParticleTypes.END_ROD, origin.x + x, origin.y, origin.z, 1, 0, 0, 0, 0);
                }

                //  y-axis
                for (double y = -1000; y <= 1000; y += stepSize) {
                    serverWorld.sendParticles(ParticleTypes.END_ROD, origin.x, origin.y + y, origin.z, 1, 0, 0, 0, 0);
                }

                //  parabola
                for (double x = -1000; x <= 1000; x += stepSize) {
                    double y = a *((x+c)*(x+b));

                    double particleX = origin.x + x;
                    double particleY = origin.y + y;
                    double particleZ = origin.z;

                    serverWorld.sendParticles(ParticleTypes.FLAME, particleX, particleY, particleZ, 1, 0, 0, 0, 0);
                }


            }, 0, 1, TimeUnit.SECONDS);

            executor.schedule(executor::shutdown, 60, TimeUnit.SECONDS);
        }
    }
}
