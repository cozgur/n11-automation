package com.company.qa.core.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Thread-safe test context for sharing state between step definitions within a scenario.
 * Provides both static methods (for direct use) and instance methods (for PicoContainer injection).
 */
public class TestContext {

    private static final ThreadLocal<Map<String, Object>> contextThread =
            ThreadLocal.withInitial(HashMap::new);

    // ---- Static methods ----

    public static void put(String key, Object value) {
        contextThread.get().put(key, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        return (T) contextThread.get().get(key);
    }

    public static <T> T get(String key, T defaultValue) {
        T value = get(key);
        return value != null ? value : defaultValue;
    }

    public static boolean containsKey(String key) {
        return contextThread.get().containsKey(key);
    }

    public static void remove(String key) {
        contextThread.get().remove(key);
    }

    public static void clear() {
        contextThread.get().clear();
    }

    public static void reset() {
        contextThread.remove();
    }

    // ---- Instance methods (delegate to ThreadLocal for PicoContainer injection) ----

    public void setValue(String key, Object value) {
        put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key) {
        return get(key);
    }

    public <T> T getValue(String key, T defaultValue) {
        return get(key, defaultValue);
    }

    public boolean hasKey(String key) {
        return containsKey(key);
    }

    public void removeValue(String key) {
        remove(key);
    }

    public void clearAll() {
        clear();
    }

    public void resetContext() {
        reset();
    }
}
