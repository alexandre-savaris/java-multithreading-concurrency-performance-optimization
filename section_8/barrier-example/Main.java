import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) {
        int numberOfThreads = 1;
        List<Thread> threads = new ArrayList<>();

        Barrier barrier = new Barrier(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            threads.add(new Thread(new CoordinatedWorkRunner(barrier)));
        }

        for (Thread thread : threads) {
            thread.start();
        }
    }

    public static class Barrier {
        private final int numberOfWorkers;
        private final Semaphore semaphore = new Semaphore(0);
        private int counter = 0;
        private final Lock lock = new ReentrantLock();

        public Barrier(int numberOfWorkers) {

            this.numberOfWorkers = numberOfWorkers;
        }

        public void waitForOthers() throws InterruptedException {

            this.lock.lock();
            boolean isLastWorker = false;
            try {
                this.counter++;
                if (this.counter == this.numberOfWorkers) {
                    isLastWorker = true;
                }
            } finally {
                this.lock.unlock();
            }

            if (isLastWorker) {
                this.semaphore.release(this.numberOfWorkers - 1);
            } else {
                this.semaphore.acquire();
            }
        }
    }

    public static class CoordinatedWorkRunner implements Runnable {
        private final Barrier barrier;

        public CoordinatedWorkRunner(Barrier barrier) {

            this.barrier = barrier;
        }

        @Override
        public void run() {

            try {
                task();
            } catch (InterruptedException e) {
            }
        }

        private void task() throws InterruptedException {

            System.out.println(Thread.currentThread().getName() + " part 1 of the work is finished.");
            barrier.waitForOthers();
            System.out.println(Thread.currentThread().getName() + " part 2 of the work is finished.");
        }
    }
}
