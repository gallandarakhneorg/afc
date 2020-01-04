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

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import io.bootique.config.PolymorphicConfiguration;
import org.apache.log4j.Appender;
import org.apache.log4j.Layout;

/**
 * Configuration for log4j appenders.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
@BQConfig("Appender of a given type.")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = ConsoleAppenderConfig.class)
public abstract class AppenderConfig implements PolymorphicConfiguration {

	private String logFormat;

	private LayoutType layout;

	/** Replies the format of the log.
	 *
	 * @return the format, or {@code null} if no format was specified.
	 */
	public String getLogFormat() {
		return this.logFormat;
	}

	/** Change the format of the log.
	 *
	 * @param format the format.
	 */
	@BQConfigProperty("Log format specification compatible with Log4j framework. If not set, the value is propagated "
			+ "from the parent configuration.")
	public void setLogFormat(String format) {
		this.logFormat = format;
	}

	/** Replies the type of layout that should be used by this appender. Default is {@link LayoutType#PATTERN}.
	 *
	 * @return the type of layout.
	 */
	public LayoutType getLayout() {
		if (this.layout == null) {
			this.layout = LayoutType.PATTERN;
		}
		return this.layout;
	}

	/** Change the type of layout that should be used by this appender.
	 *
	 * @param type the type of the layout.
	 */
	@BQConfigProperty("Type of layout that should be used by this appender. Default is PATTERN.")
	public void setLayout(LayoutType type) {
		this.layout = type;
	}

	/** Create an appender with the current configuration.
	 *
	 * @param defaultLogFormat the default log format.
	 * @return the appender.
	 */
	public abstract Appender createAppender(String defaultLogFormat);

	/** Create a layout from this appender configuration.
	 *
	 * @param defaultLogFormat the default log format.
	 * @return the layout.
	 */
	protected Layout createLayout(String defaultLogFormat) {
		final LayoutType type = getLayout();
		return type.createLayout(defaultLogFormat);
	}

}
