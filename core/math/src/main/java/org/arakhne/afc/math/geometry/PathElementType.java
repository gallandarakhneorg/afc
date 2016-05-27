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

package org.arakhne.afc.math.geometry;


/** Type of a path element.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum PathElementType {

	/** Move to the next point.
	 */
	MOVE_TO,

	/** Line to the next point.
	 */
	LINE_TO,

	/** Quadratic curve to the next point.
	 */
	QUAD_TO,

	/** Cubic curve to the next point.
	 */
	CURVE_TO,

	/** Arc to the next point.
	 *
	 * <p>Arc-to is a sub-type of {@link #CURVE_TO}
	 */
	ARC_TO,

	/** Close the path.
	 */
	CLOSE;

}
