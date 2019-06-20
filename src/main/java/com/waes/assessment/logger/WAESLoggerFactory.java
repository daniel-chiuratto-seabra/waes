package com.waes.assessment.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

/**
 * This class is an encapsulation of the traditional way of
 * instatiaging the {@link Logger} instance using {@link LoggerFactory} to
 * allow the addition of the {@code if} statements automatically for each
 * type of logging, approach that is considered as a good practice in quality
 * tools such as SonarQube, and also help with additions on loggin like a
 * prefix or something like that, because the entire application pass through
 * one of those methods on the logs
 * 
 * @author Daniel Chiuratto Seabra
 *
 */
public class WAESLoggerFactory implements Logger {

	private static Logger LOGGER;
	
	private WAESLoggerFactory(final Class<?> clazz) {
		LOGGER = LoggerFactory.getLogger(clazz);
	}

	/**
	 * This static method returns an instance of the {@link Logger} interface
	 * implementation
	 * 
	 * @param clazz containing the Class type which will be logged
	 * @return {@link Logger} implementation instance to be used on the logging
	 */
	public static Logger getLogger(final Class<?> clazz) {
		return new WAESLoggerFactory(clazz);
	}
	
	@Override
	public String getName() {
		return LOGGER.getName();
	}

	@Override
	public boolean isTraceEnabled() {
		return LOGGER.isTraceEnabled();
	}

	@Override
	public void trace(final String msg) {
		if (this.isTraceEnabled())
			LOGGER.trace(msg);
	}

	@Override
	public void trace(final String format, final Object arg) {
		if (this.isTraceEnabled())
			LOGGER.trace(format, arg);
	}

	@Override
	public void trace(final String format, final Object arg1, final Object arg2) {
		if (this.isTraceEnabled())
			LOGGER.trace(format, arg1, arg2);
	}

	@Override
	public void trace(final String format, final Object... arguments) {
		if (this.isTraceEnabled())
			LOGGER.trace(format, arguments);
	}

	@Override
	public void trace(final String msg, final Throwable t) {
		if (this.isTraceEnabled())
			LOGGER.trace(msg, t);
	}

	@Override
	public boolean isTraceEnabled(final Marker marker) {
		return LOGGER.isTraceEnabled(marker);
	}

	@Override
	public void trace(final Marker marker, final String msg) {
		if (this.isTraceEnabled())
			LOGGER.trace(marker, msg);
	}

	@Override
	public void trace(final Marker marker, final String format, final Object arg) {
		if (this.isTraceEnabled())
			LOGGER.trace(marker, format, arg);
	}

	@Override
	public void trace(final Marker marker, final String format, final Object arg1, final Object arg2) {
		if (this.isTraceEnabled())
			LOGGER.trace(marker, format, arg1, arg2);
	}

	@Override
	public void trace(final Marker marker, final String format, final Object... argArray) {
		if (this.isTraceEnabled())
			LOGGER.trace(marker, format, argArray);
	}

	@Override
	public void trace(final Marker marker, final String msg, final Throwable t) {
		if (this.isTraceEnabled())
			LOGGER.trace(marker, msg, t);
	}
	
	@Override
	public boolean isDebugEnabled() {
		return LOGGER.isDebugEnabled();
	}

	@Override
	public void debug(final String msg) {
		if (this.isDebugEnabled())
			LOGGER.debug(msg);
	}

	@Override
	public void debug(final String format, final Object arg) {
		if (this.isDebugEnabled())
			LOGGER.debug(format, arg);
	}

	@Override
	public void debug(final String format, final Object arg1, final Object arg2) {
		if (this.isDebugEnabled())
			LOGGER.debug(format, arg1, arg2);
	}

	@Override
	public void debug(final String format, final Object... arguments) {
		if (this.isDebugEnabled())
			LOGGER.debug(format, arguments);
	}

	@Override
	public void debug(final String msg, final Throwable t) {
		if (this.isDebugEnabled())
			LOGGER.debug(msg, t);
	}

	@Override
	public boolean isDebugEnabled(final Marker marker) {
		return LOGGER.isDebugEnabled(marker);
	}

	@Override
	public void debug(final Marker marker, final String msg) {
		if (this.isDebugEnabled())
			LOGGER.debug(marker, msg);
	}

	@Override
	public void debug(final Marker marker, final String format, final Object arg) {
		if (this.isDebugEnabled())
			LOGGER.debug(marker, format, arg);
	}

	@Override
	public void debug(final Marker marker, final String format, final Object arg1, final Object arg2) {
		if (this.isDebugEnabled())
			LOGGER.debug(marker, format, arg1, arg2);
	}

	@Override
	public void debug(final Marker marker, final String format, final Object... arguments) {
		if (this.isDebugEnabled())
			LOGGER.debug(marker, format, arguments);
	}

	@Override
	public void debug(final Marker marker, final String msg, final Throwable t) {
		if (this.isDebugEnabled())
			LOGGER.debug(marker, msg, t);
	}

	@Override
	public boolean isInfoEnabled() {
		return LOGGER.isInfoEnabled();
	}

	@Override
	public void info(final String msg) {
		if (this.isInfoEnabled())
			LOGGER.info(msg);
	}

