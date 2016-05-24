/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

/** This interface is a listener invoked by the subgraph builder
 * each time a new subgraph segment was reached.
 *
 * @param <PT> is the type of node in the graph
 * @param <ST> is the type of edge in the graph
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated since 13.0
 */
@Deprecated
@SuppressWarnings("all")
public class SubGraphBuildAdapter<ST extends GraphSegment<ST, PT>, PT extends GraphPoint<PT, ST>> implements SubGraphBuildListener<ST, PT> {

	@Override
	public void segmentAdded(SubGraph<ST, PT, ?> graph, GraphIterationElement<ST, PT> elementDescription) {
		//
	}

	@Override
	public void terminalPointReached(SubGraph<ST, PT, ?> graph, PT point, ST arrivingSegment) {
		//
	}

	@Override
	public void nonTerminalPointReached(SubGraph<ST, PT, ?> graph, PT point, ST arrivingSegment) {
		//
	}

}
