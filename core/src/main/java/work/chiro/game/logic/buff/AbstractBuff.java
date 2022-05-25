package work.chiro.game.logic.buff;

import work.chiro.game.logic.attributes.BasicThingAttributes;

public abstract class AbstractBuff {
    public void onStart() {
    }

    public double getDuration() {
        return 0;
    }

    public BasicThingAttributes calc(BasicThingAttributes a) {
        return a;
    }

    public void onStop() {
    }
}
