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

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.impl.StaticLoggerBinder;
import org.slf4j.impl.StaticMDCBinder;
import org.slf4j.impl.StaticMarkerBinder;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

/** Factory of logger.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 17.0
 */
public class MavenSlf4jServiceProvider implements SLF4JServiceProvider {

	/**
	 * Declare the version of the SLF4J API this implementation is compiled against.
	 * The value of this field is modified with each major release.
	 */
	private static final String REQUESTED_API_VERSION = "1.8.99"; //$NON-NLS-1$

	private ILoggerFactory loggerFactory;

	private IMarkerFactory markerFactory;

	private MDCAdapter mdcAdapter;

	@Override
	public ILoggerFactory getLoggerFactory() {
		return this.loggerFactory;
	}

	@Override
	public IMarkerFactory getMarkerFactory() {
		return this.markerFactory;
	}

	@Override
	public MDCAdapter getMDCAdapter() {
		return this.mdcAdapter;
	}

	@Override
	public void initialize() {
		this.loggerFactory = StaticLoggerBinder.getSingleton().getLoggerFactory();
		this.markerFactory = StaticMarkerBinder.getSingleton().getMarkerFactory();
		this.mdcAdapter = StaticMDCBinder.getSingleton().getMDCA();
	}

	@Override
	public String getRequestedApiVersion() {
		return REQUESTED_API_VERSION;
	}

}
