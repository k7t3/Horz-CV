package io.github.k7t3.horzcv.client;

import com.google.gwt.junit.client.GWTTestCase;

public abstract class HorzCVTestBase extends GWTTestCase {

    @FunctionalInterface
    public interface ThrowableRunnable {
        void run() throws Throwable;
    }

    @Override
    public String getModuleName() {
        return "io.github.k7t3.horzcv.HorzCV";
    }

    public <T extends Throwable> void assertThrows(Class<T> clazz, ThrowableRunnable runnable) {
        try {
            runnable.run();
            fail("Expected " + clazz.getName() + " to be thrown");
        } catch (Throwable t) {
            if (!clazz.equals(t.getClass())) {
                fail("Expected " + clazz.getName() + " to be thrown, but " + t.getClass().getName() + " was thrown");
            }
        }
    }

}
