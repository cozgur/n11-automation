package com.company.qa.core.exception;

/**
 * Exception thrown when a WebDriver instance cannot be created or initialized.
 *
 * <p>Typical scenarios include:</p>
 * <ul>
 *   <li>Invalid or unreachable Appium/Selenium server URL</li>
 *   <li>Unsupported browser or platform type</li>
 *   <li>Missing or incompatible driver binaries</li>
 *   <li>Capability configuration errors that prevent driver creation</li>
 * </ul>
 *
 * @see FrameworkException
 */
public class DriverInitializationException extends FrameworkException {

    /**
     * Creates a new driver initialization exception with the specified detail message.
     *
     * @param message the detail message describing why driver creation failed
     */
    public DriverInitializationException(String message) {
        super(message);
    }

    /**
     * Creates a new driver initialization exception with the specified detail message and cause.
     *
     * @param message the detail message describing why driver creation failed
     * @param cause   the underlying cause of this exception
     */
    public DriverInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
