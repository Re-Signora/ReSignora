package work.chiro.game.background;

/**
 * @author Chiro
 */
public class OtherBackgroundFactory<T extends AbstractBackground> extends AbstractBackgroundFactory {
    private final T fakeInstance;

    public OtherBackgroundFactory(T fakeInstance) {
        this.fakeInstance = fakeInstance;
    }

    @Override
    public AbstractBackground create() {
        return fakeInstance.newInstance(initPosition(), createAnimateContainer());
    }
}
