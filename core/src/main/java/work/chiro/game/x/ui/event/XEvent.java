package work.chiro.game.x.ui.event;

import work.chiro.game.vector.Vec2;

public class XEvent {
    private final XEventType eventType;
    private Vec2 position;

    public XEvent(XEventType eventType) {
        this.eventType = eventType;
    }

    public XEvent setPosition(Vec2 position) {
        this.position = position;
        return this;
    }

    public Vec2 getPosition() {
        return position;
    }

    public XEventType getEventType() {
        return eventType;
    }
}
