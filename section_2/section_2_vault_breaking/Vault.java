public class Vault {
    private int secret;

    public Vault(int secret) {

        this.secret = secret;
    }

    public boolean guessSecret(int secret) {

        return (secret == this.secret);
    }
}
