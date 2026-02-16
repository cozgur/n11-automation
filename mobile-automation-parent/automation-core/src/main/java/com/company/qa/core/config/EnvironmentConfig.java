package com.company.qa.core.config;

import java.util.Map;

/**
 * Singleton environment configuration providing typed access to framework settings.
 *
 * <p>Merges {@code config/default.yaml} with {@code config/{env}.yaml}, where
 * {@code env} is resolved from the {@code -Denv} system property (defaults to
 * {@code "dev"}). Environment-specific values override defaults.</p>
 *
 * <p>Thread-safe lazy initialization is achieved via the double-checked locking
 * pattern with a {@code volatile} instance field.</p>
 *
 * <p><b>Usage:</b></p>
 * <pre>{@code
 * EnvironmentConfig config = EnvironmentConfig.getInstance();
 * String appiumUrl = config.getAppiumUrl();
 * int timeout = config.getExplicitTimeout();
 * }</pre>
 */
public class EnvironmentConfig {

    private static volatile EnvironmentConfig instance;

    private final Map<String, Object> mergedConfig;

    /**
     * Private constructor that loads and merges configuration files.
     */
    private EnvironmentConfig() {
        String env = System.getProperty("env", "dev");

        // Load default config
        Map<String, Object> defaultConfig;
        try {
            defaultConfig = ConfigReader.load("config/default.yaml");
        } catch (RuntimeException e) {
            defaultConfig = new java.util.HashMap<>();
        }

        // Load environment-specific config
        Map<String, Object> envConfig;
        try {
            envConfig = ConfigReader.load("config/" + env + ".yaml");
        } catch (RuntimeException e) {
            envConfig = new java.util.HashMap<>();
        }

        // Merge: env overrides default
        mergedConfig = new java.util.HashMap<>(defaultConfig);
        deepMerge(mergedConfig, envConfig);
    }

    /**
     * Returns the singleton instance, creating it on first access.
     *
     * @return the shared {@link EnvironmentConfig} instance
     */
    public static EnvironmentConfig getInstance() {
        if (instance == null) {
            synchronized (EnvironmentConfig.class) {
                if (instance == null) {
                    instance = new EnvironmentConfig();
                }
            }
        }
        return instance;
    }

    /**
     * Returns the Appium server URL.
     *
     * @return the configured Appium URL, or {@code "http://127.0.0.1:4723"} if not set
     */
    public String getAppiumUrl() {
        return getStringValue("appium.url", "http://127.0.0.1:4723");
    }

    /**
     * Returns the implicit wait timeout in seconds.
     *
     * @return the implicit timeout, or {@code 10} if not configured
     */
    public int getImplicitTimeout() {
        return getIntValue("timeout.implicit", 10);
    }

    /**
     * Returns the explicit wait timeout in seconds.
     *
     * @return the explicit timeout, or {@code 15} if not configured
     */
    public int getExplicitTimeout() {
        return getIntValue("timeout.explicit", 15);
    }

    /**
     * Returns the maximum number of test retries on failure.
     *
     * @return the max retry count, or {@code 2} if not configured
     */
    public int getRetryMax() {
        return getIntValue("retry.max", 2);
    }

    /**
     * Returns the delay between retries in milliseconds.
     *
     * @return the retry delay in ms, or {@code 1000L} if not configured
     */
    public long getRetryDelayMs() {
        return getLongValue("retry.delayMs", 1000L);
    }

    /**
     * Returns whether screenshots should be captured on test failure.
     *
     * @return {@code true} if screenshot-on-failure is enabled (default),
     *         {@code false} otherwise
     */
    public boolean isScreenshotOnFailure() {
        return getBooleanValue("screenshot.onFailure", true);
    }

    /**
     * Retrieves a raw string value from the merged configuration.
     *
     * @param key the dot-separated configuration key
     * @return the value as a string, or {@code null} if not found
     */
    public String get(String key) {
        return ConfigReader.getString(mergedConfig, key);
    }

    /**
     * Retrieves a string value from the merged configuration with a fallback default.
     *
     * @param key          the dot-separated configuration key
     * @param defaultValue the value to return if the key is not found
     * @return the value as a string, or {@code defaultValue} if not found
     */
    public String get(String key, String defaultValue) {
        return ConfigReader.getString(mergedConfig, key, defaultValue);
    }

    /**
     * Retrieves a string value with a fallback default.
     *
     * @param key          the configuration key
     * @param defaultValue the fallback value
     * @return the resolved value or the default
     */
    private String getStringValue(String key, String defaultValue) {
        String val = ConfigReader.getString(mergedConfig, key);
        return val != null ? val : defaultValue;
    }

    /**
     * Retrieves an integer value with a fallback default.
     *
     * @param key          the configuration key
     * @param defaultValue the fallback value
     * @return the resolved integer or the default
     */
    private int getIntValue(String key, int defaultValue) {
        String val = ConfigReader.getString(mergedConfig, key);
        if (val != null) {
            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Retrieves a long value with a fallback default.
     *
     * @param key          the configuration key
     * @param defaultValue the fallback value
     * @return the resolved long or the default
     */
    private long getLongValue(String key, long defaultValue) {
        String val = ConfigReader.getString(mergedConfig, key);
        if (val != null) {
            try {
                return Long.parseLong(val);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Retrieves a boolean value with a fallback default.
     *
     * @param key          the configuration key
     * @param defaultValue the fallback value
     * @return the resolved boolean or the default
     */
    private boolean getBooleanValue(String key, boolean defaultValue) {
        String val = ConfigReader.getString(mergedConfig, key);
        if (val != null) {
            return Boolean.parseBoolean(val);
        }
        return defaultValue;
    }

    /**
     * Recursively merges the override map into the base map. Nested maps are
     * merged rather than replaced.
     *
     * @param base     the base configuration map (modified in place)
     * @param override the override map whose values take precedence
     */
    @SuppressWarnings("unchecked")
    private void deepMerge(Map<String, Object> base, Map<String, Object> override) {
        for (Map.Entry<String, Object> entry : override.entrySet()) {
            String key = entry.getKey();
            Object overrideValue = entry.getValue();
            Object baseValue = base.get(key);

            if (baseValue instanceof Map && overrideValue instanceof Map) {
                deepMerge((Map<String, Object>) baseValue, (Map<String, Object>) overrideValue);
            } else {
                base.put(key, overrideValue);
            }
        }
    }
}
