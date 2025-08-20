import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class Main {

    public static void main(String[] args) {
//        StandardStack<Integer> stack = new StandardStack<>();
        LockFreeStack<Integer> stack = new LockFreeStack<>();
        Random random = new Random();

        for (int i = 0; i < 100000; i++) {
            stack.push(random.nextInt());
        }

        List<Thread> threads = new ArrayList<>();

        int pushingThreads = 2;
        int poppingThreads = 2;

        for (int i = 0; i < pushingThreads; i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    stack.push(random.nextInt());
                }
            });
            thread.setDaemon(true);
            threads.add(thread);
        }

        for (int i = 0; i < poppingThreads; i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    stack.pop();
                }
            });
            thread.setDaemon(true);
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }

        System.out.printf("%,d operations were performed in 10 seconds", stack.getCounter());
    }

    public static class LockFreeStack<T> {
        private AtomicReference<StackNode<T>> head = new AtomicReference<>();
        private AtomicInteger counter = new AtomicInteger(0);

        public void push(T value) {

            StackNode<T> newHead = new StackNode<>(value);
            while (true) {
                StackNode<T> currentHead = this.head.get();
                newHead.next = currentHead;
                if (this.head.compareAndSet(currentHead, newHead)) {
                    break;
                } else {
                    LockSupport.parkNanos(1);
                }
            }
            this.counter.incrementAndGet();
        }

        public T pop() {
            StackNode<T> currentHead = this.head.get();
            StackNode<T> newHead;

            while (currentHead != null) {
                newHead = currentHead.next;
                if (this.head.compareAndSet(currentHead, newHead)) {
                    break;
                } else {
                    LockSupport.parkNanos(1);
                    currentHead = this.head.get();
                }
            }

            this.counter.incrementAndGet();
            return currentHead != null ? currentHead.value : null;
        }

        public int getCounter() {

            return this.counter.get();
        }
    }

    public static class StandardStack<T> {
        private StackNode<T> head;
        private int counter = 0;

        public synchronized void push(T value) {

            StackNode<T> newHead = new StackNode<>(value);
            newHead.next = this.head;
            this.head = newHead;
            this.counter++;
        }

        public synchronized T pop() {

            if (this.head == null) {
                this.counter++;
                return null;
            }
            T value = this.head.value;
            this.head = this.head.next;
            this.counter++;
            return value;
        }

        public int getCounter() {

            return this.counter;
        }
    }

    private static class StackNode<T> {
        public T value;
        public StackNode<T> next;

        public StackNode(T value) {

            this.value = value;
        }
    }
}
