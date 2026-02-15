package com.company.qa.core.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Thread-safe test context for sharing state between step definitions within a scenario.
 */
public class TestContext {

    private static final ThreadLocal<Map<String, Object>> contextThread =
            ThreadLocal.withInitial(HashMap::new);

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
}
