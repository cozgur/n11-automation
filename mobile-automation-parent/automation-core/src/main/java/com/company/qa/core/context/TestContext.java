package com.company.qa.core.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Thread-safe test context for sharing state between step definitions within a scenario.
 *
 * <p>Uses {@link ThreadLocal} storage so that each test thread maintains its own
 * isolated key-value map. Provides both static methods (for direct use) and
 * instance methods (for dependency injection frameworks like PicoContainer).</p>
 *
 * <p><b>Usage (static):</b></p>
 * <pre>{@code
 * TestContext.put("userId", 42);
 * int userId = TestContext.get("userId");
 * }</pre>
 *
 * <p><b>Usage (injected instance):</b></p>
 * <pre>{@code
 * // PicoContainer injects TestContext into step definition classes
 * testContext.setValue("userId", 42);
 * int userId = testContext.getValue("userId");
 * }</pre>
 */
public class TestContext {

    private static final ThreadLocal<Map<String, Object>> contextThread =
            ThreadLocal.withInitial(HashMap::new);

    // ---- Static methods ----

    /**
     * Stores a value in the current thread's context.
     *
     * @param key   the key to associate the value with
     * @param value the value to store
     */
    public static void put(String key, Object value) {
        contextThread.get().put(key, value);
    }

    /**
     * Retrieves a value from the current thread's context.
     *
     * @param key  the key whose associated value is to be returned
     * @param <T>  the expected value type
     * @return the value mapped to the key, or {@code null} if no mapping exists
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        return (T) contextThread.get().get(key);
    }

    /**
     * Retrieves a value from the current thread's context, returning a default
     * if no mapping exists.
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the value to return if the key has no mapping
     * @param <T>          the expected value type
     * @return the mapped value, or {@code defaultValue} if the key is not present
     */
    public static <T> T get(String key, T defaultValue) {
        T value = get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Checks whether the current thread's context contains a mapping for the given key.
     *
     * @param key the key to check
     * @return {@code true} if the key exists in the context, {@code false} otherwise
     */
    public static boolean containsKey(String key) {
        return contextThread.get().containsKey(key);
    }

    /**
     * Removes the mapping for the specified key from the current thread's context.
     *
     * @param key the key to remove
     */
    public static void remove(String key) {
        contextThread.get().remove(key);
    }

    /**
     * Clears all entries from the current thread's context map.
     *
     * <p>The underlying map is retained (emptied, not removed).</p>
     */
    public static void clear() {
        contextThread.get().clear();
    }

    /**
     * Fully removes the current thread's context map from the {@link ThreadLocal}.
     *
     * <p>A subsequent call to any accessor will create a fresh empty map.</p>
     */
    public static void reset() {
        contextThread.remove();
    }

    // ---- Instance methods (delegate to ThreadLocal for PicoContainer injection) ----

    /**
     * Stores a value in the current thread's context (instance method).
     *
     * @param key   the key to associate the value with
     * @param value the value to store
     */
    public void setValue(String key, Object value) {
        put(key, value);
    }

    /**
     * Retrieves a value from the current thread's context (instance method).
     *
     * @param key  the key whose associated value is to be returned
     * @param <T>  the expected value type
     * @return the value mapped to the key, or {@code null} if no mapping exists
     */
    @SuppressWarnings("unchecked")
    public <T> T getValue(String key) {
        return get(key);
    }

    /**
     * Retrieves a value from the context with a default fallback (instance method).
     *
     * @param key          the key whose associated value is to be returned
     * @param defaultValue the value to return if the key has no mapping
     * @param <T>          the expected value type
     * @return the mapped value, or {@code defaultValue} if the key is not present
     */
    public <T> T getValue(String key, T defaultValue) {
        return get(key, defaultValue);
    }

    /**
     * Checks whether the context contains the given key (instance method).
     *
     * @param key the key to check
     * @return {@code true} if the key exists, {@code false} otherwise
     */
    public boolean hasKey(String key) {
        return containsKey(key);
    }

    /**
     * Removes the mapping for the given key (instance method).
     *
     * @param key the key to remove
     */
    public void removeValue(String key) {
        remove(key);
    }

    /**
     * Clears all entries from the context (instance method).
     */
    public void clearAll() {
        clear();
    }

    /**
     * Fully resets the context for the current thread (instance method).
     */
    public void resetContext() {
        reset();
    }
}
