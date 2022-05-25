package work.chiro.game.animate.action;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.objects.character.AbstractThings;

/**
 * 相比 Animate 增加更多功能：<br/>
 * 1. 图片轮播      <br/>
 * 2. 角色状态机     <br/>
 *
 * @author Chiro
 */
public class AbstractAction extends AnimateContainer {
    private final AbstractThings character;

    public AbstractAction(AbstractThings character) {
        this.character = character;
    }

    public AbstractThings getCharacter() {
        return character;
    }
}
