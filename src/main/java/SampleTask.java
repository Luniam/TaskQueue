

public class SampleTask implements Runnable {

    public void run() {
        System.out.println(Thread.currentThread().getName() + " executing at " + System.currentTimeMillis()/1000);
    }
}
