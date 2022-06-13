package work.chiro.game.logic.attributes.popup;

import work.chiro.game.logic.element.Element;
import work.chiro.game.logic.attributes.BasicThingAttributes;

public class PopupAttributes extends BasicThingAttributes {
    public Enum<?> type = Element.None;

    public Enum<?> getType() {
        return type;
    }

    public void setType(Enum<?> type) {
        this.type = type;
    }
}
