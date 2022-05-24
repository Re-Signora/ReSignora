package work.chiro.game.test;

import org.junit.jupiter.api.Test;

import work.chiro.game.x.compatible.ResourceProvider;
import work.chiro.game.compatible.ResourceProviderPC;
import work.chiro.game.x.ui.XLayout;
import work.chiro.game.x.ui.XLayoutBuilder;
import work.chiro.game.utils.Utils;

public class PCTest {
    PCTest() {
        ResourceProvider.setInstance(new ResourceProviderPC());
    }

    @Test
    void testLayoutBuilder() {
        XLayoutBuilder builder = new XLayoutBuilder("main");
        XLayout layout = builder.build();
        Utils.getLogger().info("layout: {}", layout);
    }
}
