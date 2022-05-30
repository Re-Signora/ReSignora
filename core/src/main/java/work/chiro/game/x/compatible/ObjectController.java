package work.chiro.game.x.compatible;

import work.chiro.game.objects.AbstractObject;

public abstract class ObjectController {
    abstract public boolean isShootPressed();

    private AbstractObject<?> target = null;

    public void setTarget(AbstractObject<?> target) {
        this.target = target;
    }

    public AbstractObject<?> getTarget() {
        return target;
    }
}
