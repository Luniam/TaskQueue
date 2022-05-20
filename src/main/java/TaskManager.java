import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class TaskManager {

    private final int QUEUE_CAPACITY = 10;
    private final int THREADPOOL_SIZE = 10;

    BlockingTaskQueue<Runnable> taskQueue;
    ThreadPoolExecutor taskExecutorPool;

    /**
     * Initialize the task manager with a blocking queue to hold the tasks
     * And a pool of threads to execute them
     */
    public TaskManager() {
        taskQueue = new BlockingTaskQueue<>(5);
        taskExecutorPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREADPOOL_SIZE);
    }

    public void submitTask(Runnable task) throws InterruptedException {
        taskQueue.enqueue(task);
    }

    /**
     * Run the daemon thread to check for incoming tasks in the queue
     */
    public void startTaskRunningDaemon() {
        Thread taskDaemon = new Thread(() -> {
            try {
                while (true) {
                    Runnable task = taskQueue.dequeue();
                    taskExecutorPool.submit(task);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        taskDaemon.setDaemon(true);
        taskDaemon.start();
    }
}
