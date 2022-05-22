package work.chiro.game.test;

import org.junit.jupiter.api.Test;

import work.chiro.game.config.RunningConfig;
import work.chiro.game.logger.AbstractLogger;
import work.chiro.game.logger.BasicLogger;
import work.chiro.game.ui.XLayout;
import work.chiro.game.ui.XLayoutBuilder;

public class FunctionTest {
    AbstractLogger logger = new BasicLogger();

    @Test
    void testLogger() {
        logger.info("here.");
        logger.fatal("fatal err");

        logger.log("simple log");

        logger.log("args test: {}", "done?");
        RunningConfig.debug = false;
        logger.debug("disable debug");
    }
}
