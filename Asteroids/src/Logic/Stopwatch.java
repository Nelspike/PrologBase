package Logic;

public class Stopwatch {

    private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;

    /**
     * Default constructor of Stopwatch.
     */
    public Stopwatch()
    {
    	
    }
    
    /**
     * Starts the stopwatch.
     */
    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }

    /**
     * Stops the stopwatch.
     */
    public void stop() {
        this.stopTime = System.currentTimeMillis();
        this.running = false;
    }

    
    /**
     * Gets the elapsed time of the stopwatch, in milliseconds.
     * @return A Long data type.
     */
    public long getElapsedTime() {
        long elapsed;
        if (running) {
             elapsed = (System.currentTimeMillis() - startTime);
        }
        else {
            elapsed = (stopTime - startTime);
        }
        return elapsed;
    }
    
    
    /**
     * Gets the elapsed time of the stopwatch, in seconds.
     * @return A Long data type.
     */
    public long getElapsedTimeSecs() {
        long elapsed;
        if (running) {
            elapsed = ((System.currentTimeMillis() - startTime) / 1000);
        }
        else {
            elapsed = ((stopTime - startTime) / 1000);
        }
        return elapsed;
    }
}
