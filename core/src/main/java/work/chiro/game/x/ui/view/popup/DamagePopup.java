package work.chiro.game.x.ui.view.popup;

import work.chiro.game.vector.Vec2;

public class DamagePopup extends BasicPopup {
    public DamagePopup(Vec2 posInit, Enum<?> type, int damage,Vec2 size) {
        super(posInit, type, "" + damage,size);
    }
}
