package aq.rvg.junit5;

import aq.rvg.Operational;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import static aq.rvg.Operational.getSeed;
import static aq.rvg.Operational.setSeed;
import static java.lang.System.currentTimeMillis;

public final class WhatsTheSeedExtension implements TestWatcher, BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        Operational.setSeed(currentTimeMillis());
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        System.out.println("seed = " + Operational.getSeed());
    }
}
