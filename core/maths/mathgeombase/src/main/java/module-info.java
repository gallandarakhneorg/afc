/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

/** Base geometry objects and utilities.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
open module org.arakhne.afc.core.mathgeombase {
	requires org.arakhne.afc.core.mathgen;
	requires transitive org.arakhne.afc.core.vmutils;
	requires org.eclipse.xtext.xbase.lib;
	
	exports org.arakhne.afc.math.geometry.base;
	exports org.arakhne.afc.math.geometry.base.coordinatesystem;
	exports org.arakhne.afc.math.geometry.base.d1;
	exports org.arakhne.afc.math.geometry.base.d2;
	exports org.arakhne.afc.math.geometry.base.d3;
	exports org.arakhne.afc.math.geometry.base.extensions.scala;
	exports org.arakhne.afc.math.geometry.base.extensions.xtext;
	exports org.arakhne.afc.math.geometry.base.matrix;
}