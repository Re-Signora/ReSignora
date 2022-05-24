package work.chiro.game.x.ui;

public class XEvent {
    private final XEventType eventType;

    public XEvent(XEventType eventType) {
        this.eventType = eventType;
    }

    public XEventType getEventType() {
        return eventType;
    }
}
