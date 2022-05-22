package work.chiro.game.ui;

import java.io.IOException;
import java.util.stream.Collectors;

import work.chiro.game.compatible.ResourceProvider;
import work.chiro.game.vector.Vec2;

public class XLayoutBuilder {
    private final String layoutName;

    public XLayoutBuilder(String layoutName) {
        this.layoutName = layoutName;
    }

    public XLayout build(XLayoutBean layoutBean) {
        XLayout layout = new XLayout();
        layoutBean.views.forEach(viewBean -> layout.addView(new XView(
                new Vec2(viewBean.position.get(0), viewBean.position.get(1))
        ) {
        }.setId(viewBean.id)
                .setBox(viewBean.box.stream().map(item -> new Vec2(item.get(0), item.get(1))).collect(Collectors.toList()))
                .setText(viewBean.text)
                .setImageResource(viewBean.image)));
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