	@Override
	public void info(final String format, final Object arg) {
		if (this.isInfoEnabled())
			LOGGER.info(format, arg);
	}

	@Override
	public void info(final String format, final Object arg1, final Object arg2) {
		if (this.isInfoEnabled())
			LOGGER.info(format, arg1, arg2);
	}

	@Override
	public void info(final String format, final Object... arguments) {
		if (this.isInfoEnabled())
			LOGGER.info(format, arguments);
	}

	@Override
	public void info(final String msg, final Throwable t) {
		if (this.isInfoEnabled())
			LOGGER.info(msg, t);
	}

	@Override
	public boolean isInfoEnabled(final Marker marker) {
		return LOGGER.isInfoEnabled(marker);
	}

	@Override
	public void info(final Marker marker, final String msg) {
		if (this.isInfoEnabled())
			LOGGER.info(marker, msg);
	}

	@Override
	public void info(final Marker marker, final String format, final Object arg) {
		if (this.isInfoEnabled())
			LOGGER.info(marker, format, arg);
	}

	@Override
	public void info(final Marker marker, final String format, final Object arg1, final Object arg2) {
		if (this.isInfoEnabled())
			LOGGER.info(marker, format, arg1, arg2);
	}

	@Override
	public void info(final Marker marker, final String format, final Object... arguments) {
		if (this.isInfoEnabled())
			LOGGER.info(marker, format, arguments);
	}

	@Override
	public void info(final Marker marker, final String msg, final Throwable t) {
		if (this.isInfoEnabled())
			LOGGER.info(marker, msg, t);
	}
	
	@Override
	public boolean isWarnEnabled() {
		return LOGGER.isWarnEnabled();
	}

	@Override
	public void warn(final String msg) {
		if (this.isWarnEnabled())
			LOGGER.warn(msg);
	}

	@Override
	public void warn(final String format, final Object arg) {
		if (this.isWarnEnabled())
			LOGGER.warn(format, arg);
	}

	@Override
	public void warn(final String format, final Object... arguments) {
		if (this.isWarnEnabled())
			LOGGER.warn(format, arguments);
	}

	@Override
	public void warn(final String format, final Object arg1, final Object arg2) {
		if (this.isWarnEnabled())
			LOGGER.warn(format, arg1, arg2);
	}

	@Override
	public void warn(final String msg, final Throwable t) {
		if (this.isWarnEnabled())
			LOGGER.warn(msg, t);
	}

	@Override
	public boolean isWarnEnabled(final Marker marker) {
		return LOGGER.isWarnEnabled(marker);
	}

	@Override
	public void warn(final Marker marker, final String msg) {
		if (this.isWarnEnabled())
			LOGGER.warn(marker, msg);
	}

	@Override
	public void warn(final Marker marker, final String format, final Object arg) {
		if (this.isWarnEnabled())
			LOGGER.warn(marker, format, arg);
	}

	@Override
	public void warn(final Marker marker, final String format, final Object arg1, final Object arg2) {
		if (this.isWarnEnabled())
			LOGGER.warn(marker, format, arg1, arg2);
	}

	@Override
	public void warn(final Marker marker, final String format, final Object... arguments) {
		if (this.isWarnEnabled())
			LOGGER.warn(marker, format, arguments);
	}

	@Override
	public void warn(final Marker marker, final String msg, final Throwable t) {
		if (this.isWarnEnabled())
			LOGGER.warn(marker, msg, t);
	}
	
	@Override
	public boolean isErrorEnabled() {
		return LOGGER.isErrorEnabled();
	}

	@Override
	public void error(final String msg) {
		if (this.isErrorEnabled())
			LOGGER.error(msg);
	}

	@Override
	public void error(final String format, final Object arg) {
		if (this.isErrorEnabled())
			LOGGER.error(format, arg);
	}

	@Override
	public void error(final String format, final Object arg1, final Object arg2) {
		if (this.isErrorEnabled())
			LOGGER.error(format, arg1, arg2);
	}

	@Override
	public void error(final String format, final Object... arguments) {
		if (this.isErrorEnabled())
			LOGGER.error(format, arguments);
	}

	@Override
	public void error(final String msg, final Throwable t) {
		if (this.isErrorEnabled())
			LOGGER.error(msg, t);
	}

	@Override
	public boolean isErrorEnabled(final Marker marker) {
		return LOGGER.isErrorEnabled(marker);
	}

	@Override
	public void error(final Marker marker, final String msg) {
		if (this.isErrorEnabled())
			LOGGER.error(marker, msg);
	}

	@Override
	public void error(final Marker marker, final String format, final Object arg) {
		if (this.isErrorEnabled())
			LOGGER.error(marker, format, arg);
	}

	@Override
	public void error(final Marker marker, final String format, final Object arg1, final Object arg2) {
		if (this.isErrorEnabled())
			LOGGER.error(marker, format, arg1, arg2);
	}

	@Override
	public void error(final Marker marker, final String format, final Object... arguments) {
		if (this.isErrorEnabled())
			LOGGER.error(marker, format, arguments);
	}

	@Override
	public void error(final Marker marker, final String msg, final Throwable t) {
		if (this.isErrorEnabled())
			LOGGER.error(marker, msg, t);
	}
}
