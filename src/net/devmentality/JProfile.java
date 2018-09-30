package net.devmentality;

/**
 * A utility class that makes timing algorithms in java more convenient.
 *
 * @author Bradly Tillmann
 * 
 */

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;

public class JProfile <T extends Comparable<? super T>> {
	
    private String name;
    private int size;
    private int numProfiles;
    private double[] startTimeIndex;
    private double[] deltaTimeIndex;
    private double[] pauseStartTimeIndex;
    private double[] pauseDeltaTimeIndex;
    private Hashtable<T, Integer> startTimeTable;

    /* Some standard values to use when exporting csv files */
    public static final double Nanoseconds = 1e1;
    public static final double Microseconds = 1e3;
    public static final double Milliseconds = 1e6;
    public static final double Seconds = 1e9;
    public static final double Minutes = 6e10;
    public static final double Hours = 3.6e12;

    /**
     * Creates a new JProfile with the specified name, and maximum data points.
     * @param name the name of the profile. Will also determine the name of the exported csv file
     * @param maxSamples the maximum number of data points that can be held within this profile
     */
    public JProfile(String name, int maxSamples) {
        this.name = name;
        this.size = maxSamples;
        this.numProfiles = 0;

        this.startTimeIndex = new double[maxSamples];
        this.deltaTimeIndex = new double[maxSamples];
        this.pauseStartTimeIndex = new double[maxSamples];
        this.pauseDeltaTimeIndex = new double[maxSamples];
        this.startTimeTable =  new Hashtable<T, Integer>();
    }

    /**
     * Records the current time for the given sample id
     * @param id the id of the current data point that is being recorded
     */
    public void startSample(T id) {
        if(startTimeTable.containsKey(id)) {
            int idx = startTimeTable.get(id);
            startTimeIndex[idx] = System.nanoTime();
        }
        else {
            startTimeTable.put(id, numProfiles);
            startTimeIndex[numProfiles++] = System.nanoTime();
        }
    }

    /**
     * Determines the amount of time that has passed since profiling of the given sample id had started.
     * Records the difference in time within the delta time index.
     * @param id the id of the current data point being recorded
     * @param executions
     */
    public void stopSample(T id, int executions) {
        // Get the time first to minimize error
        double now = System.nanoTime();

        if(startTimeTable.containsKey(id)) {
            int idx = startTimeTable.get(id);
            deltaTimeIndex[idx] = ((now - startTimeIndex[idx]) - pauseDeltaTimeIndex[idx]) / (double)executions;
        }
        else {
            throw new NullPointerException();
        }
    }
    
    /**
     * Starts recording down time for the given sample. This will be subtracted from the total
     * run time when the sample is stopped.
     * @param id the id of the current data point being recorded
     */
    public void pauseSample(T id) {
    	double now = System.nanoTime();
    	
    	if(startTimeTable.containsKey(id)) {
            int idx = startTimeTable.get(id);
            pauseStartTimeIndex[idx] = now;
        }
        else {
            throw new NullPointerException();
        }
    }
    
    /**
     * Records the total elapsed down time, and adds it to the total down time for the current
     * sample. This down time will be subtracted from the total sample runtime.
     * @param id the id of the current data point being recorded
     */
    public void resumeSample(T id) {
    	if(startTimeTable.containsKey(id)) {
    		int idx = startTimeTable.get(id);
    		pauseDeltaTimeIndex[idx] += (System.nanoTime() - pauseStartTimeIndex[idx]);
    	}
    	else {
    		throw new NullPointerException();
    	}
    }

    /**
     * Exports the profile to a .csv file for use in excel and other spreadsheet applications.
     * @param scale the scale to use for the timing information
     */
    public void export(double scale) {
        String units = "";

        if(scale == Nanoseconds) {
            units = "ns";
        }
        else if(scale == Microseconds) {
            units = "us";
        }
        else if(scale == Milliseconds) {
            units = "ms";
        }
        else if(scale == Seconds) {
            units = "s";
        }
        else if(scale == Minutes) {
            units = "min";
        }
        else if(scale == Hours) {
            units = "hrs";
        }
        else {
        	units = "";
        }

        try {
            FileWriter file = new FileWriter(name + "_profile.csv");
            file.write("x,Execution Time (" + units + ")\n");

            ArrayList<T> x = new ArrayList<T>();

            for(Enumeration<T> e = startTimeTable.keys(); e.hasMoreElements();) {
                T idx = e.nextElement();
                x.add(idx);
            }

            Collections.sort(x);

            for(int idx = 0; idx < x.size(); idx++) {
                file.write(x.get(idx) + "," + deltaTimeIndex[startTimeTable.get(x.get(idx))] / scale + "\n");
            }

            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
