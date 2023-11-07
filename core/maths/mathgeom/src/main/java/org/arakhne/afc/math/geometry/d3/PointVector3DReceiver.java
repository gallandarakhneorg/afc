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

package org.arakhne.afc.math.geometry.d3;

/**
 * Receiver of a 3D point and a 3D vector.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
@FunctionalInterface
public interface PointVector3DReceiver {

	/**
	 * Invoked when the receiver should get the 3 coordinates of the 3D point
	 * and the 3 coordinates of the 3D vector.
	 *
	 * @param px x coordinate of the point.
	 * @param py y coordinate of the point.
	 * @param pz z coordinate of the point.
	 * @param vx x coordinate of the vector.
	 * @param v2 y coordinate of the vector.
	 * @param z2 z coordinate of the v.
	 */
	void set(double px, double py, double pz, double vx, double vy, double vz);

}
