import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;

public class TaskManager {

    private final int QUEUE_CAPACITY = 10;
    private final int THREADPOOL_SIZE = 10;

    BlockingTaskQueue<Runnable> taskQueue;
    ThreadPoolExecutor taskExecutorPool;

    private static final ReentrantLock lock = new ReentrantLock();
    private static TaskManager taskManager;

    /**
     * Initialize the task manager with a blocking queue to hold the tasks
     * And a pool of threads to execute them
     * We want to make this class a singleton so the constructor is private
     */
    private TaskManager() {
        taskQueue = new BlockingTaskQueue<>(5);
        taskExecutorPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREADPOOL_SIZE);
    }

    /**
     * This method will return the same instance of the TaskManager
     * We need to lock the creation since multiple threads might access the creation method at the same time
     * @return TaskManager
     */
    public static TaskManager getTaskManager() {
        lock.lock();
        if(taskManager == null) taskManager = new TaskManager();
        lock.unlock();
        return taskManager;
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
                    System.out.println("Checking for tasks");
                    Runnable task = taskQueue.dequeue();
                    taskExecutorPool.submit(task);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        //Making this a daemon thread will kill the thread when there are no producers
        //taskDaemon.setDaemon(true);
        taskDaemon.start();
    }
}
