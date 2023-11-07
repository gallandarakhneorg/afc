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

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.eclipse.xtext.xbase.lib.Pure;

/** Abstract plane that is one of the orthogonal planes with 2 double precision floating-point numbers.
 *
 * @param <T> the type of the plane.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
public abstract class AbstractOrthoPlane3d<T extends AbstractOrthoPlane3d<T>> extends AbstractPlane3d<T> {

	private static final long serialVersionUID = -1508357850482332388L;

	/** Indicates if this plane is oriented to the positive side.
	 * If {@code true}, the normal of the plane is directed
	 * to the positive infinity.
	 */ 
	private boolean isPositive = true;

	/** Indicates if this plane is oriented to the positive side.
	 * If {@code true}, the normal of the plane is directed
	 * to the positive infinity.
	 *
	 * @return {@code true} if the plan normal is positive.
	 */ 
	public boolean isPositive() {
		return this.isPositive;
	}

	/** Change if this plane is oriented to the positive side.
	 * If {@code true}, the normal of the plane is directed
	 * to the positive infinity.
	 *
	 * @param positive {@code true} if the plan normal is positive.
	 */ 
	public void setPositive(boolean positive) {
		if (positive != this.isPositive) {
			this.isPositive = positive;
			fireGeometryChange();
		}
	}

	@Override
	public void negate() {
		this.isPositive = !this.isPositive;
		fireGeometryChange();
	}

	@Override
	public void absolute() {
		if (!isPositive()) {
			negate();
		}
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public T normalize() {
		return (T) this;
	}

	@Override
	public void rotate(Quaternion<?, ?, ?> rotation, Point3D<?, ?, ?> pivot) {
		// Do nothing because othrthogonal planes cannot be rotated
	}

}
