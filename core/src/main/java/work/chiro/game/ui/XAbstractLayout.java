package work.chiro.game.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import work.chiro.game.compatible.XGraphics;

@SuppressWarnings("NullableProblems")
public abstract class XAbstractLayout implements List<XView> {
    private final String name;
    private List<XView> viewList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public XAbstractLayout(String name) {
        this.name = name;
    }

    public XAbstractLayout() {
        this(null);
    }

    /**
     * 替换内容
     * @param that 另一个 XLayout 对象
     * @return this
     */
    public XAbstractLayout replaceLayout(XAbstractLayout that) {
        viewList = that.viewList;
        return this;
    }

    /**
     * 绘制当前容器内所有 UI 物体
     *
     * @param g XGraphics
     * @return this
     */
    public XAbstractLayout show(XGraphics g) {
        viewList.forEach(view -> view.draw(g));
        return this;
    }

    /**
     * 触发一个事件
     *
     * @param event 事件
     * @return this
     */
    public XAbstractLayout trigger(XEvent event) {
        viewList.forEach(view -> view.trigger(event));
        return this;
    }

    /**
     * 添加一个控件
     *
     * @param view 控件
     * @return this
     */
    public XAbstractLayout addView(XView view) {
        viewList.add(view);
        return this;
    }

    /**
     * 清除所有控件
     *
     * @return this
     */
    public XAbstractLayout clearView() {
        viewList.clear();
        return this;
    }

    @Override
    public int size() {
        return viewList.size();
    }

    @Override
    public boolean isEmpty() {
        return viewList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return viewList.contains(o);
    }

    @Override
    public Iterator<XView> iterator() {
        return viewList.iterator();
    }

    @Override
    public Object[] toArray() {
        return viewList.toArray();
    }

    public XView[] toArray(XView[] ts) {
        return viewList.toArray(ts);
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        //noinspection SuspiciousToArrayCall
        return viewList.toArray(ts);
    }

    @Override
    public boolean add(XView view) {
        return viewList.add(view);
    }

    @Override
    public boolean remove(Object o) {
        return viewList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return viewList.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection<? extends XView> collection) {
        return viewList.addAll(collection);
    }

    @Override
    public boolean addAll(int i, Collection<? extends XView> collection) {
        return viewList.addAll(i, collection);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return viewList.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return viewList.retainAll(collection);
    }

    @Override
    public void clear() {
        viewList.clear();
    }

    @Override
    public XView get(int i) {
        return viewList.get(i);
    }

    @Override
    public XView set(int i, XView view) {
        return viewList.set(i, view);
    }

    @Override
    public void add(int i, XView view) {
        viewList.add(i, view);
    }

    @Override
    public XView remove(int i) {
        return viewList.remove(i);
    }

    @Override
    public int indexOf(Object o) {
        return viewList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return viewList.lastIndexOf(o);
    }

    @Override
    public ListIterator<XView> listIterator() {
        return viewList.listIterator();
    }

    @Override
    public ListIterator<XView> listIterator(int i) {
        return viewList.listIterator(i);
    }

    @Override
    public List<XView> subList(int i, int i1) {
        return viewList.subList(i, i1);
    }
}
