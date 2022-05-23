package work.chiro.game.ui;

import java.io.IOException;

import work.chiro.game.compatible.ResourceProvider;
import work.chiro.game.vector.Vec2;

public class XLayoutBuilder {
    private final String layoutName;

    public XLayoutBuilder(String layoutName) {
        this.layoutName = layoutName;
    }

    public XLayout build(XLayoutBean layoutBean) {
        XLayout layout = new XLayout(layoutName)
                .setBackground(layoutBean.background);
        layoutBean.views.forEach(viewBean -> layout.addView(new XViewBuilder(
                        XView.stringToType(viewBean.type),
                        new Vec2(viewBean.position.get(0), viewBean.position.get(1))
                ).build()
                        .setId(viewBean.id)
                        .setText(viewBean.text)
                        .setImageResource(viewBean.image)
        ));
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
