package com.company.qa.core.util;

import org.apache.logging.log4j.Logger;

/**
 * Centralized logging accessor for the automation framework.
 *
 * <p>Provides a shared Log4j2 {@link Logger} instance that framework classes
 * can use for consistent, structured logging. For class-specific logging
 * (e.g. in subclasses), prefer creating a logger scoped to the concrete class.</p>
 *
 * <p><b>Usage:</b></p>
 * <pre>{@code
 * import static com.company.qa.core.util.LogManager.LOGGER;
 *
 * LOGGER.info("Driver created on thread [{}]", Thread.currentThread().getId());
 * }</pre>
 */
public class LogManager {

    /** Shared framework logger instance. */
    public static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(LogManager.class);
}
