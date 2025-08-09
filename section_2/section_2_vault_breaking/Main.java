import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        int MAX_SECRET = 10000;

        Vault vault = new Vault(new Random().nextInt(MAX_SECRET));

        List<Thread> threads = new ArrayList<>();
        threads.add(new AscendingRobber(vault, 9999, "Berlin"));
        threads.add(new DescendingRobber(vault, 9999, "Rio"));
        threads.add(new Police());

        for (Thread thread : threads) {
            thread.start();
        }
    }
}
