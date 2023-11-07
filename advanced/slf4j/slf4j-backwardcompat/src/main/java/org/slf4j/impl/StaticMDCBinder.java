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

package org.slf4j.impl;

import org.slf4j.helpers.BasicMDCAdapter;
import org.slf4j.spi.MDCAdapter;

/** This implementation is bound to {@link BasicMDCAdapter}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 16.0
 */
public final class StaticMDCBinder extends StaticGateway {

	/**
	 * The unique instance of this class.
	 */
	public static final StaticMDCBinder SINGLETON = new StaticMDCBinder();

	private StaticMDCBinder() {
		//
	}

	/**
	 * Return the singleton of this class.
	 *
	 * @return the StaticMDCBinder singleton
	 */
	public static StaticMDCBinder getSingleton() {
		return SINGLETON;
	}

	/**
	 * Return the instance of {@link MDCAdapter} that
     * {@code MDC} should bind to.
	 *
	 * @return the adapter.
	 */
	public MDCAdapter getMDCAdapter() {
		return getServiceProvider().getMDCAdapter();
	}

}
