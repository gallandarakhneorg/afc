/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.slf4j.impl;

import org.apache.maven.plugin.logging.Log;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

import org.arakhne.afc.slf4j.maven.MavenSlf4jLoggerFactory;

/** The binding of {@link LoggerFactory} class with an actual instance of
 * {@link ILoggerFactory} is performed using information returned by this class.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 16.0
 */
public final class StaticLoggerBinder implements LoggerFactoryBinder {

	/**
	 * The unique instance of this class.
	 */
	private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

	private static final String LOGGER_FACTORY_CLASS_STR = MavenSlf4jLoggerFactory.class.getName();

	private Log mavenLogger;

	private StaticLoggerBinder() {
		//
	}

	/**
	 * Return the singleton of this class.
	 *
	 * @return the StaticLoggerBinder singleton
	 */
	public static StaticLoggerBinder getSingleton() {
		return SINGLETON;
	}

	/** Register the Maven Logger.
	 *
	 * @param logger Maven Logger.
	 */
	public void registerMavenLogger(Log logger) {
		this.mavenLogger = logger;
	}

	/** Register the Maven Logger.
	 *
	 * @return Maven Logger.
	 */
	public Log getMavenLogger() {
		return this.mavenLogger;
	}

	@Override
	public ILoggerFactory getLoggerFactory() {
		return new MavenSlf4jLoggerFactory(getMavenLogger());
	}

	@Override
	public String getLoggerFactoryClassStr() {
		return LOGGER_FACTORY_CLASS_STR;
	}
}
