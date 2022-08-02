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

package org.arakhne.afc.gis.bus.network;

import org.arakhne.afc.gis.location.GeoLocationPoint;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
class BusStopStub extends BusStop {

	private static final long serialVersionUID = -3157645330505370940L;

	private final BusNetwork busNetwork;

	/**
	 * @param bn
	 * @param name
	 */
	public BusStopStub(BusNetwork bn, String name) {
		super(name);
		this.busNetwork = bn;
	}

	/**
	 * @param bn
	 * @param name
	 * @param position
	 */
	public BusStopStub(BusNetwork bn, String name, GeoLocationPoint position) {
		super(name, position);
		this.busNetwork = bn;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BusNetwork getBusNetwork() {
		return this.busNetwork;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BusNetwork getContainer() {
		return getBusNetwork();
	}

}
