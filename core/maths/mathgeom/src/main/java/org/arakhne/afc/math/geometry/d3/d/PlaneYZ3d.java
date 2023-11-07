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
import org.arakhne.afc.math.geometry.d3.afp.PlaneYZ3afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** This class represents a 3D Y-Z plane with double floating point coordinates.
 * This type of plane represents all the planes that are always parallel to the 0-Y-Z plane.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
public class PlaneYZ3d extends AbstractOrthoPlane3d<PlaneYZ3d>
		implements PlaneYZ3afp<PlaneYZ3d, Segment3d, Point3d, Vector3d, Quaternion4d> {

	private static final long serialVersionUID = 6614428850990372311L;

	/** Is the coordinate of the plane.
	 */
	private double x;

	/** Construct a plane with origin as point.
	 */
	public PlaneYZ3d() {
		this.x = 0.;
	}

	/** Construct a plane with the given y coordinate.
	 * 
	 * @param positive indicates if the plan is positive or negative.
	 * @param x is the coordinate of the plane
	 */
	public PlaneYZ3d(boolean positive, double x) {
		setPositive(positive);
		setX(x);
	}

	/** Construct a plane that contains the given point.
	 *
	 * @param p is a point on the plane.
	 */
	public PlaneYZ3d(boolean positive, Point3D<?, ?, ?> p) {
		assert p != null : AssertMessages.notNullParameter();
		setPositive(positive);
		setX(p.getX());
	}

	@Override
	public double getX() {
		return this.x;
	}

	@Override
	public void setX(double x) {
		if (x != this.x) {
			this.x = x;
			fireGeometryChange();
		}
	}

	@Override
	public PlaneClassification classifies(Plane3D<?, ?, ?, ?, ?> otherPlane) {
		// TODO Auto-generated method stub
		return null;
	}

}
