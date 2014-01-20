package com.pw.common.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class LogWrapper {

	private static final Marker FATAL = MarkerFactory.getMarker("FATAL");
	
	private final Logger logger;

	public static final LogWrapper getLogger(Class<?> clazz) {
		return new LogWrapper(clazz);
	}

	protected LogWrapper(Class<?> clazz) {
		logger = LoggerFactory.getLogger(clazz);
	}

	public void fatal(String fmt, Object... args) {
		logger.error(FATAL, String.format(fmt, args));
	}

	public void fatal(String fmt, Throwable t, Object... args) {
		logger.error(FATAL, String.format(fmt, args), t);
	}

	public void error(String fmt, Object... args) {
		if (logger.isErrorEnabled()) logger.error(String.format(fmt, args));
	}

	public void error(String fmt, Throwable t, Object... args) {
		if (logger.isErrorEnabled()) logger.error(String.format(fmt, args), t);
	}

	public void warn(String fmt, Object... args) {
		if (logger.isWarnEnabled()) logger.warn(String.format(fmt, args));
	}

	public void warn(String fmt, Throwable t, Object... args) {
		if (logger.isWarnEnabled()) logger.warn(String.format(fmt, args), t);
	}

	public void info(String fmt, Object... args) {
		if (logger.isInfoEnabled()) logger.info(String.format(fmt, args));
	}

	public void info(String fmt, Throwable t, Object... args) {
		if (logger.isInfoEnabled()) logger.info(String.format(fmt, args), t);
	}

	public void debug(String fmt, Object... args) {
		if (logger.isDebugEnabled()) logger.debug(String.format(fmt, args));
	}

	public void debug(String fmt, Throwable t, Object... args) {
		if (logger.isDebugEnabled()) logger.debug(String.format(fmt, args), t);
	}

	public void trace(String fmt, Object... args) {
		if (logger.isTraceEnabled()) logger.trace(String.format(fmt, args));
	}

	public void trace(String fmt, Throwable t, Object... args) {
		if (logger.isTraceEnabled()) logger.trace(String.format(fmt, args), t);
	}

	public Logger getWrappedLogger() {
		return logger;
	}
}
