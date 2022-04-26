package work.chiro.game.background;

/**
 * @author Chiro
 */
public class OtherBackgroundFactory<T extends BasicBackground> extends BasicBackgroundFactory {
    private final T fakeInstance;

    public OtherBackgroundFactory(T fakeInstance) {
        this.fakeInstance = fakeInstance;
    }

    @Override
    public AbstractBackground create() {
        return fakeInstance.newInstance(initPosition(), createAnimateContainer());
    }
}
