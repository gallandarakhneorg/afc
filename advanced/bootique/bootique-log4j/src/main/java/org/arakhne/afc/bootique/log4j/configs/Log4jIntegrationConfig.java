/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.bootique.log4j.configs;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import com.google.common.base.Strings;
import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import io.bootique.config.ConfigurationFactory;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * Configuration for log4j over SLF4J.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
@BQConfig("Configuration of Log4J")
public class Log4jIntegrationConfig {

	/**
	 * Prefix for the configuration entries of the log4j modules.
	 */
	public static final String PREFIX = "log"; //$NON-NLS-1$

	/**
	 * Name of the property that contains the logging level.
	 */
	public static final String LEVEL = PREFIX + ".level"; //$NON-NLS-1$

	/**
	 * Name of the property that indicates if the log4j configuration must be used for initializing the loggers.
	 */
	public static final String USE_LOG4J_CONFIG = PREFIX + ".useLog4jConfig"; //$NON-NLS-1$

	/**
	 * Name of the property that contains the log format.
	 */
	public static final String LOG_FORMAT = PREFIX + ".logFormat"; //$NON-NLS-1$

	/** Default conversion pattern for the logger.
	 */
	public static final String DEFAULT_LOG_FORMAT = "%-5p %m%n"; //$NON-NLS-1$

	private Level level;

	private String logFormat;

	private boolean useLog4jConfig;

	private List<AppenderConfig> appenders;

	/** Replies the configuration factory for the logging.
	 *
	 * @param configFactory the general configuration factory.
	 * @return the logging configuration factory.
	 */
	public static Log4jIntegrationConfig getConfiguration(ConfigurationFactory configFactory) {
		assert configFactory != null;
		return configFactory.config(Log4jIntegrationConfig.class, PREFIX);
	}

	/** Replies the appenders' configurations.
	 *
	 * @return the appenders' configurations.
	 */
	public List<AppenderConfig> getAppenders() {
		if (this.appenders == null) {
			this.appenders = Collections.emptyList();
		}
		return this.appenders;
	}

	/** Change the appenders' configurations.
	 *
	 * @param configs the appenders' configurations.
	 */
	@BQConfigProperty("Configuration of the appenders.")
	public void setAppenders(List<AppenderConfig> configs) {
		this.appenders = configs;
	}

	/** Replies the level.
	 *
	 * @return the level.
	 */
	public Level getLevel() {
		if (this.level == null) {
			this.level = Level.INFO;
		}
		return this.level;
	}

	/** Change the level.
	 *
	 * @param level the level.
	 */
	@BQConfigProperty("Logging level of a given logger and its children.")
	public void setLevel(Level level) {
		this.level = level;
	}

	/** Configure the given logger from the configuration.
	 *
	 * @param logger the logger to configure.
	 * @return the logger.
	 */
	@SuppressWarnings("unchecked")
	public Logger configureLogger(Logger logger) {
		final String format = getLogFormat();
		final List<AppenderConfig> appenders = getAppenders();
		if (!appenders.isEmpty()) {
			logger.removeAllAppenders();
			for (final AppenderConfig config : appenders) {
				config.createAppender(format);
			}
		} else if (!Strings.isNullOrEmpty(format)) {
			final Enumeration<? extends Appender> allAppenders  = logger.getAllAppenders();
			while (allAppenders.hasMoreElements()) {
				final Appender appender = allAppenders.nextElement();
				appender.setLayout(new PatternLayout(format));
			}
		}
		logger.setLevel(getLevel().toLog4j());
		return logger;
	}

	/** Configure the given logger from the configuration.
	 *
	 * @param loggerName the name of the logger to configure.
	 * @return the logger.
	 */
	public Logger configureLogger(String loggerName) {
		final Logger logger = Logger.getLogger(loggerName);
		return configureLogger(logger);
	}

	/** Replies if the configuration should be used for configuring the Log4j loggers.
	 *
	 * @return {@code true} for using the configuration.
	 */
	public boolean getUseLog4jConfig() {
		return this.useLog4jConfig;
	}

	/** Change the flag that indicates if the configuration should be used for configuring the Log4j loggers.
	 *
	 * @param use {@code true} for using the configuration.
	 */
	@BQConfigProperty("If true, all Bootique log4j settings are ignored and the user is expected to provide its own "
            + "config file per Log4j documentation. This is only needed for a few advanced options not directly "
            + "available via Bootique config. So the value should stay false (which is the default).")
	public void setUseLog4jConfig(boolean use) {
		this.useLog4jConfig = use;
	}

	/** Replies the format of the log.
	 *
	 * @return the format, never {@code null}.
	 */
	public String getLogFormat() {
		if (this.logFormat == null) {
			this.logFormat = DEFAULT_LOG_FORMAT;
		}
		return this.logFormat;
	}

	/** Change the format of the log.
	 *
	 * @param format the format.
	 */
	@BQConfigProperty("Log format specification used by child appenders unless redefined at the appender level, or not "
            + "relevant for a given type of appender. The spec is "
            + "compatible with Log4j framework. Default format is '" + DEFAULT_LOG_FORMAT + "'.")
	public void setLogFormat(String format) {
		this.logFormat = format;
	}

}
