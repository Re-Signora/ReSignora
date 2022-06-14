package work.chiro.game.network.data;

public class DataBean {
    public String command = "Error";
    public PositionBean position;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public PositionBean getPosition() {
        return position;
    }

    public void setPosition(PositionBean position) {
        this.position = position;
    }
}
