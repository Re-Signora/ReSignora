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
    }

    public XLayout build(XLayoutBean layoutBean) {
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
                    XView.stringToType(viewBean.type),
                    viewBean.position != null ? new Vec2(viewBean.position.get(0), viewBean.position.get(1)) : null
            ).build()
                    .setId(viewBean.id)
                    .setText(viewBean.text)
                    .setFont(viewBean.font)
                    .setImageResource(viewBean.image);
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
            return build(layoutBean);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
