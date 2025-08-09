public class AscendingRobber extends Robber {

    public AscendingRobber(Vault vault, int maxSecret, String name) {
        super(vault, maxSecret, name);
    }

    @Override
    public void run() {

        for (int i = 1; i <= maxSecret; i++) {
            if (vault.guessSecret(i)) {
                System.out.println("AscendingRobber " + this.getName()
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
