public class DescendingRobber extends Robber {

    public DescendingRobber(Vault vault, int maxSecret, String name) {
        super(vault, maxSecret, name);
    }

    @Override
    public void run() {

        for (int i = maxSecret; i >= 1; i--) {
            if (vault.guessSecret(i)) {
                System.out.println("DescendingRobber " + this.getName()
                    + " entered the vault using the secret " + i + " !");
                System.exit(0);
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
