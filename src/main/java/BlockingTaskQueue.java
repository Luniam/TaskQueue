/**
 * A simple blocking FIFO queue implementation to hold the tasks
 * @param <T>
 */
public class BlockingTaskQueue<T extends Runnable> {

    T[] array;
    int capacity;
    int size = 0, head = 0, tail = 0;

    /**
     * Initialize the queue with a default capacity
     * @param capacity
     */
    @SuppressWarnings("unchecked")
    public BlockingTaskQueue(int capacity) {
        this.capacity = capacity;
        array = (T[]) new Runnable[capacity];
    }

    /**
     * Standard queue enqueue method. 
     * If the queue is full the thread will give up the monitor and wait for it to have space again
     * @param item
     * @throws InterruptedException
     */
    public synchronized void enqueue(T item) throws InterruptedException {
        while (size == capacity) wait();
        if(head == capacity) head = 0;
        array[head++] = item;
        size++;
        notifyAll();
    }

    /**
     * Standard FIFO deque
     * If the queue is empty the thread will give up the monitor and wait to get at least one element
     * @return T
     * @throws InterruptedException
     */
    public synchronized T dequeue() throws InterruptedException {
        while(size == 0) wait();
        if(tail == capacity) tail = 0;
        T item = array[tail];
        array[tail] = null;
        tail++;
        size--;
        notifyAll();
        return item;
    }
}