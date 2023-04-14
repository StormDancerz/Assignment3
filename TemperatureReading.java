import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.List;
import java.security.cert.X509CRLEntry;
import java.util.ArrayList; 
import java.util.Collections;

public class TemperatureReading {

    // threads represent each of the temperature sensors
    static final int numThreads = 8;
    static int numHours = 100;
    static Semaphore mutex = new Semaphore(1);

    // class TempThread for temperature sensors
    // has ID, an array of the sensor readings, and a boolean array of whether sensors are available 
    static class TempThread implements Runnable {
        public int ID;
        public int[] sensorReadings;
        public boolean[] sensorsAvailable;

        public TempThread(int ID, int[] sensorReadings, boolean[] sensorsAvailable) {
            this.ID = ID;
            this.sensorReadings = sensorReadings;
            this.sensorsAvailable = sensorsAvailable;
        }

        // run method that loops through the arrays and sets random values for the sensors
        // simulates sensor availability 
        @Override
        public void run() {
            for (int i = 0; i < numHours; i++) {
                for (int j = 0; j < 60; j++) {
                    sensorsAvailable[ID] = false;
                    sensorReadings[j + (ID * 60)] = (int) (Math.random() * (171)) - 100;
                    sensorsAvailable[ID] = true;
                    while (!sensorsAvailable(ID, sensorsAvailable)) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (ID == 0) {
                    try {
                        mutex.acquire();
                    } catch (Exception e) {
                        System.out.println("Exception caught!");
                    }
                    printTemps(i, sensorReadings);
                    mutex.release();
                }
            }
        }

    }

    // function checks if sensors are available 
    private static boolean sensorsAvailable(int ID, boolean[] sensorsAvailable) {
        for (int i = 0; i < sensorsAvailable.length; i++) {
            if (!sensorsAvailable[i] && ID != i) {
                return false;
            }
        }
        return true;
    }

    // function that prints the largest difference in an 10 minute interval
    private static void largestDifference(int[] sensorReadings) {
        int start = 0;
        int largestDifference = Integer.MIN_VALUE;

        // loop through the arrays to find the largest difference
        for (int i = 0; i < sensorReadings.length - 10; i++) {
            int maximum = Integer.MIN_VALUE;
            int minimum = Integer.MAX_VALUE;
            for (int j = i; j < (i + 10); j++) {
                if (sensorReadings[j] < minimum) {
                    minimum = sensorReadings[j];
                }
                if (sensorReadings[j] > maximum) {
                    maximum = sensorReadings[j];
                }
            }
            if (maximum - minimum > largestDifference) {
                largestDifference = maximum - minimum;
                start = i;
            }
        }

        // print largest difference
        System.out.println("10-minute interval of time when the largest temperature difference was observed: "
                + largestDifference + " starting at minute " + start + " and ending at minute " + (start + 10));
    }

    // find the top 5 highest and print in order
    private static void top5Highest(int[] sensorReadings) {
        List<Integer> temps = new ArrayList<>();

        for (int i = sensorReadings.length - 1; i >= 0; i--) {
            int currTemp = sensorReadings[i];

            if (!temps.contains(currTemp)) {
                temps.add(currTemp);
            }

            if (temps.size() == 5) {
                break;
            }
        }

        System.out.print("Top 5 highest temperatures: ");

        Collections.sort(temps);
        for (int i = 0; i <temps.size(); i++) {
            System.out.print(temps.get(i) + " ");
        }

        System.out.println();
    }

    // find top 5 lowest and print in order
    private static void top5Lowest(int[] sensorReadings) {
        List<Integer> temps = new ArrayList<>();

        for (int i = 0; i < sensorReadings.length; i++) {
            int currTemp = sensorReadings[i];

            if (!temps.contains(currTemp)) {
                temps.add(currTemp);
            }

            if (temps.size() == 5) {
                break;
            }
        }

        System.out.print("Top 5 lowest temperatures: ");
        Collections.sort(temps);
        for (int i = 0; i <temps.size(); i++) {
            System.out.print(temps.get(i) + " ");
        }

        System.out.println();
    }

    private static void printTemps(int hour, int[] sensorReadings) {
        System.out.println("Hour #" + (hour + 1));
        largestDifference(sensorReadings);
        Arrays.sort(sensorReadings);
        top5Highest(sensorReadings);
        top5Lowest(sensorReadings);
        System.out.println();
    }

    // generates random temperatures from -100F to 70F
    public static void measureTemperature(int ID, int[] sensorReadings, boolean[] sensorsAvailable) {
        for (int hour = 0; hour < numHours; hour++) {
            for (int minute = 0; minute < 60; minute++) {
                sensorsAvailable[ID] = false;
                sensorReadings[minute + (ID * 60)] = (int) (Math.random() * (70 - (-100) + 1) + (-100));
                sensorsAvailable[ID] = true;
            }
            if (ID == 0) {
                try {
                    mutex.acquire();
                } catch (Exception e) {
                    System.out.println("Exception caught!");
                }
                printTemps(hour, sensorReadings);
                mutex.release();
            }
        }
    }

    public static void main(String args[]) {
        // generate temperature threads
        int numThreads = 8;
        int[] sensorReadings = new int[numThreads * 60];
        boolean[] sensorsAvailable = new boolean[numThreads];
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new TempThread(i, sensorReadings, sensorsAvailable));
            threads[i].start();
        }

        try {
            long start = System.currentTimeMillis();

            for (int i = 0; i < numThreads; i++) {
                threads[i].join();
            }

            long end = System.currentTimeMillis();
            System.out.println("Finished in " + (end - start) + "ms");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
