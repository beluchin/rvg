package aq.rvg.junit4;

import org.junit.rules.ExternalResource;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import static aq.rvg.Operational.getSeed;
import static aq.rvg.Operational.setSeed;
import static java.lang.System.currentTimeMillis;

public final class WhatsTheSeedRule {
    public static TestRule whatsTheSeedRule() {
        return RuleChain.outerRule(new ExternalResource() {
                    @Override
                    protected void before() {
                        setSeed(currentTimeMillis());
                    }
                })
                .around(new TestWatcher() {
                    @Override
                    protected void failed(Throwable e, Description description) {
                        System.out.println("seed = " + getSeed());
                    }
                });
    }
}
