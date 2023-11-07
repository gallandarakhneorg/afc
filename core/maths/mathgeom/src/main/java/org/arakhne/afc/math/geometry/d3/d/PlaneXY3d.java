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

package org.arakhne.afc.math.geometry.d3.d;

import org.arakhne.afc.math.geometry.d3.Plane3D;
import org.arakhne.afc.math.geometry.d3.PlaneClassification;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.afp.PlaneXY3afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** This class represents a 3D X-Y plane with double floating point coordinates.
 * This type of plane represents all the planes that are always parallel to the 0-X-Y plane.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
public class PlaneXY3d extends AbstractOrthoPlane3d<PlaneXY3d>
		implements PlaneXY3afp<PlaneXY3d, Segment3d, Point3d, Vector3d, Quaternion4d> {

	private static final long serialVersionUID = 2443352106043326173L;

	/** Is the coordinate of the plane.
	 */
	private double z;

	/** Construct a plane with origin as point.
	 */
	public PlaneXY3d() {
		this.z = 0.;
	}

	/** Construct a plane with the given y coordinate.
	 * 
	 * @param positive indicates if the plan is positive or negative.
	 * @param z is the coordinate of the plane
	 */
	public PlaneXY3d(boolean positive, double z) {
		setPositive(positive);
		setZ(z);
	}

	/** Construct a plane that contains the given point.
	 *
	 * @param p is a point on the plane.
	 */
	public PlaneXY3d(boolean positive, Point3D<?, ?, ?> p) {
		assert p != null : AssertMessages.notNullParameter();
		setPositive(positive);
		setZ(p.getZ());
	}

	@Override
	public double getZ() {
		return this.z;
	}

	@Override
	public void setZ(double z) {
		if (z != this.z) {
			this.z = z;
			fireGeometryChange();
		}
	}

	@Override
	public PlaneClassification classifies(Plane3D<?, ?, ?, ?, ?> otherPlane) {
		// TODO Auto-generated method stub
		return null;
	}

}
