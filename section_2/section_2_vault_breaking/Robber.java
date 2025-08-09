public abstract class Robber extends Thread {
    protected Vault vault;
    protected int maxSecret;

    public Robber(Vault vault, int maxSecret, String name) {

        this.vault = vault;
        this.maxSecret = maxSecret;
        this.setName(name);
        this.setPriority(Thread.MAX_PRIORITY);
    }
}
