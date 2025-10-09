package com.machina.api.starchart.obj;

import net.minecraft.world.phys.Vec3;

public interface Celestial {
    
    double radiusAU();
    
    Orbit orbit();
    
    String texture_fg();
    
    String texture_bg();
    
    double a();
    
    double e();
    
    double orb_period();
    
    double where_in_orbit();
    
    String name();
    
    default Vec3 calculateOrbitalCoordinates(double t) {
        double a = a();
        double e = e();
        double trueAnomaly = calculateTrueAnomaly(t, e);

        double x = a * (Math.cos(trueAnomaly) - e);
        double z = a * Math.sqrt(1 - e * e) * Math.sin(trueAnomaly);

        return new Vec3(x, 0, z);
    }
    
    default double trueAnomalyFromMean(double M, double e) {
        double E = calculateEccentricAnomaly(M, e);
        return 2 * Math.atan(Math.sqrt((1 + e) / (1 - e)) * Math.tan(E / 2));
    }

    default double calculateTrueAnomaly(double t, double e) {
        // Orbital parameters
        double meanMotion = 2 * Math.PI / orb_period();
        double meanAnomaly = meanMotion * t + where_in_orbit();

        double eccentricAnomaly = calculateEccentricAnomaly(meanAnomaly, e);
        return 2 * Math.atan(Math.sqrt((1 + e) / (1 - e)) * Math.tan(eccentricAnomaly / 2));
    }

    default double calculateEccentricAnomaly(double meanAnomaly, double e) {
        double E = meanAnomaly;
        double tolerance = 1e-9;
        int maxIterations = 1000;
        int iterations = 0;

        do {
            double nextE = E - ((E - e * Math.sin(E) - meanAnomaly) / (1 - e * Math.cos(E)));
            if (Math.abs(nextE - E) < tolerance) {
                E = nextE;
                break;
            }
            E = nextE;
            iterations++;
        } while (iterations < maxIterations);

        return E;
    }

}
