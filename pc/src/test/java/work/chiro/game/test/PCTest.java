package work.chiro.game.test;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import work.chiro.game.compatible.ResourceProviderPC;
import work.chiro.game.logic.attributes.CharacterBasicAttributes;
import work.chiro.game.utils.Utils;
import work.chiro.game.x.compatible.ResourceProvider;
import work.chiro.game.x.ui.XLayout;
import work.chiro.game.x.ui.XLayoutBuilder;
import work.chiro.game.x.ui.XLayoutManager;

public class PCTest {
    PCTest() {
        ResourceProvider.setInstance(new ResourceProviderPC());
    }

    @Test
    void testLayoutBuilder() {
        XLayoutManager layoutManager = XLayoutManager.getInstance();
        XLayoutBuilder builder = new XLayoutBuilder(layoutManager, "main");
        XLayout layout = builder.build();
        Utils.getLogger().info("layout: {}", layout);
    }

    @Test
    void testLoadAttributes() {
        try {
            CharacterBasicAttributes characterBasicAttributes = ResourceProvider.getInstance().getAttributesFromResource("la-signora", CharacterBasicAttributes.class);
            Utils.getLogger().info("characterBasicAttributes: {}", characterBasicAttributes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
