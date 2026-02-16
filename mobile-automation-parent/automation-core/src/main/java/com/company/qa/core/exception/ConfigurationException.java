package com.company.qa.core.exception;

/**
 * Exception thrown when a configuration error is encountered.
 *
 * <p>Typical scenarios include:</p>
 * <ul>
 *   <li>Missing or unreadable YAML/JSON configuration files</li>
 *   <li>Malformed configuration values that cannot be parsed</li>
 *   <li>Required configuration keys that are absent</li>
 * </ul>
 *
 * @see FrameworkException
 */
public class ConfigurationException extends FrameworkException {

    /**
     * Creates a new configuration exception with the specified detail message.
     *
     * @param message the detail message describing the configuration error
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * Creates a new configuration exception with the specified detail message and cause.
     *
     * @param message the detail message describing the configuration error
     * @param cause   the underlying cause of this exception
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
