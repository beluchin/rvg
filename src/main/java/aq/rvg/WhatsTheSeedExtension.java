package aq.rvg;

import lombok.val;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import static aq.rvg.Operational.setSeed;
import static java.lang.System.currentTimeMillis;

public final class WhatsTheSeedExtension implements TestWatcher, BeforeEachCallback {
    private final ThreadLocal<Long> threadLocalSeed = new ThreadLocal<>();

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        val seed = currentTimeMillis();
        threadLocalSeed.set(seed);
        setSeed(seed);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        System.out.println("threadLocalSeed = " + threadLocalSeed.get());
    }
}
