package work.chiro.game.test;

import org.junit.jupiter.api.Test;

import work.chiro.game.config.RunningConfig;
import work.chiro.game.x.logger.AbstractLogger;
import work.chiro.game.x.logger.BasicLogger;

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

    interface TestInterface {
        void fun();
    }

    static class HasTestInterface implements TestInterface {
        @Override
        public void fun() {
            System.out.println("fun()");
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void testInterfaceInstance() {
        Object o = new HasTestInterface();
        System.out.println("o instanceof HasTestInterface = " + (o instanceof HasTestInterface));
        System.out.println("o instanceof TestInterface = " + (o instanceof TestInterface));
    }
}
