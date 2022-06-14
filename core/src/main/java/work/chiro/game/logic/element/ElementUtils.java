package work.chiro.game.logic.element;

import java.util.Map;

/**
 * 用于元素反应处理的静态类
 */
public class ElementUtils {

    private static final Map<Element, String> elementSting = Map.of(
            // 元素
            Element.None, "无元素",
            Element.Geo, "岩元素",
            Element.Pyro, "火元素",
            Element.Hydro, "水元素",
            Element.Electro, "雷元素",
            Element.Anemo, "风元素",
            Element.Dendro, "草元素",
            Element.Cryo, "冰元素"
    );
    private static final Map<ElementalReactions, String> elementalReactionsSting = Map.of(
            // 元素反应
            ElementalReactions.Wet, "潮湿",
            ElementalReactions.ElectroCharged, "感电",
            ElementalReactions.Overloaded, "超载",
            ElementalReactions.Frozen, "冻结",
            ElementalReactions.Crystallize, "结晶"
    );

    /**
     * 元素、元素反应等枚举转换成字符串
     * @param elementType 枚举
     * @return 字符串 | "ErrorType"
     */
    public static String elementToString(Enum<?> elementType) {
        if (elementType instanceof Element)
            return elementSting.getOrDefault(elementType, "ErrorType");
        else if (elementType instanceof ElementalReactions)
            return elementalReactionsSting.getOrDefault(elementType, "ErrorType");
        else return "ErrorType";
    }
}
