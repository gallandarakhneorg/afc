/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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
import org.slf4j.helpers.MarkerIgnoringBase;

/** Apache logger that is wrapping a Maven logger.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 16.0
 */
public class MavenSlf4jLogger extends MarkerIgnoringBase {

	private static final long serialVersionUID = 937126994481217789L;

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
	public void trace(String msg) {
		this.mavenLogger.debug(msg);
	}

	@Override
	public void trace(String format, Object arg) {
		this.mavenLogger.debug(MessageFormat.format(format, arg));
	}

	@Override
	public void trace(String format, Object arg1, Object arg2) {
		this.mavenLogger.debug(MessageFormat.format(format, arg1, arg2));
	}

	@Override
	public void trace(String format, Object... arguments) {
		this.mavenLogger.debug(MessageFormat.format(format, arguments));
	}

	@Override
	public void trace(String msg, Throwable t) {
		this.mavenLogger.debug(msg, t);
	}

	@Override
	public boolean isDebugEnabled() {
		return this.mavenLogger.isDebugEnabled();
	}

	@Override
	public void debug(String msg) {
		this.mavenLogger.debug(msg);
	}

	@Override
	public void debug(String format, Object arg) {
		this.mavenLogger.debug(MessageFormat.format(format, arg));
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		this.mavenLogger.debug(MessageFormat.format(format, arg1, arg2));
	}

	@Override
	public void debug(String format, Object... arguments) {
		this.mavenLogger.debug(MessageFormat.format(format, arguments));
	}

	@Override
	public void debug(String msg, Throwable t) {
		this.mavenLogger.debug(msg, t);
	}

	@Override
	public boolean isInfoEnabled() {
		return this.mavenLogger.isInfoEnabled();
	}

	@Override
	public void info(String msg) {
		this.mavenLogger.info(msg);
	}

	@Override
	public void info(String format, Object arg) {
		this.mavenLogger.info(MessageFormat.format(format, arg));
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		this.mavenLogger.info(MessageFormat.format(format, arg1, arg2));
	}

	@Override
	public void info(String format, Object... arguments) {
		this.mavenLogger.info(MessageFormat.format(format, arguments));
	}

	@Override
	public void info(String msg, Throwable t) {
		this.mavenLogger.info(msg, t);
	}

	@Override
	public boolean isWarnEnabled() {
		return this.mavenLogger.isWarnEnabled();
	}

	@Override
	public void warn(String msg) {
		this.mavenLogger.warn(msg);
	}

	@Override
	public void warn(String format, Object arg) {
		this.mavenLogger.warn(MessageFormat.format(format, arg));
	}

	@Override
	public void warn(String format, Object... arguments) {
		this.mavenLogger.warn(MessageFormat.format(format, arguments));
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		this.mavenLogger.warn(MessageFormat.format(format, arg1, arg2));
	}

	@Override
	public void warn(String msg, Throwable t) {
		this.mavenLogger.warn(msg, t);
	}

	@Override
	public boolean isErrorEnabled() {
		return this.mavenLogger.isErrorEnabled();
	}

	@Override
	public void error(String msg) {
		this.mavenLogger.error(msg);
	}

	@Override
	public void error(String format, Object arg) {
		this.mavenLogger.error(MessageFormat.format(format, arg));
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		this.mavenLogger.error(MessageFormat.format(format, arg1, arg2));
	}

	@Override
	public void error(String format, Object... arguments) {
		this.mavenLogger.error(MessageFormat.format(format, arguments));
	}

	@Override
	public void error(String msg, Throwable t) {
		this.mavenLogger.error(msg,  t);
	}

}
