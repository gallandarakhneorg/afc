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

package org.arakhne.afc.math.graph;

import org.eclipse.xtext.xbase.lib.Pure;

/** This interface representes a graph's segment.
 *
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface GraphSegment<ST extends GraphSegment<ST, PT>, PT extends GraphPoint<PT, ST>> {

	/** Replies the starting point of this segment.
	 *
	 * @return the starting point of this segment.
	 */
	@Pure
	PT getBeginPoint();

	/** Replies the ending point of this segment.
	 *
	 * @return the ending point of this segment.
	 */
	@Pure
	PT getEndPoint();

	/** Replies the point at the other side of the segment.
	 *
	 * @param point the reference point.
	 * @return the point at the other side of the segment.
	 */
	@Pure
	PT getOtherSidePoint(PT point);

	/** Replies the length of the segment.
	 *
	 * @return the length of the segment.
	 */
	@Pure
	double getLength();

}
