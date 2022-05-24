package work.chiro.game.objects.background;

/**
 * @author Chiro
 */
public class BasicBackgroundFactory extends AbstractBackgroundFactory {
    private final String backgroundName;

    public BasicBackgroundFactory(String backgroundName) {
        this.backgroundName = backgroundName;
    }

    @Override
    public AbstractBackground create() {
        return new BasicBackground(backgroundName);
    }
}
