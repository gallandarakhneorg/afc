/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.slf4j.maven;

import java.text.MessageFormat;

import org.apache.maven.plugin.logging.Log;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.helpers.LegacyAbstractLogger;

/** Apache logger that is wrapping a Maven logger.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 16.0
 */
public class MavenSlf4jLogger extends LegacyAbstractLogger {

	private static final long serialVersionUID = -5538811471324622933L;

	private final Log mavenLogger;

	/** Constructor.
	 *
	 * @param name the name of the logger.
	 * @param mavenLogger the logger.
	 */
	MavenSlf4jLogger(String name, Log mavenLogger) {
		this.name = name;
		this.mavenLogger = mavenLogger;
	}

	@Override
	public boolean isTraceEnabled() {
		return this.mavenLogger.isDebugEnabled();
	}

	@Override
	public boolean isDebugEnabled() {
		return this.mavenLogger.isDebugEnabled();
	}

	@Override
	public boolean isInfoEnabled() {
		return this.mavenLogger.isInfoEnabled();
	}

	@Override
	public boolean isWarnEnabled() {
		return this.mavenLogger.isWarnEnabled();
	}

	@Override
	public boolean isErrorEnabled() {
		return this.mavenLogger.isErrorEnabled();
	}

	@Override
	protected String getFullyQualifiedCallerName() {
		return null;
	}

	@Override
	protected void handleNormalizedLoggingCall(Level level, Marker marker, String msg, Object[] arguments,
			Throwable throwable) {
		final String expandedMessage = MessageFormat.format(msg, arguments);
		if (level != null) {
			switch (level) {
			case INFO:
				if (throwable != null) {
					this.mavenLogger.info(expandedMessage, throwable);
				} else {
					this.mavenLogger.info(expandedMessage);
				}
				break;
			case WARN:
				if (throwable != null) {
					this.mavenLogger.warn(expandedMessage, throwable);
				} else {
					this.mavenLogger.warn(expandedMessage);
				}
				break;
			case ERROR:
				if (throwable != null) {
					this.mavenLogger.error(expandedMessage, throwable);
				} else {
					this.mavenLogger.error(expandedMessage);
				}
				break;
			case DEBUG:
			case TRACE:
				if (throwable != null) {
					this.mavenLogger.debug(expandedMessage, throwable);
				} else {
					this.mavenLogger.debug(expandedMessage);
				}
				break;
			default:
				break;
			}
		}
	}

}
