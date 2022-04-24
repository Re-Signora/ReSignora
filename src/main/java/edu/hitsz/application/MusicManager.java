package edu.hitsz.application;

/**
 * @author Chiro
 */
public class MusicManager {
    public enum MusicType {
        // 背景音乐
        BGM,
        // Boss 下的背景音乐
        BGM_BOSS,
        // 炸弹爆炸
        BOMB_EXPLOSION,
        // 本机发射
        HERO_SHOOT,
        // 本机被击中
        HERO_HIT,
        // 道具生效
        PROPS,
        // 游戏结束
        GAME_OVER
    }

    static class ErrorMusicTypeException extends Throwable {
    }

    static public String get(MusicType type) {
        switch (type) {
            case BGM:
                return "bgm.wav";
            case BGM_BOSS:
                return "bgm_boss.wav";
            case BOMB_EXPLOSION:
                return "bomb_explosion.wav";
            case HERO_SHOOT:
                return "bullet.wav";
            case HERO_HIT:
                return "bullet_hit.wav";
            case PROPS:
                return "get_supply.wav";
            case GAME_OVER:
                return "game_over.wav";
            default:
                break;
        }
        return null;
    }
}
