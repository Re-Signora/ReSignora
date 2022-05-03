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
            case Medium:
                return new MediumConfig();
            case Hard:
                return new HardConfig();
            default:
                try {
                    throw new ConfigArgumentErrorException();
                } catch (ConfigArgumentErrorException e) {
                    e.printStackTrace();
                    return new EasyConfig();
                }
        }
    }
}
