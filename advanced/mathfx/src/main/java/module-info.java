/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

/** JavaFX implementation of the geometry objects.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 17.0
 */
module org.arakhne.afc.advanced.mathfx {
	requires org.eclipse.xtext.xbase.lib;
	requires transitive javafx.base;
	requires transitive org.arakhne.afc.core.vmutils;
	requires org.arakhne.afc.core.mathgen;
	requires transitive org.arakhne.afc.core.mathgeom;
	requires transitive org.arakhne.afc.advanced.javafx;

	exports org.arakhne.afc.math.geometry.d1.dfx;
	exports org.arakhne.afc.math.geometry.d2.dfx;
	exports org.arakhne.afc.math.geometry.d2.ifx;
	exports org.arakhne.afc.math.geometry.d3.dfx;
	exports org.arakhne.afc.math.geometry.d3.ifx;
	exports org.arakhne.afc.math.geometry.fx;
}