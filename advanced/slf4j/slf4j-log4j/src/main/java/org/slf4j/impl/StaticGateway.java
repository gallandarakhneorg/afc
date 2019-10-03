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

package org.slf4j.impl;

import java.lang.reflect.Method;

import org.slf4j.LoggerFactory;
import org.slf4j.spi.SLF4JServiceProvider;

/** Utility class for binders.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 16.0
 */
class StaticGateway {

	private SLF4JServiceProvider provider;

	/** Constructor.
	 */
	protected StaticGateway() {
		//
	}

	/**
	 * Get the SLF4J provider.
	 *
	 * @return the provider
	 */
	protected SLF4JServiceProvider getServiceProvider() {
		if (this.provider == null) {
			try {
				final Method meth = LoggerFactory.class.getMethod("getProvider"); //$NON-NLS-1$
				meth.setAccessible(true);
				this.provider = (SLF4JServiceProvider) meth.invoke(null);
			} catch (Throwable exception) {
				throw new Error(exception);
			}
		}
		return this.provider;
	}

}
