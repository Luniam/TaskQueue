public class TaskManager {

    BlockingTaskQueue<Runnable> taskQueue;

    public TaskManager() {
        taskQueue = new BlockingTaskQueue<>(5);
    }

    public void submitTask(Runnable task) throws InterruptedException {
        taskQueue.enqueue(task);
    }
}
