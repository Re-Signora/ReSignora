package work.chiro.game.x.compatible.colors;

import java.util.Map;

import work.chiro.game.logic.Element;
import work.chiro.game.logic.ElementalReactions;

public class DrawColor {
    public static int green = 0xFF00FF00;
    public static int gray = 0xFF778899;
    public static int darkGray = 0xFF666666;
    public static int red = 0xFFff0000;
    public static int white = 0xFFffffff;
    public static int black = 0xFF000000;
    public static int lightGray = 0xFF666666;

    public static Map<Enum<?>, CoupleColor> enumColors = Map.of(
            // UI
            UIColors.Normal, new CoupleColor(white, darkGray),

            // 元素
            Element.None, new CoupleColor(white, lightGray),
            Element.Geo, new CoupleColor(0xFFffcc66, 0xFF8a5233),
            Element.Pyro, new CoupleColor(0xFFff9b00, 0xFF5e2e0d),
            Element.Hydro, new CoupleColor(0xFF66ccff, 0xFF3f68b3),
            Element.Electro, new CoupleColor(0xFFe19bff, 0xFF853ad6),

            // 元素反应
            ElementalReactions.Wet, new CoupleColor(0xFF66ccff, 0xFF3f68b3)
    );

    public static CoupleColor getEnumColors(Enum<?> e) {
        return enumColors.getOrDefault(e, enumColors.get(UIColors.Normal));
    }
}
