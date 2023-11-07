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

/** Definition of the data structures for road network.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 17.0
 */
open module org.arakhne.afc.gis.gisroad {
	requires transitive java.prefs;
	requires org.eclipse.xtext.xbase.lib;
	requires org.arakhne.afc.core.mathgraph;
	requires org.arakhne.afc.core.references;
	requires transitive org.arakhne.afc.core.util;
	requires transitive org.arakhne.afc.gis.giscore;

	exports org.arakhne.afc.gis.road;
	exports org.arakhne.afc.gis.road.layer;
	exports org.arakhne.afc.gis.road.path;
	exports org.arakhne.afc.gis.road.path.astar;
	exports org.arakhne.afc.gis.road.primitive;
}