package com.company.qa.core.exception;

/**
 * Base exception for the automation framework.
 * All framework-specific exceptions extend this class.
 *
 * <p>This is an unchecked exception (extends {@link RuntimeException}) to avoid
 * cluttering test code with checked-exception handling while still providing
 * a clear hierarchy for framework errors.</p>
 */
public class FrameworkException extends RuntimeException {

    /**
     * Creates a new framework exception with the specified detail message.
     *
     * @param message the detail message describing the error
     */
    public FrameworkException(String message) {
        super(message);
    }

    /**
     * Creates a new framework exception with the specified detail message and cause.
     *
     * @param message the detail message describing the error
     * @param cause   the underlying cause of this exception
     */
    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
