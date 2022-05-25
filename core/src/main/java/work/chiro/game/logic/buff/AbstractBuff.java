package work.chiro.game.logic.buff;

import work.chiro.game.logic.attributes.BasicCharacterAttributes;

public abstract class AbstractBuff {
    public void onStart() {
    }

    public double getDuration() {
        return 0;
    }

    public BasicCharacterAttributes calc(BasicCharacterAttributes a) {
        return a;
    }

    public void onStop() {
    }
}
