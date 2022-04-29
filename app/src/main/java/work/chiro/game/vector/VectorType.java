package work.chiro.game.vector;

import java.util.LinkedList;
import java.util.List;

/**
 * 多维向量类型
 *
 * @author Chiro
 */
public class VectorType {
    private final int size;
    private final LinkedList<Double> content = new LinkedList<>();

    public VectorType() {
        this(0, new LinkedList<>());
    }

    public VectorType(int size, List<Double> dataInit) {
        this.size = size;
        if (dataInit != null) {
            content.addAll(dataInit);
        }
        assert content.size() <= size;
        while (content.size() < size) {
            content.add(0.0);
        }
    }

    public LinkedList<Double> get() {
        return content;
    }

    public int getSize() {
        return size;
    }

    public void set(List<Double> values) {
        for (int i = 0; i < size; i++) {
            if (i < values.size()) {
                content.set(i, values.get(i));
            } else {
                content.set(i, 0.0);
            }
        }
    }

    public void set(VectorType that) {
        set(that.get());
    }

    public Boolean equals(VectorType that) {
        assert getSize() == that.getSize();
        for (int i = 0; i < size; i++) {
            if (!get().get(i).equals(that.get().get(i))) {
                return false;
            }
        }
        return true;
    }

    enum Operator {
        // 加
        PLUS,
        // 减
        MINUS,
        //乘
        TIMES,
        // 除
        DIVIDE}

    protected VectorType calc(VectorType that, Operator operator) {
        VectorType res = new VectorType(getSize(), null);
        for (int i = 0; i < size; i++) {
            switch (operator) {
                case PLUS:
                    res.get().set(i, get().get(i) + that.get().get(i));
                    break;
                case MINUS:
                    res.get().set(i, get().get(i) - that.get().get(i));
                    break;
                case TIMES:
                    res.get().set(i, get().get(i) * that.get().get(i));
                    break;
                case DIVIDE:
                    res.get().set(i, get().get(i) / that.get().get(i));
                    break;
                default:
                    break;
            }
        }
        return res;
    }

    public VectorType plus(VectorType that) {
        return calc(that, Operator.PLUS);
    }

    public VectorType minus(VectorType that) {
        return calc(that, Operator.MINUS);
    }

    public VectorType times(VectorType that) {
        return calc(that, Operator.TIMES);
    }

    public VectorType divide(VectorType that) {
        return calc(that, Operator.DIVIDE);
    }

    public VectorType plus(double that) {
        return calc(VectorTypeFactory.fromDouble(getSize(), that), Operator.PLUS);
    }

    public VectorType minus(double that) {
        return calc(VectorTypeFactory.fromDouble(getSize(), that), Operator.MINUS);
    }

    public VectorType times(double that) {
        return calc(VectorTypeFactory.fromDouble(getSize(), that), Operator.TIMES);
    }

    public VectorType divide(double that) {
        return calc(VectorTypeFactory.fromDouble(getSize(), that), Operator.DIVIDE);
    }

    public Scale getScale() {
        final Scale sum = new Scale();
        get().forEach(item -> sum.setX(sum.getX() + item * item));
        return sum;
    }
}

