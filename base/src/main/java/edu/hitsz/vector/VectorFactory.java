package edu.hitsz.vector;

/**
 * 用于 Vector 子类产生、复制等
 * @author Chiro
 */
public interface VectorFactory<T> {
    T copy();
    T fromVector(VectorType that);
    T getNewInstance();
}
