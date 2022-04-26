package work.chiro.game.vector;

import java.util.Collections;

/**
 * 生成 VectorType 的工厂类
 *
 * @author Chiro
 */
public class VectorTypeFactory {
    static VectorType fromDouble(int size, double value) {
        return new VectorType(size, Collections.nCopies(size, value));
    }
}
