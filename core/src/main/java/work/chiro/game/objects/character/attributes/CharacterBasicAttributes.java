package work.chiro.game.objects.character.attributes;

public class CharacterBasicAttributes extends BasicAttributes {
    /**
     * 统帅
     */
    private int st = 8;
    /**
     * 隐藏伤害倍率
     */
    private int extraDMGRate = 0;

    public int getSt() {
        return st;
    }

    public void setSt(int st) {
        this.st = st;
    }

    public int getExtraDMGRate() {
        return extraDMGRate;
    }

    public void setExtraDMGRate(int extraDMGRate) {
        this.extraDMGRate = extraDMGRate;
    }
}
