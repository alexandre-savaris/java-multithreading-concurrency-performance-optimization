import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    public static final int HIGHEST_PRICE = 1000;

    public static void main(String[] args) {
        InventoryDatabase inventoryDatabase = new InventoryDatabase();

        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
            inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
        }

        Thread writer = new Thread(() -> {
            while (true) {
                inventoryDatabase.addItem(random.nextInt(HIGHEST_PRICE));
                inventoryDatabase.removeItem(random.nextInt(HIGHEST_PRICE));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
        });
        writer.setDaemon(true);
        writer.start();

        int numberOfReaderThreads = 7;
        List<Thread> readers = new ArrayList<>();
        for (int readerIndex = 0; readerIndex < numberOfReaderThreads; readerIndex++) {
            Thread reader = new Thread(() -> {
                for (int i = 0; i < 100000; i++) {
                    int upperBoundPrice = random.nextInt(HIGHEST_PRICE);
                    int lowerBoundPrice = upperBoundPrice > 0 ? random.nextInt(upperBoundPrice) : 0;
                    inventoryDatabase.getNumberOfItemsInPriceRange(lowerBoundPrice, upperBoundPrice);
                }
            });
            reader.setDaemon(true);
            readers.add(reader);
        }

        long startReadingTime = System.currentTimeMillis();
        for (Thread reader : readers) {
            reader.start();
        }
        for (Thread reader : readers) {
            try {
                reader.join();
            } catch (InterruptedException e) {
            }
        }
        long endReadingTime = System.currentTimeMillis();
        System.out.printf("Reading took %d ms%n", endReadingTime - startReadingTime);
    }

    public static class InventoryDatabase {
        private TreeMap<Integer, Integer> priceToCountMap = new TreeMap<>();
//        private ReentrantLock lock = new ReentrantLock();
        private ReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        private Lock readLock = reentrantReadWriteLock.readLock();
        private Lock writeLock = reentrantReadWriteLock.writeLock();

        public int getNumberOfItemsInPriceRange(int lowerBound, int upperBound) {

//            this.lock.lock();
            this.readLock.lock();
            try {

                Integer fromKey = this.priceToCountMap.ceilingKey(lowerBound);
                Integer toKey = this.priceToCountMap.floorKey(upperBound);

                if (fromKey == null || toKey == null) {
                    return 0;
                }

                NavigableMap<Integer, Integer> rangeOfPrices
                    = this.priceToCountMap.subMap(fromKey, true, toKey, true);
                int sum = 0;
                for (int numberOfItemsForPrice : rangeOfPrices.values()) {
                    sum += numberOfItemsForPrice;
                }
                return sum;

            } finally {
//                this.lock.unlock();
                this.readLock.unlock();
            }
        }

        public void addItem(int price) {

//            this.lock.lock();
            this.writeLock.lock();
            try {

                Integer numberOfItemsForPrice = this.priceToCountMap.get(price);
                if (numberOfItemsForPrice == null) {
                    this.priceToCountMap.put(price, 1);
                } else {
                    this.priceToCountMap.put(price, numberOfItemsForPrice + 1);
                }

            } finally {
//                this.lock.unlock();
                this.writeLock.unlock();
            }
        }

        public void removeItem(int price) {

//            this.lock.lock();
            this.writeLock.lock();
            try {

                Integer numberOfItemsForPrice = this.priceToCountMap.get(price);
                if (numberOfItemsForPrice == null || numberOfItemsForPrice == 1) {
                    this.priceToCountMap.remove(price);
                } else {
                    this.priceToCountMap.put(price, numberOfItemsForPrice - 1);
                }

            } finally {
//                this.lock.unlock();
                this.writeLock.unlock();
            }
        }
    }
}
