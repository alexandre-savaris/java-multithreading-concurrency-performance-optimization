public class Police extends Thread {

    @Override
    public void run() {

        for (int i = 1; i <= 10; i++) {
            System.out.println("The police has still not arrived at time " + i + " !");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("The police has finally arrived!");
        System.exit(0);
    }
}
