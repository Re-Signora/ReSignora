package work.chiro.game.config;

/**
 * @author Chiro
 */
public class ConfigFactory {
    private final Difficulty difficulty;

    public ConfigFactory(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public static class ConfigArgumentErrorException extends Exception {
    }

    public AbstractConfig create() {
        switch (difficulty) {
            case Easy:
                return new EasyConfig();
            default:
                try {
                    throw new ConfigArgumentErrorException();
                } catch (ConfigArgumentErrorException e) {
                    e.printStackTrace();
                    return new EasyConfig();
                }
        }
    }

    public static void main(String[] args) {
        new ConfigFactory(Difficulty.Easy);
    }
}
