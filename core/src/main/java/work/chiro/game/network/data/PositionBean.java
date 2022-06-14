package work.chiro.game.network.data;

import work.chiro.game.vector.Vec2;

public class PositionBean {
    public double x;
    public double y;

    public PositionBean(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public PositionBean(Vec2 pos) {
        this.x = pos.getX();
        this.y = pos.getY();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
