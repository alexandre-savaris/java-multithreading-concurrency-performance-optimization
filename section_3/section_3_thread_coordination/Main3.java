import java.math.BigInteger;

public class Main3 {

    public static void main(String[] args) throws InterruptedException {

        Thread t = new Thread(new LongComputationTask(new BigInteger("2000"), new BigInteger("2000000000000")));

        // Preventing the thread to block our app from exiting.
        t.setDaemon(true);
        t.start();
        Thread.sleep(100);
        t.interrupt();
    }

    private static class LongComputationTask implements Runnable {
        private BigInteger base;
        private BigInteger power;

        public LongComputationTask(BigInteger base, BigInteger power) {

            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {

            System.out.println(base + "^" + power + " = " + pow(base, power));

        }

        private BigInteger pow(BigInteger base, BigInteger power) {
            BigInteger result = BigInteger.ONE;

            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("The thread was interrupted!");
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }

            return result;
        }
    }
}
