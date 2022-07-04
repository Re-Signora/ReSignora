package work.chiro.game.x.ui.layout;

import java.io.IOException;

import work.chiro.game.x.compatible.ResourceProvider;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.ui.builder.XViewBuilder;
import work.chiro.game.x.ui.view.XView;

public class XLayoutBuilder {
    private final String layoutName;
    private final XLayoutManager layoutManager;

    public XLayoutBuilder(XLayoutManager layoutManager, String layoutName) {
        this.layoutManager = layoutManager;
        this.layoutName = layoutName;
//        给出这这东西的名字？？？？然后给到最下面的build？
    }

    public XLayout build(XLayoutBean layoutBean) {
//        viewBean是个什么玩意儿啊？？？？，，是XLayoutBean中的嘛？？？？有些小小的疑惑emmm
//        场景？那角色应该是在哪里呢？
        XLayout layout = new XLayout(layoutName)
                .setBackground(layoutBean.background);
        layoutBean.views.forEach(viewBean -> {
            XView cachedView = layoutManager.getViewByID(viewBean.id);
            if (cachedView != null) {
                // 不重新产生重复 ID 的 View
                layout.addView(cachedView);
                return;
            }
            XView view = new XViewBuilder(
//                    原来是从ViewBean里面来的？？？？但是为什么套盾套那么多层啊，啊啊啊啊啊啊
                    XView.stringToType(viewBean.type),
                    viewBean.position != null ? new Vec2(viewBean.position.get(0), viewBean.position.get(1)) : null
            ).build()
                    .setId(viewBean.id)
                    .setText(viewBean.text)
                    .setFont(viewBean.font)
                    .setImageResource(viewBean.image);
//            这是个什么用法？？？？
            if (viewBean.size != null) {
                view.setSize(new Vec2(viewBean.size.get(0), viewBean.size.get(1)));
            }
            layoutManager.putViewByID(viewBean.id, view);
            layout.addView(view);
        });
        return layout;
    }

    public XLayout build() {
        try {
            XLayoutBean layoutBean = ResourceProvider.getInstance().getLayoutBeanFromResource(layoutName);
//            所以最后对每个角色进行控制的是layoutBean？？？？
            return build(layoutBean);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
