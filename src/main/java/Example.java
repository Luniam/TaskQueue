public class Example {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.startTaskRunningDaemon();

        Thread taskProducer1 = new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep((long) (Math.random() * 10000));
                    taskManager.submitTask(new SampleTask());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        taskProducer1.start();

        Thread taskProducer2 = new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep((long) (Math.random() * 1000));
                    taskManager.submitTask(new SampleTask());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        taskProducer2.start();
    }
}
