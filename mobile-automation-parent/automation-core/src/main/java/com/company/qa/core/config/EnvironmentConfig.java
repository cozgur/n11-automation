package com.company.qa.core.config;

import java.util.Map;

/**
 * Singleton environment configuration.
 * Merges config/default.yaml with config/{env}.yaml (env from -Denv system property, defaults to "dev").
 * Thread-safe lazy initialization.
 */
public class EnvironmentConfig {

    private static volatile EnvironmentConfig instance;

    private final Map<String, Object> mergedConfig;

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

    public String getAppiumUrl() {
        return getStringValue("appium.url", "http://127.0.0.1:4723");
    }

    public int getImplicitTimeout() {
        return getIntValue("timeout.implicit", 10);
    }

    public int getExplicitTimeout() {
        return getIntValue("timeout.explicit", 15);
    }

    public int getRetryMax() {
        return getIntValue("retry.max", 2);
    }

    public long getRetryDelayMs() {
        return getLongValue("retry.delayMs", 1000L);
    }

    public boolean isScreenshotOnFailure() {
        return getBooleanValue("screenshot.onFailure", true);
    }

    public String get(String key) {
        return ConfigReader.getString(mergedConfig, key);
    }

    public String get(String key, String defaultValue) {
        return ConfigReader.getString(mergedConfig, key, defaultValue);
    }

    private String getStringValue(String key, String defaultValue) {
        String val = ConfigReader.getString(mergedConfig, key);
        return val != null ? val : defaultValue;
    }

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

    private boolean getBooleanValue(String key, boolean defaultValue) {
        String val = ConfigReader.getString(mergedConfig, key);
        if (val != null) {
            return Boolean.parseBoolean(val);
        }
        return defaultValue;
    }

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
