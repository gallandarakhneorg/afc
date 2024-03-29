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

package org.arakhne.afc.math.graph.astar;

import org.arakhne.afc.math.graph.GraphSegment;

/** This interface representes a graph's segment.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
class SegmentStub implements GraphSegment<SegmentStub,ConnectionStub> {

	/**
	 */
	public final ConnectionStub c1;
	/**
	 */
	public final ConnectionStub c2;
	
	private final String id;
	
	/**
	 * @param id1
	 * @param c11
	 * @param c21
	 */
	public SegmentStub(String id1, ConnectionStub c11, ConnectionStub c21) {
		this.id = id1;
		this.c1 = c11;
		this.c2 = c21;
		this.c1.segments.add(this);
		this.c2.segments.add(this);
	}
	
	@Override
	public String toString() {
		return this.id;
	}

	/** {@inheritDoc}
	 */
	@Override
	public ConnectionStub getBeginPoint() {
		return this.c1;
	}

	/** {@inheritDoc}
	 */
	@Override
	public ConnectionStub getEndPoint() {
		return this.c2;
	}

	/** {@inheritDoc}
	 */
	@Override
	public ConnectionStub getOtherSidePoint(ConnectionStub point) {
		if (point==this.c1) return this.c2;
		return this.c1;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public double getLength() {
		return 1.;
	}

}
